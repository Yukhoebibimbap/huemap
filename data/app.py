from flask import Flask,jsonify,request
import configparser
from flask_mysqldb import MySQL
from service.kmeansCluster import kmeans

app = Flask(__name__)

config = configparser.ConfigParser()
config.read('.env')
mysql = MySQL()


app.config['MYSQL_USER'] = config['local']['user']
app.config['MYSQL_PASSWORD'] = config['local']['password']
app.config['MYSQL_DB'] = config['local']['database']
app.config['MYSQL_HOST'] = config['local']['host']
app.config['MYSQL_CURSORCLASS'] = 'DictCursor'
app.config['MYSQL_PORT'] = 3306
mysql.init_app(app)

def db_connection():
    try:
        cursor = mysql.connection.cursor()
    except Exception as error:
            raise error
    return cursor

def fetchall_cursor(fetchall_query):
    cursor = db_connection()
    cursor.execute(fetchall_query)
    row = cursor.fetchall()

    return row


@app.route('/data/healthCheck',methods = ['GET'])
def index():
    return "휴맵 플라스크 서버"

@app.route('/data/bins',methods = ['GET'])
def cluster():

    query="""select ST_Y(location) as x, ST_X(location) as y from suggestion where type='{}' and gu='{}' and """\
    """(created_at between '{}' and '{}')""".format(request.args["type"],request.args["gu"],request.args["startDate"],request.args["endDate"])

    bins=fetchall_cursor(query)

    return kmeans(bins, int(request.args["k"]))


if __name__ == '__main__':
    app.run(host='0.0.0.0')