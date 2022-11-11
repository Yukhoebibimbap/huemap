<template>
    <v-container>
        <v-expansion-panel>
        <v-expansion-panel-header><h1>채팅</h1></v-expansion-panel-header>
        <v-expansion-panel-content>
        <v-card class="mt-2">
            <v-card-title><h2>채팅 테스트</h2></v-card-title>
            <v-card-text>
                      <v-alert
                        dense
                        type="info"
                        color="teal lighten-3"
                    >두개 이상의 브라우저를 띄워 간단한 채팅을 확인해 볼 수 있다.</v-alert>
                    <v-container>
                        uid : 
                        <input
                            v-model="uid"
                            type="text"
                        >
                        message : 
                        <input
                            v-model="message"
                            type="text"
                            @keyup="sendMessage"
                        >
                        <div
                            class="mt-2"
                            v-for="(item, idx) in recvList"
                            :key="idx"
                        >
                            <v-card
                                class="mt-2 mb-2"
                                color="teal lighten-3"
                                dark
                                max-width="400"
                            >
                                <v-card-text>
                                    <div>uid : {{ item.user.id }}</div>
                                    <div>{{ item.message }}</div>
                                    <div>{{ item.sendAt }}</div>
                                </v-card-text>
                            </v-card>
                        </div>
                    </v-container>
            </v-card-text>
        </v-card>
        </v-expansion-panel-content>
    </v-expansion-panel>
    </v-container>
</template>

<script lang="js">
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

export default {
    data() {
        return {
            uid: '',
            message: '',
            recvList: []
            }
    },
    created() {
        this.connect() // ChattingView.vue 생성 시 소켓 연결 시도
    },
    methods: {
        sendMessage(e) {
            if(e.keyCode === 13 && this.uid !== '' && this.message !== '') {
                this.send();
                this.message = '';
            }
        },
        send() {
            console.log("Send message:" + this.message);
            if (this.stompClient && this.stompClient.connected) {
                const msg = { 
                user: {
                    id: this.uid
                },
                message: this.message,
                sendAt: Date.now(),
                isRequest: false,
                };
                this.stompClient.send("/receive", JSON.stringify(msg), {});
            }
        }, 
        connect() {
            const serverURL = "http://localhost:8080"
            let socket = new SockJS(serverURL);
            this.stompClient = Stomp.over(socket);
            console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
            this.stompClient.connect(
                {},
                frame => {
                this.connected = true;
                console.log('소켓 연결 성공', frame);
                this.stompClient.subscribe("/send", res => {
                    console.log('구독으로 받은 메시지 입니다.', res.body);
                    this.recvList.push(JSON.parse(res.body))
                });
                },
                error => {
                console.log('소켓 연결 실패', error);
                this.connected = false;
                } 
            );               
        }
    }
}
</script>