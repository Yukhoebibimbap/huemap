<template>
    <v-app-bar
      app
      color="#212121"
      dark

    >
    <v-btn href="/" color="#212121" elevation="0">서울특별시 폐수거함 모니터링</v-btn>
    
    <v-spacer></v-spacer>
      <v-btn href="/state/select/gu" color="#212121" elevation="0">폐수거함 상태</v-btn>
      <v-btn href="/suggestion/select/gu" color="#212121" elevation="0">폐수거함 건의</v-btn>
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