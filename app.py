from flask import Flask, request
import socket
# Comment for Github version log
app = Flask(__name__)

def get_request_info():
    host_name = socket.gethostname()
    host_ip = socket.gethostbyname(host_name)
    data = '------------------------ \n' \
           'You accessed to path "{}"\n' \
           'Access Server URL : {} \n' \
           'Container Hostname : {} \n' \
           'Container IP : {} \n' \
           'Original IP with Proxy : {}\n\n' \
           '------------------------ \n' \
           'Flask received HTTP header : \n'\
           '{}\n' \
           '------------------------\n'.format(request.path, request.base_url, host_name, host_ip,
             request.environ.get('HTTP_X_REAL_IP', request.remote_addr), request.headers)
    return data

@app.route('/')
def path_root():
    return get_request_info()

@app.route('/color')
def path_color():
    return get_request_info()

@app.route('/color/red')
def path_color_red():
    return get_request_info()

@app.route('/color/blue')
def path_color_blue():
    return get_request_info()

if __name__ == '__main__':
    app.run(host='0.0.0.0')
