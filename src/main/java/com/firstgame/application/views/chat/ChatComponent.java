package com.firstgame.application.views.chat;

import com.firstgame.application.service.WebSocketsManager;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class ChatComponent extends VerticalLayout {
    private Div chatArea = new Div();
    private TextField textField = new TextField();
    private Button sendBtn = new Button("Send");

    // WebSocket-Manager erstellen und einrichten
//    WebSocketsManager webSocketManager = new WebSocketsManager(chatArea.getElement());
    WebSocketsManager chatWebSocketManager = new WebSocketsManager(chatArea.getElement());

//    Gestaltung
    public ChatComponent() {

        chatArea.setId("chatArea");
        chatArea.addClassName("chat-area");

        textField.addClassName("input-text-field");

        sendBtn.addClassName("button-send");
        sendBtn.addClickListener(event -> sendMessage());
        sendBtn.addClickShortcut(Key.ENTER);

        add(chatArea, textField, sendBtn);

//        WebSocket-Chatverbindung starten
        chatWebSocketManager.setupWebSocketChat();
//        webSocketManager.setupWebSocketChat("chatArea");
    }

    public void sendMessage() {
        String message = textField.getValue();
        if(!message.isEmpty()) {
            getElement().executeJs("window.ChatWebSocket.sendMessage(JSON.stringify({type: 'chat', text: $0}))", message);
            textField.clear();
        }
    }
}
