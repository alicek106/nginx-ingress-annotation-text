from requests_opentracing import SessionTracing
import time
import logging
from jaeger_client import Config
from flask_opentracing import FlaskTracing
from flask import Flask, request
import os
import socket
import random
import requests

JAEGER_HOST = os.environ['CONF_JAEGER_HOST']
JAEGER_PORT = os.environ['CONF_JAEGER_PORT']
SLEEP_ENABLED = os.environ['CONF_SLEEP_ENABLED']
REQUEST_TO_SPRING_ENABLED = os.environ['REQUEST_TO_SPRING_ENABLED']
SPRING_ENDPOINT = os.environ['SPRING_ENDPOINT']

def process(path):
    with jaeger_tracer.start_active_span('spring server call start') as scope:
        scope.span.log_kv({'event': 'spring start', 'result': 1})
        if REQUEST_TO_SPRING_ENABLED == "true":
            opentracing_tracer = jaeger_tracer
            traced_session = SessionTracing(opentracing_tracer, propagate=True,  # propagation allows distributed tracing in
                                            span_tags=dict(my_helpful='tag'))     # upstream services you control (True by default).
            resp = traced_session.get(SPRING_ENDPOINT + path)
            logging.info('Response code of spring request : {}'.format(resp.status_code))
    with jaeger_tracer.start_active_span('spring server call start end') as scope:
        if SLEEP_ENABLED == "true":
            time.sleep(random.randint(1, 10) / 10)
        scope.span.log_kv({'event': 'spring end', 'result': 2})

def print_header():
    host_name = socket.gethostname()
    host_ip = socket.gethostbyname(host_name)
    data = '------------------------ \n' \
           'You accessed to path "{}"\n' \
           'Access Server URL : {} \n' \
           'Container Hostname : {} \n' \
           'Container IP : {} \n' \
           'Original IP with Proxy : {}\n' \
           'Static string : {}\n\n' \
           '------------------------ \n' \
           'Flask received HTTP header : \n' \
           '{}\n' \
           '------------------------\n'.format(request.path, request.base_url, host_name, host_ip,
                                               request.environ.get('HTTP_X_REAL_IP', request.remote_addr), '',
                                               request.headers)
    logging.info(data)
    return data

app = Flask(__name__)

# Create configuration object with enabled logging and sampling of all requests.
config = Config(
    config=
    {
        'sampler': {'type': 'const', 'param': 1},
        'logging': True,
        'propagation': 'b3',
        'local_agent': {'reporting_host': JAEGER_HOST, 'reporting_port': JAEGER_PORT}
    },
    # Service name can be arbitrary string describing this particular web service.
    service_name="alicek106-flask-test",
)

jaeger_tracer = config.initialize_tracer()
tracing = FlaskTracing(jaeger_tracer)

@app.route('/')
@tracing.trace()
def flask_root_path():
    process('/')
    return print_header()

@app.route('/red')
@tracing.trace()
def flask_red():
    process('/red')
    return print_header()

@app.route('/red/apple')
@tracing.trace()
def flask_red_apple():
    process('/red/apple')
    return print_header()

if __name__ == '__main__':
    log_level = logging.DEBUG
    logging.getLogger('').handlers = []
    logging.basicConfig(format='%(asctime)s %(message)s', level=log_level)
    app.run(debug=True, host='0.0.0.0', port=80)
