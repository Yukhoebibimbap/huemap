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
    print("로우",type(row))
    # resp = jsonify(row)
    # resp.status_code=200
    return row


@app.route('/data/healthCheck',methods = ['GET'])
def index():
    return "휴맵 플라스크 서버"

@app.route('/data/bins',methods = ['GET'])
def cluster():
    bins=fetchall_cursor("""select ST_Y(location) as x, ST_X(location) as y from suggestion where type='GENERAL'""")
    d=kmeans(bins)
    print(type(d))
    return d


if __name__ == '__main__':
    app.run(host='0.0.0.0')