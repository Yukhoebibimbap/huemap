<template>
    
      <div id="map"></div>
    
  </template>
  
  
  <script>
  
  export default {
    data() {
      return {
        map: null,
        markers: [],
        latitude: 0,
        longitude: 0
      }
    },
    created() {
      if (!("geolocation" in navigator)) {
        return;
      }
  
      // get position
      navigator.geolocation.getCurrentPosition(pos => {
        this.latitude = pos.coords.latitude;
        this.longitude = pos.coords.longitude;
  
        if (window.kakao && window.kakao.maps) {
  
          this.initMap();
  
        } else {
          const script = document.createElement("script");
          /* global kakao */
          script.onload = () => kakao.maps.load(this.initMap);
          script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${process.env.VUE_APP_KAKAO_KEY}`;
          document.head.appendChild(script);
        }
  
      }, err => {
        alert(err.message);
      })
    },
    async mounted (){

      // await this.$axios.get(`/api/healthCheck`)
      //   .then(()=>{
      //     alert('dd')
      //   })
      //   .catch(()=>{alert("로그인 실패")})

      await this.$axios.get(`/api/v1/bins?type=GENERAL`)
        .then(()=>{
          alert('dd')
        })
        .catch(()=>{alert("로그인 실패")})
            
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
    width: 1000px;
    height: 1000px;
    border: 1px #a8a8a8 solid;
  }
  </style>