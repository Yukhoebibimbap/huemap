

export default class kakaoService {

    static getAddress = async (lat,long)=>{
        let address=null
    
        await this.$axios
            .get(
              `https://dapi.kakao.com/v2/local/geo/coord2address.json?x=${long}&y=${lat}`,
              {
                headers: {
                  Authorization: `KakaoAK ${process.env.VUE_APP_KAKAO_REST_API_KEY}`,  
                },
              },
            )
            .then(res => {
              address=res.data.documents[0].address.address_name
            })
            .catch(err=>{
              console.log(err)
            })
    
          return address
      }

}
