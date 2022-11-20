<template>
  <v-app>
    <MainBar/>
    <v-main>
      <router-view :key="$route.fullPath"/>
    </v-main>
  </v-app>
</template>

<script>
import MainBar from './components/mainBar';

export default {
  name: 'App',

  components: {
    MainBar,
  },

  data: () => ({
    //
  }),
};
</script>
