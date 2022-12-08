<template>
    <v-container width="100%" height="100%" fluid class="ma-0 pa-0">
      <v-row>
        
      <v-card
      height="800px"
      width="20%"
      class="rounded-0"
      elevation="8"
      color="#212121"
      dark
      fluid
    >
    <v-card elevation="0" color="#212121"><v-card-title></v-card-title></v-card>
    <v-card elevation="0" color="#212121"><v-card-title></v-card-title></v-card>
    <v-card elevation="0" color="#212121"><v-card-title></v-card-title></v-card>
    <v-row justify="center">
      <v-icon class="mx-2" color="#898989">mdi-delete-empty</v-icon><v-btn elevation="0" @click="setBinType('GENERAL')" color="#212121">일반 쓰레기</v-btn>
    </v-row>

    <v-row justify="center">
      <v-icon class="mx-2" color="#898989">mdi-recycle</v-icon><v-btn elevation="0" @click="setBinType('RECYCLE')" color="#212121">재활용</v-btn>
    </v-row>

    <v-row justify="center">
      <v-icon class="mx-2" color="#898989">mdi-tshirt-crew</v-icon><v-btn elevation="0" @click="setBinType('CLOTHES')" color="#212121">의류수거함</v-btn>
    </v-row>

    <v-row justify="center">
      <v-icon class="mx-2" color="#898989">mdi-battery-charging</v-icon><v-btn elevation="0" @click="setBinType('BATTERY')" color="#212121">폐건전지</v-btn>
    </v-row>
    <v-row justify="center">
      <v-icon class="mx-2" color="#898989">mdi-medical-bag</v-icon><v-btn elevation="0" @click="setBinType('MEDICINE')" color="#212121">폐의약품</v-btn>
    </v-row>
    <v-row justify="center">
      <v-icon class="mx-2" color="#898989">mdi-lightbulb-on</v-icon><v-btn elevation="0" @click="setBinType('LAMP')" color="#212121">폐형광등</v-btn>
    </v-row>
    

    <v-row justify="center" class="mt-10">
      <v-col
      cols="12"
          sm="6"
        >
      <v-text-field
            v-model="kmeans_k"
            :counter="2"
            label="폐수거함 설치 갯수"
            required
    ></v-text-field>
    </v-col>
    </v-row>
    <v-row justify="center">
      <v-icon class="mx-2" color="#898989">mdi-google-analytics</v-icon><v-btn @click="clustering()" elevation="0" color="#212121">클러스터링</v-btn>
    </v-row>

    </v-card>



      <div id="map"></div>
      
    
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
        kmeans_k:4,
      }
    },
    computed:{
      location () {
        return this.$store.getters.location
      }
    },
    async mounted (){
      console.log(this.location)
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
            center: new kakao.maps.LatLng(this.location.get(this.$route.params.gu)[1], this.location.get(this.$route.params.gu)[0]),
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

      async setBinType(type){
        this.$router.push(`/suggestion/${this.$route.params.gu}/${type}`)
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

  
  #map {
    width: 80%;
    height: 800px;
    border: 1px #a8a8a8 solid;
  }

 
  </style>