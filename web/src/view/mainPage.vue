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
    <v-card elevation="0"><v-card-title></v-card-title></v-card>
    <v-card elevation="0"><v-card-title></v-card-title></v-card>
    <v-card elevation="0"><v-card-title></v-card-title></v-card>
    <v-row justify="center">
      <v-icon class="mx-2" color="#898989">mdi-delete-empty</v-icon><v-btn elevation="0" @click="draw('GENERAL')" color="#212121">일반 쓰레기</v-btn>
    </v-row>

    <v-row justify="center">
      <v-icon class="mx-2" color="#898989">mdi-recycle</v-icon><v-btn elevation="0" @click="draw('RECYCLE')" color="#212121">재활용</v-btn>
    </v-row>

    <v-row justify="center">
      <v-icon class="mx-2" color="#898989">mdi-tshirt-crew</v-icon><v-btn elevation="0" @click="draw('CLOTHES')" color="#212121">의류수거함</v-btn>
    </v-row>

    <v-row justify="center">
      <v-icon class="mx-2" color="#898989">mdi-battery-charging</v-icon><v-btn elevation="0" @click="draw('BATTERY')" color="#212121">폐건전지</v-btn>
    </v-row>
    <v-row justify="center">
      <v-icon class="mx-2" color="#898989">mdi-medical-bag</v-icon><v-btn elevation="0" @click="draw('MEDICINE')" color="#212121">폐의약품</v-btn>
    </v-row>
    <v-row justify="center">
      <v-icon class="mx-2" color="#898989">mdi-lightbulb-on</v-icon><v-btn elevation="0" @click="draw('LAMP')" color="#212121">폐형광등</v-btn>
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
      latitude: 37.554879888000016,
      longitude: 126.972424008999951
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
    
    },
  methods: {
    initMap() {
        const container = document.getElementById("map");
        const options = {
          center : new kakao.maps.LatLng(37.554879888000016, 126.972424008999951),
          level: 9,
        };
        this.map = new kakao.maps.Map(container, options);
        
      },
      displayMarker(markerPositions) {
        
        if (this.markers.length > 0) {
          this.markers.forEach((marker) => marker.setMap(null));
        }
  
        const positions = markerPositions.map(
            (position) => new kakao.maps.LatLng(...position)
        );
  
        if (positions.length > 0) {
          this.markers = positions.map(
              (position) =>
                  new kakao.maps.Marker({
                    map: this.map,
                    position,
                  })
          );
  
          const bounds = positions.reduce(
              (bounds, latlng) => bounds.extend(latlng),
              new kakao.maps.LatLngBounds()
          );
  
          this.map.setBounds(bounds);
        }
      },

      async draw(type){

        let list=[]

        await this.$axios.get(`/api/v1/bins?type=${type}`)
        .then((result)=>{
          const {data}=result
          data.data.map(x=>{
            list.push([x.latitude,x.longitude])
          })
        })
        .catch(()=>{alert("실패")})

        this.displayMarker(list);
      },
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
  width: 80%;
  height: 800px;
  border: 1px #a8a8a8 solid;
}
</style>