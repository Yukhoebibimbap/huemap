<template>
    <v-container>
      <v-row>
      <div id="map"></div>
      <v-select
          v-model="binType"
          :items="binTypeList"
          item-text="text"
          label="폐수거함 유형"
          outlined
          @change="setBinType"
          return-object
    ></v-select>
    <v-text-field
            v-model="kmeans_k"
            :counter="2"
            label="폐수거함 설치 갯수"
            required
    ></v-text-field>
    <v-btn @click="clustering()">클러스터링</v-btn>
  </v-row>
    </v-container>
    
      
      
  </template>
  
  
  <script>
  export default {
    data() {
      return {
        map: null,
        markers: [],
        latitude: 33.450701,
        longitude: 126.570667,
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
        binType:"",
        kmeans_k:4
      }
    },
    computed:{

    },
    async mounted (){
      if (window.kakao && window.kakao.maps) {
        this.initMap();
      } else {
        const script = document.createElement("script");
        
        script.onload = () => kakao.maps.load(this.initMap);
        script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${process.env.VUE_APP_KAKAO_KEY}`;
        document.head.appendChild(script);
      } 


        await this.$axios.get(`/api/v1/suggestions/bin-location?gu=${this.$route.params.gu}&type=${this.$route.params.binType}&startDate=${this.startDate}&endDate=${this.endDate}`)
        .then(result=>{
          const {data}=result
          if (data.data.length>0){
            this.displayMarker(data.data)
          }
        })
        .catch(err=>{})

      
      },
    methods: {
      initMap() {
          const container = document.getElementById("map");
          const options = {
            center: new kakao.maps.LatLng(37.50856152, 127.0450995),
            level: 7,
          };
          this.map = new kakao.maps.Map(container, options);
        },
       
      async displayMarker(dataList) {

        var bins = []

        for(let data of dataList){
          
          bins.push({
            position : new kakao.maps.LatLng(data.latitude, data.longitude),
          })
          
        }
        
        if (bins.length > 0) {
          for(let bin of bins){
            this.markers.push(
              new kakao.maps.Marker({
                  map: this.map,
                  position:bin.position,
                })
            )
          }
        
          const bounds = bins.reduce(
              (bounds, bin) => bounds.extend(bin.position),
              new kakao.maps.LatLngBounds()
          );
  
          this.map.setBounds(bounds);
        }
          
      },

      async setBinType(){
        this.$router.push(`/suggestion/${this.$route.params.gu}/${this.binType.value}`)
      },

      async displayCluster(dataList) {
        console.log(dataList)
        var bins = []

        for(let data of dataList){

          bins.push({
            position : new kakao.maps.LatLng(data[0], data[1]),
          })
          
        }

        if (bins.length > 0) {
          for(let bin of bins){
            this.markers.push(
              new kakao.maps.Marker({
                  map: this.map,
                  position:bin.position,
                })
            )
          }

          const bounds = bins.reduce(
              (bounds, bin) => bounds.extend(bin.position),
              new kakao.maps.LatLngBounds()
          );

          this.map.setBounds(bounds);
        }
          
      },

      async clustering(){
        if (this.markers.length > 0) {
        this.markers.forEach((marker) => marker.setMap(null));
        }

        await this.$axios.get(`/data/bins?k=${this.kmeans_k}&gu=${this.$route.params.gu}&type=${this.$route.params.binType}&startDate=${this.startDate}&endDate=${this.endDate}`)
        .then(result=>{
          const {data}=result

          if (data.length>0){
            this.displayCluster(data)
          }
        })
        .catch(err=>{})
        
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