FROM ubuntu:16.04
LABEL "maintainer=alice_k106@naver.com"
WORKDIR /root
RUN apt update && \
  apt install python3 python3-pip -y && \ 
  pip3 install -r requirements.txt && \
  apt clean autoclean && \
  apt autoremove --yes && \
  rm -rf /var/lib/{apt,dpkg,cache,log}
ADD ["requirements.txt", "app.py", "/root/"]
CMD ["python3", "/root/app.py"] 
