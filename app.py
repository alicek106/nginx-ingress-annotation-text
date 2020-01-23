from flask import Flask, request
from pathlib import Path
import time
import socket
# Comment for Github version log
app = Flask(__name__)

def get_request_info(path):
    my_file = Path("/file")
    if my_file.is_file():
        print("file exists")
        pass
    else:
        print("sleep")
        time.sleep(10)
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
           'Flask received HTTP header : \n'\
           '{}\n' \
           '------------------------\n'.format(request.path, request.base_url, host_name, host_ip,
             request.environ.get('HTTP_X_REAL_IP', request.remote_addr), path, request.headers)
    return data

@app.route('/')
def path_root():
    return get_request_info('/')

@app.route('/color')
def path_color():
    return get_request_info('/color')

@app.route('/color/red')
def path_color_red():
    return get_request_info('/color/red')

@app.route('/color/blue')
def path_color_blue():
    return get_request_info('/color/blue')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=80)
