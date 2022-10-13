FROM python:3

WORKDIR /app

ADD . /app
RUN python3 -m pip install -U pip
RUN pip3 install flask
RUN pip3 install uwsgi


CMD ["uwsgi","uwsgi.ini"]
