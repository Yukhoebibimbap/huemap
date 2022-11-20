<template>
    <v-container>
      <v-row>
      <div id="map"></div>
      <v-select
          v-model="conditionType"
          :items="conditionTypeList"
          item-text="text"
          label="폐수거함 유형"
          outlined
          @change="setConditionType"
          return-object
    ></v-select>
  </v-row>
    </v-container>
    
      
      
  </template>
  
  
  <script>
  import io from 'socket.io-client/dist/socket.io'
  import '../assets/css/overlay.css';

  export default {
    data() {
      return {
        map: null,
        markers: [],
        latitude: 33.450701,
        longitude: 126.570667,
        address:"",
        startDate: "2020-01-01T00:00:00",
        endDate:"2023-01-01T00:00:00",
        binTypeList:[
          {text:'일반쓰레기' ,value:'GENERAL'},
          {text:'재활용' ,value:'RECYCLE'},
          {text:'의류 수거함', value: 'CLOTHES'},
          {text:'폐건전지' ,value:'BATTERY'},
          {text:'폐형광등' ,value:'LAMP'},
          {text:'폐의약품' ,value:'MEDICINE'},
        ],
        conditionTypeList:[
          {text:'가득 참' ,value:'FULL'},
          {text:'손상' ,value:'DAMAGED'},
          {text:'더러움', value: 'DIRTY'},
        ],
        conditionType:"",
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
    },
    async mounted (){
      if (window.kakao && window.kakao.maps) {
        
        this.initMap();
      } else {
        const script = document.createElement("script");
        /* global kakao */
        script.onload = () => kakao.maps.load(this.initMap);
        script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${process.env.VUE_APP_KAKAO_KEY}`;
        document.head.appendChild(script);
      } 


      this.socket.on('send', (data) => {
        if(data.type==this.$route.params.conditionType){
            this.displayMarker([data]);
          }
        });


        await this.$axios.get(`/api/v1/bins/report-condition?gu=${this.$route.params.gu}&type=${this.$route.params.conditionType}&startDate=${this.startDate}&endDate=${this.endDate}`)
        .then(result=>{
          const {data}=result
          console.log(data)
          if (data.data.length>0){
            this.displayMarker(data.data)
          }
        })
        .catch(err=>{console.log(err)})

      
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
            let type, binType

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

            switch (data.binType){
              case "GENERAL": 
                binType="일반 쓰레기통" 
                break;
              case "RECYCLE": 
                binType="재활용 쓰레기통" 
                break;
              case "CLOTHES": 
                binType="의류 수거함" 
              case "BATTERY": 
                binType="폐건전지 수거함" 
                break;
              case "MEDICINE": 
                binType="의류 수거함"
              case "LAMP": 
                binType="폐형광등 수거함"
                break;
            }
            
            bins.push({
              position : new kakao.maps.LatLng(data.latitude, data.longitude),
              content:  '<div class="overlay">' +`<img src=${data.img} style="width: 100%;
                                height: 105px;
                                border-radius: 8px 8px 0 0;
                                object-fit: cover"/>` +
                        '    <div class="overlayInfoFrame">' +
                        `        <p class="overlayTime">${createdAt}</p>` +
                        `        <p class="overlayInfo">${data.address}</p>` +
                        `        <p class="overlayInfo">${binType}</p>` +
                        `        <p class="overlayInfo">${type}</p>` +
                        '    </div>' +
                        '</div>'
            
            })
            
          }
          
          if (bins.length > 0) {
            this.markers = bins.map(
                (bin) =>{
                  
                  var marker=new kakao.maps.Marker({
                    map: this.map,
                    position:bin.position,
                  })
                  

                  // var infowindow = new kakao.maps.CustomOverlay({
                  //     position : bin.position, 
                  //     content : bin.content
                  // });

 
                  // kakao.maps.event.addListener(marker, 'mouseover', this.makeOverListener(this.map, marker, infowindow));
                  // kakao.maps.event.addListener(marker, 'mouseout', this.makeOutListener(infowindow));

                  var customOverlay = new kakao.maps.CustomOverlay({
                    position: bin.position,
                    content: bin.content,
                  });
                  

                  kakao.maps.event.addListener(marker, 'mouseover', ()=> {
                    customOverlay.setMap(this.map);
                  });

                  kakao.maps.event.addListener(marker, 'mouseout', ()=> {
                    setTimeout(()=> {
                      customOverlay.setMap(null);
                    })
                  });
                  
                   
                }
            );
    
          }
          const bounds = bins.reduce(
                (bounds, bin) => bounds.extend(bin.position),
                new kakao.maps.LatLngBounds()
            );
    
            this.map.setBounds(bounds);
          
        },

        makeOverListener(map, marker, infowindow) {
            return function() {
                infowindow.setMap(map);
            };
        },

        // 인포윈도우를 닫는 클로저를 만드는 함수입니다 
        makeOutListener(infowindow) {
            return function() {
                infowindow.setMap();
            };
        },
        async setConditionType(){
          this.$router.push(`/state/${this.$route.params.gu}/${this.conditionType.value}`)
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