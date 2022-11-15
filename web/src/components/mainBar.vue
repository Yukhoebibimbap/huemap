<template>
    <v-app-bar
      app
      color="#212121"
      dark

    >

    서울특별시 폐수거함 모니터링
    <v-spacer></v-spacer>

      <v-btn href="/state/select/gu" color="primary">폐수거함 상태</v-btn>
    </v-app-bar>
</template>

<script>
  export default {
    name: 'MenuBar',
    data: () => ({
        
    }),
     mounted(){
  },
    computed: {
    
  },
    methods:{
        
    }
  }
</script>