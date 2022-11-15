package com.huemap.backend.infrastructure.socket;

import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.huemap.backend.domain.report.dto.response.ConditionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocketService {

	public void sendSocketMessage(SocketIOClient senderClient, ConditionResponse conditionResponse, String room) {
		for (
			SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
			client.sendEvent("send", conditionResponse);
		}
	}

}
