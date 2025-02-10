package com.firstgame.application.service;

// die Jackson-Bibliothek. die wird zur Verarbeitung von JSON-Daten verwendet. JSON-Daten in Java-Objekte umzuwandeln
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
// Jakarta WebSocket API. die wird zur Verarbeitung von JSON-Daten verwendet
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

//  WebSocket-Server-Komponente

@Component
@ServerEndpoint("/chat")
public class GeneralConfigWebSocket {
    // Speichert alle aktiven WebSocket-Sitzungen
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();
    // JSON-Daten in Java-Objekte umwandeln
    private static final ObjectMapper mapper = new ObjectMapper();
    private static String currentPlayer = "X";

    // Wird aufgerufen, wenn ein neuer Client eine Verbindung herstellt.

    @OnOpen
    public void onOpen(Session session){
        // Wenn ein neuer Spieler beitritt, wird die Session in die sessions-Liste aufgenommen.
        sessions.add(session);
        try {
            // Erlaubt das Erstellen und Manipulieren von JSON-Objekten.
            ObjectNode response = mapper.createObjectNode();
            response.put("type", "state_response");
            response.put("currentPlayer", currentPlayer);
            session.getBasicRemote().sendText(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    Diese Methode wird aufgerufen, wenn der Server eine Nachricht vom Client erhält.
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        // Die Nachricht wird als JSON-Objekt geparst.
        JsonNode jsonNode = mapper.readTree(message);
        // type bestimmt, welche Art von Nachricht empfangen wurde.
        String type = jsonNode.get("type").asText();

        if ("game".equals(type)) {
            String nextPlayer = jsonNode.get("nextPlayer").asText();
            // Spielnachricht an alle senden
            broadcastMessage(message);
            currentPlayer = nextPlayer;
        }
        else if ("chat".equals(type)) {
            broadcastMessage(message);  // Sende die Chat-Nachricht an alle Clients
        }
//        Spielstatus-Abfrage
        else if ("state_request".equals(type)) {
            ObjectNode response = mapper.createObjectNode();
            response.put("type", "state_response");
            response.put("currentPlayer", currentPlayer);
            session.getBasicRemote().sendText(response.toString());
        }
//        Spiel zurücksetzen
        else if ("reset".equals(type)) {
            currentPlayer = "X";
            broadcastMessage(message);
            System.out.println(jsonNode.get("type").asText());
        }
    }

    //  Wird aufgerufen, wenn ein Client die Verbindung schließt.
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

//    Durchläuft alle aktiven Sessions und sendet die Nachricht weiter.
    private void broadcastMessage(String message) {
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
