package com.firstgame.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/chat")
public class GeneralConfigWebSocket {
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static String currentPlayer = "X";

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        try {
            ObjectNode response = mapper.createObjectNode();
            response.put("type", "state_response");
            response.put("currentPlayer", currentPlayer);
            session.getBasicRemote().sendText(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        JsonNode jsonNode = mapper.readTree(message);
        String type = jsonNode.get("type").asText();

        if ("game".equals(type)) {
//            int row = jsonNode.get("row").asInt();
//            int col = jsonNode.get("col").asInt();
//            String player = jsonNode.get("player").asText();
            String nextPlayer = jsonNode.get("nextPlayer").asText();

            // Spielnachricht an alle senden
            broadcastMessage(message);
            currentPlayer = nextPlayer;
        }
        else if ("chat".equals(type)) {
            broadcastMessage(message);  // Sende die Chat-Nachricht an alle Clients
        }
        else if ("state_request".equals(type)) {
            ObjectNode response = mapper.createObjectNode();
            response.put("type", "state_response");
            response.put("currentPlayer", currentPlayer);
            session.getBasicRemote().sendText(response.toString());
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    private void broadcastMessage(String message) {
        // Перебираємо всі відкриті сесії і відправляємо кожній
        for (Session session : sessions) {
            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
