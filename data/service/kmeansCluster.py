import pandas as pd
from sklearn.cluster import KMeans
from flask import jsonify

def kmeans(bins):
    x=[]
    y=[]
    for bin in bins:
        
        x.append(bin["x"])
        y.append(bin["y"])
  

    feature=pd.DataFrame({'x':x,'y':y})

    kmeans = KMeans(n_clusters=4)
    kmeans.fit(feature)     
    centroids  = kmeans.cluster_centers_

    resp = jsonify(tuple(map(tuple, centroids)))
    resp.status_code=200
    return resp