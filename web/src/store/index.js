import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    location:new Map([
        ['도봉구', [127.01985135699999, 37.700884901999984]],
        ['노원구', [127.084252351000032, 37.691883454999981]],
        ['중랑구', [127.112933878000035, 37.620163032999983]],
        ['강북구', [127.006072320000044, 37.684984327]],
        ['성북구', [126.985957048000046, 37.635775851]],
        ['강동구', [127.181528596000021, 37.555520547000015]],
        ['동대문구', [127.071232348999956, 37.606424048]],
        ['송파구' ,[127.113162559999978, 37.542955674999973]],
        ['강남구', [127.040208034999978, 37.53577826899999]],
        ['서초구' ,[127.01320492100001, 37.522609073000012]],
        ['관악구' ,[126.928419835, 37.49487056800001]],
        ['동작구' ,[126.955566886000042, 37.515711216]],
        ['금천구' ,[126.880610239000021, 37.485828814]],
        ['영등포구', [126.881409902999962, 37.55581268899999]],
        ['양천구' ,[126.874412989, 37.546945632000018]],
        ['구로구' ,[126.882034987999987, 37.516106482]],
        ['강서구' ,[126.808452938999949, 37.6010638828]],
        ['마포구' ,[126.88994293899998, 37.58399065499998]],
        ['서대문구' ,[126.952672967000012, 37.602716214]],
        ['은평구' ,[126.952333383999985, 37.654917855]],
        ['종로구' ,[126.974691051000036, 37.62974306000001]],
        ['중구', [127.02338131199997, 37.571917440999982]],
        ['용산구', [126.972424008999951, 37.554879888000016]],
        ['성동구', [127.045726676999948, 37.57135506899999]],
        ['광진구', [127.102626147000024, 37.572211536999987]],
    ]),
  },
  getters: {
    location:state=>{return state.location},

  },
  mutations: {

  },
  actions: {
  },
  modules: {
  },
  plugins:[
    createPersistedState()
  ],

})