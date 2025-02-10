package com.firstgame.application.views.chat;

import com.firstgame.application.service.WebSocketsManager;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class ChatView extends VerticalLayout {
    private Div chatArea = new Div();
    private TextField textField = new TextField();
    private Button sendBtn = new Button("Send");

    // WebSocket-Manager erstellen und einrichten
//    WebSocketsManager webSocketManager = new WebSocketsManager(chatArea.getElement());
    WebSocketsManager webSocketManager = new WebSocketsManager(chatArea.getElement());

//    Gestaltung
    public ChatView() {
        chatArea.setWidth("100%");
        chatArea.setHeight("300px");
        chatArea.getStyle().set("padding", "10px")
                        .set("border", "1px solid #ccc")
                                .set("background-color", "#f9f9f9")
                                        .set("word-wrap", "break-word")
                                                .set("overflow-wrap", "break-word")
                                                        .set("white-space", "pre-wrap")
                                                                .set("max-width", "100%")
                                                                        .set("overflow-y", "auto")
                                                                                .set("color", "black");


        textField.setWidth("100%");
        textField.getElement().getStyle().set("color", "black");
        sendBtn.getElement().getStyle().set("background-color", "hsl(214, 35%, 21%)");
        sendBtn.addClickListener(event -> sendMessage());

        add(chatArea, textField, sendBtn);

//        WebSocket-Chatverbindung starten
        webSocketManager.setupWebSocketChat();
    }

    public void sendMessage() {
        String message = textField.getValue();
        if(!message.isEmpty()) {
            getElement().executeJs("window.sendMessage(JSON.stringify({type: 'chat', text: $0}))", message);
            textField.clear();
        }
    }
}
