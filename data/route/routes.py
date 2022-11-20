from app import db_connection,app

app_route = Blueprint('first_route',__name__)

def fetchall_cursor(fetchall_query):
    cursor = db_connection()
    cursor.execute(fetchall_query)
    row = cursor.fetchall()
    resp = jsonify(row)
    resp.status_code=200
    return resp

@app.route('/data/healthCheck',methods = ['GET'])
def index():
    return "휴맵 플라스크 서버"

@app.route('/data/bins',methods = ['GET'])
def cluster():
    all_items =fetchall_cursor("""SELECT * FROM bin""")
    return all_items







