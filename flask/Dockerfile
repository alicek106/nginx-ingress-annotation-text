FROM python:3
ADD main.py /
ADD requirements.txt /
EXPOSE 80
RUN pip3 install requests && pip3 install -r requirements.txt
CMD ["python3", "main.py"]
