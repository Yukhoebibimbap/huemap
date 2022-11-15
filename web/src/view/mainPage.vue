<template>
  <v-container>
    <div id="map"></div>
      <v-btn @click="draw('GENERAL')">일반</v-btn>
      <v-btn @click="draw('RECYCLE')">재활용</v-btn>
      <v-btn @click="draw('CLOTHES')">의류수거함</v-btn>
      <v-btn @click="draw('BATTERY')">폐건전지</v-btn>
      <v-btn @click="draw('MEDICINE')">폐의약품</v-btn>
      <v-btn @click="draw('LAMP')">폐형광등</v-btn>
  </v-container>
    
    
    
</template>


<script>

export default {
  data() {
    return {
      map: null,
      markers: [],
      latitude: 33.450701,
      longitude: 126.570667
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
    
    // await this.$axios.get(`/api/healthCheck`)
    //   .then(()=>{
    //     alert('dd')
    //   })
    //   .catch(()=>{alert("로그인 실패")})

    // await this.$axios.get(`/api/v1/bins?type=GENERAL`)
    //   .then(()=>{
    //     alert('dd')
    //   })
    //   .catch(()=>{alert("로그인 실패")})
          
    },
  methods: {
    initMap() {
        const container = document.getElementById("map");
        const options = {
          center: new kakao.maps.LatLng(33.450701, 126.570667),
          level: 4,
        };
        this.map = new kakao.maps.Map(container, options);
        
        this.displayMarker([[this.latitude, this.longitude]]);
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
          console.log(list)
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
  width: 800px;
  height: 600px;
  border: 1px #a8a8a8 solid;
}
</style>