<template>

    <v-card
        class="mx-auto my-12 overflow-y-auto"
        max-width="500"
        max-height="1000"
        elevation="0"
      >
        <v-card class="mx-auto my-12">
          <v-card elevation="0">
                  <div class="pa-10">
                    <h1 style="text-align: center" class="mb-10"></h1>
                    
                    <div id="chat-list">
                        <div class="mine">
                          
                          <div class="chat"></div>
                        </div>
                        <div class="other">

                        </div>
                      
                    </div>
                    <!-- <v-btn
                          color="black"
                          block
                          class="my-8 rounded-pill btn"
                          @click="send()"
                        >
                          <h3>전송</h3>
                    </v-btn> -->
                      <!-- <v-row>
                        <v-col width="80%">
                        <v-form ref="form">
                        <v-text-field
                        v-model="message"
                        label="채팅"
                        ></v-text-field>
                        </v-form>
                        </v-col>
                        <v-col>
                        <v-btn
                          type="submit"
                          color="black"
                          block
                          class="my-8 rounded-pill btn"
                          @click="send()"
                        >
                          <h3>전송</h3>
                        </v-btn>
                        </v-col>
                      </v-row> -->
                     
                  </div>
                </v-card>
    
        </v-card>
    
        
      </v-card>
    
    </template>
      
    
    <script>
    import io from 'socket.io-client/dist/socket.io'
    
    export default {
      data () {
        return {
          chats:[],
          user:{},
          message:''
        }
      },
      computed:{

        socket(){
          return io.connect(`http://localhost:8081`,{
            cors:{origin:'*'},
            path: '/socket.io',
            query: { room: this.$route.params.gu }
          })
        }
      },
      async mounted (){
    
        this.socket.on('send', (data) => {
          const div = document.createElement('div');
            div.classList.add('mine');
    
          const chat = document.createElement('div');
          chat.textContent = data.gu+" "+data.img+" "+data.latitude+" "+data.longitude+" "+data.type+" "+data.createdAt;
          div.appendChild(chat);
    
          const div2= document.createElement('div');
          div2.appendChild(div)
          
          document.querySelector('#chat-list').appendChild(div2);
          
    
        });
    
         
      },
    
      methods:{
        onScroll () {
            this.scrollInvoked++
          },
          send(){
            const div = document.createElement('div');
            div.classList.add('mine');

    
          const chat = document.createElement('div');
          chat.textContent = "ㅋㅋ";
          div.appendChild(chat);
    
          const div2= document.createElement('div');
          div2.appendChild(div)
          
          document.querySelector('#chat-list').appendChild(div2);
          }
          
      }
    
    }
    </script>
    
    
    <style>
    h3{color: #1CFFA0;}
    .mine { text-align: right; }
    .other { text-align: left; }
    .mine div:first-child, .other div:first-child { font-size: 12px; }
    .mine div:last-child, .other div:last-child {
      display: inline-block;
      border: 1px solid silver;
      border-radius: 5px;
      padding: 2px 5px;
      max-width: 300px;
    }
    
    /* .chat {position:relative; margin: 50px; padding: 20px; width:200px; height:60px; color: #FFF; border-radius: 10px; background-color: #000;}
    .chat:after {content:""; position: absolute; top: 21px; right: -30px; border-left: 30px solid #000; border-top: 10px solid transparent; border-bottom: 10px solid transparent;} */
    </style>