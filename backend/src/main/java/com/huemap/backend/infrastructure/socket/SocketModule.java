package com.huemap.backend.infrastructure.socket;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SocketModule {

	private final SocketIOServer server;
	private final SocketService socketService;

	public SocketModule(SocketIOServer server, SocketService socketService) {
		this.server = server;
		this.socketService = socketService;
		server.addConnectListener(onConnected());
		server.addDisconnectListener(onDisconnected());
	}

	private ConnectListener onConnected() {

		return (client) -> {
			var params = client.getHandshakeData().getUrlParams();
			String room = params.get("room").stream().collect(Collectors.joining());
			client.joinRoom(room);

			log.info("Socket ID[{}] - room[{}] - Connected to socket",
				client.getSessionId().toString(), room);
		};

	}

	private DisconnectListener onDisconnected() {
		return (client) -> {
			var params = client.getHandshakeData().getUrlParams();
			String room = params.get("room").stream().collect(Collectors.joining());
			client.joinRoom(room);

			log.info("Socket ID[{}] - room[{}] - Disconnected to socket",
				client.getSessionId().toString(), room);
		};
	}


}
