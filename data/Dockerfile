FROM python:3

WORKDIR /app

ADD . /app
RUN python3 -m pip install -U pip
RUN pip3 install flask
RUN pip3 install uwsgi
RUN pip install flask-mysqldb
RUN pip install pandas
RUN pip install scikit-learn
CMD ["uwsgi","uwsgi.ini"]
