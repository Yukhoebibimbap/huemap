from flask import Blueprint, render_template

app_route = Blueprint('first_route',__name__)

@app_route.route('/data/healthCheck',methods = ['GET'])
def index():
    return "휴맵 플라스크 서버"
