<template>
    <v-container>
      <div id="map"></div>
    </v-container>
      
      
      
  </template>
  
  
  <script>
  import io from 'socket.io-client/dist/socket.io'
  
  export default {
    data() {
      return {
        map: null,
        markers: [],
        latitude: 33.450701,
        longitude: 126.570667,
        address:""
      }
    },
    computed:{

      socket(){
        return io.connect(`http://localhost:8081`,{
          cors:{origin:'*'},
          path: '/socket.io',
          query: { room: this.$route.params.gu }
        })
      }
    },
    created() {
          if (window.kakao && window.kakao.maps) {
        
        this.initMap();
      } else {
        const script = document.createElement("script");
        /* global kakao */
        script.onload = () => kakao.maps.load(this.initMap);
        script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${process.env.VUE_APP_KAKAO_KEY}`;
        document.head.appendChild(script);
      } 
      
    },
    async mounted (){
      this.socket.on('send', (data) => {
        this.displayMarker([data]);
        });
      
      },
    methods: {
      initMap() {
          const container = document.getElementById("map");
          const options = {
            center: new kakao.maps.LatLng(37.50856152, 127.0450995),
            level: 7,
          };
          this.map = new kakao.maps.Map(container, options);
          
          // this.displayMarker([[this.latitude, this.longitude]]);
        },
       
       async displayMarker(dataList) {

          var bins = []

          for(let data of dataList){
            let date = new Date(data.createdAt)

            let createdAt=`${date.getMonth()+1}월 ${date.getDate()}일 ${date.getHours()}:${date.getMinutes()}`
            let type

            switch (data.type){
              case "FULL": 
                type="가득 참" 
                break;
              case "DAMAGED": 
                type="손상" 
                break;
              case "DIRTY": 
                type="더러움" 
                break;

            }
            await this.getAddress(data.latitude, data.longitude)
              .then((address)=>{
                bins.push({
                  position : new kakao.maps.LatLng(data.latitude, data.longitude),
                  content: `<div style="width:180px;">${createdAt}</div><div>${address}</div><div>${type}</div><img src="${data.img}" style="width:160px; height:105px;"/>`
              })
            })
          }
          
          if (bins.length > 0) {
            this.markers = bins.map(
                (bin) =>{
                  
                  var marker=new kakao.maps.Marker({
                    map: this.map,
                    position:bin.position,
                  })
                  

                  var infowindow = new kakao.maps.InfoWindow({
                      position : bin.position, 
                      content : bin.content
                  });

                  infowindow.open(this.map, marker);

                }
            );
    
          }
          const bounds = bins.reduce(
                (bounds, bin) => bounds.extend(bin.position),
                new kakao.maps.LatLngBounds()
            );
    
            this.map.setBounds(bounds);
          
        },


        async getAddress(lat, long){
          let address=null

          await this.$axios
              .get(
                `https://dapi.kakao.com/v2/local/geo/coord2address.json?x=${long}&y=${lat}`,
                {
                  headers: {
                    Authorization: `KakaoAK ${process.env.VUE_APP_KAKAO_REST_API_KEY}`,  
                  },
                },
              )
              .then(res => {
                address=res.data.documents[0].address.address_name
              })
              .catch(err=>{
                console.log(err)
              })

            return address
        }
    }
  }
  </script>
  
  
  <style scoped>
  /* .test {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100vh;
  } */
  
  #map {
    width: 800px;
    height: 600px;
    border: 1px #a8a8a8 solid;
  }
  </style>