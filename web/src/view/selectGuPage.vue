<template>
    <v-container>
      <div id="pollution-map"></div>
      <v-btn href="/state/강남구" color="primary">강남구</v-btn>
    </v-container>
      
      
      
  </template>
  
  
  <script>
  import io from 'socket.io-client/dist/socket.io'
  
  export default {
    data() {
      return {
       
      }
    },
    computed:{
    socket(){
      // return io.connect(`http://localhost:8081/room`,{
      //   cors:{origin:'*'},
      //   path: '/socket.io',
      // })
    }
    },
    async mounted (){
      
        // this.socket.on()
 
 
            
      },
    methods: {
        
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