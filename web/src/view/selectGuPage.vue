<template>
    <v-container width="100%" fluid class="ma-0 pa-0">
      <div id="map"></div>
    </v-container>
  </template>
  
  
  <script>
  import geojson from "../assets/seoul.geojson"
  import '../assets/css/overlay.css';

  export default {
    data() {
      return {
        map:null
      }
    },
    created(){

    },
     async mounted (){

      await this.init()
      setTimeout(this.draw,300);

    },
    methods: {
      async initMap() {
        
          const container = document.getElementById("map");
          const options = {
            center: new kakao.maps.LatLng(37.554879888000016, 126.972424008999951),
            level: 9,
          };
          this.map = new kakao.maps.Map(container, options);
          
        },
      
    displayArea(coordinates, name) {
      
     
        var path = [];            
        var points = [];        
        
        for(let coordinate of coordinates[0]){
          
          var point = new Object(); 
            point.x = coordinate[1];
            point.y = coordinate[0];
            points.push(point);
            path.push(new kakao.maps.LatLng(coordinate[1], coordinate[0])); 
            
        }
        
        
        var polygon = new kakao.maps.Polygon({
            map : this.map, 
            path : path,
            strokeWeight : 2,
            strokeColor : '#004c80',
            strokeOpacity : 0.8,
            fillColor : '#fff',
            fillOpacity : 0.7
        });
        polygon.setMap(this.map)
        
        var customOverlay = new kakao.maps.CustomOverlay({
                  });

        kakao.maps.event.addListener(polygon, 'mouseover', (mouseEvent)=> {
          
            polygon.setOptions({
                fillColor : '#09f'
            });
     
            customOverlay.setContent('<div class="area">' + name + '</div>');
     
            customOverlay.setPosition(mouseEvent.latLng);
            customOverlay.setMap(this.map);
        });
     
         
        kakao.maps.event.addListener(polygon, 'mousemove', (mouseEvent)=> {
          
            customOverlay.setPosition(mouseEvent.latLng);
        });
     
        
        kakao.maps.event.addListener(polygon, 'mouseout', ()=> {
          
            polygon.setOptions({
                fillColor : '#fff'
            });
            customOverlay.setMap(null);
        });
     
      
        kakao.maps.event.addListener(polygon, 'click', ()=> {
          this.$router.push(`/${this.$route.params.type}/${name}/GENERAL`)   
        });

    },

    centroid (points) {
        var i, j, len, p1, p2, f, area, x, y;
    
        area = x = y = 0;
    
        for (i = 0, len = points.length, j = len - 1; i < len; j = i++) {
                p1 = points[i];
                p2 = points[j];
    
                f = p1.y * p2.x - p2.y * p1.x;
                x += (p1.x + p2.x) * f;
                y += (p1.y + p2.y) * f;
                area += f * 3;
        }

        return new daum.maps.LatLng(x / area, y / area);
    },

    async init(){
      if (window.kakao && window.kakao.maps) {
        this.initMap();
      } else {

        const script = document.createElement("script");
        script.onload = () => kakao.maps.load(this.initMap);

        script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${process.env.VUE_APP_KAKAO_KEY}`;
        document.head.appendChild(script);
      }
    },

    async draw(){
      for(let val of geojson.features){
          this.displayArea(val.geometry.coordinates, val.properties.SIG_KOR_NM);
      }
    }


    }
  }
  </script>
  
  
  <style scoped>
  
  #map {
    width: 100%;
    height: 800px;
    border: 1px #a8a8a8 solid;
  }
  </style>
  

