# Flask Server to Test Annotations Nginx Ingress

This Docker image provides some endpoints to test nginx ingress controller in Kubernetes.

- /
- /color
- /color/blue and /color/red

Endpoints return request information as below.

```
(venv) root@kubernetes-dev-5ffd5db99d-q9xsv:/# curl localhost:5000/color/red

-----
You accessed to path "/color/red"
Access Server URL : http://localhost:5000/color/red
Container Hostname : kubernetes-dev-5ffd5db99d-q9xsv
Container IP : 10.1.0.16
-----
```

