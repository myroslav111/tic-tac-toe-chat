package com.firstgame.application.service;

import com.vaadin.flow.dom.Element;


public class WebSocketsManager {
    private final Element domElement;

    public WebSocketsManager(Element domElement) {
        this.domElement = domElement;
    }

    public void setupWebSocketGame() {
        domElement.executeJs(
                "let socketUrl = (window.location.protocol === 'https:' ? 'wss://' : 'ws://') + window.location.host + '/chat';" +
                        "var socket = new WebSocket(socketUrl);" +

                        "socket.onopen = function() { " +
                        "  socket.send(JSON.stringify({type: 'state_request'})); " +
                        "};" +

                        "socket.onmessage = function(event) {" +
                        "  let data = JSON.parse(event.data);" +
                        "  console.log('WebSocket-Nachricht erhalten:', data);" +

                        "  if (data.type === 'state_response') { " +
                        "    window.currentPlayer = data.currentPlayer || 'X'; " +
                        "    console.log('Empfangener Spieler:', window.currentPlayer);" +
                        "  }" +

                        "  if (data.type === 'game') {" +
                        "    window.updateGame(data.row, data.col, data.player, data.nextPlayer);" +
                        "  }" +
                        "};" +

                        "if (!window.currentPlayer) {" +
                        "    window.currentPlayer = 'X';" +
                        "}" +

                        "window.sendMessage = function(msg) { socket.send(msg); };" +
                        "window.getCurrentPlayer = function() { return window.currentPlayer || 'X'; };" +

                        "window.updateGame = function(row, col, player, nextPlayer) {" +
                        "  let btn = document.getElementById('btn-' + row + '-' + col);" +
                        "  if (btn && !btn.innerText) { btn.innerText = player; }" +
                        "  window.currentPlayer = nextPlayer;" +
                        "  console.log('Nächster Spieler:', window.currentPlayer);" +
                        "};",
                domElement
        );

        // Забезпечуємо правильне ініціалізування currentPlayer
        domElement.executeJs("window.getCurrentPlayer();").then(player -> {
            System.out.println("Aktueller Spieler: " + player.asString());
        });
    }

        public void setupWebSocketChat() {
            domElement.executeJs(
                "let socketUrl = (window.location.protocol === 'https:' ? 'wss://' : 'ws://') + window.location.host + '/chat';" +
                        "var socket = new WebSocket(socketUrl);" +
                        "socket.onmessage = function(event) {" +
                        "  let data = JSON.parse(event.data);" +
                        "  if (data.type === 'chat') {" +
                        "    var msgDiv = document.createElement('div');" +
                        "    msgDiv.textContent = data.text;" +
                        "    msgDiv.style.wordWrap = 'break-word';" +
                        "    msgDiv.style.overflowWrap = 'break-word';" +
                        "    msgDiv.style.whiteSpace = 'pre-wrap';" +
                        "    msgDiv.style.maxWidth = '100%';" +
                        "    msgDiv.style.padding = '5px';" +
                        "    msgDiv.style.borderRadius = '5px';" +
                        "    msgDiv.style.backgroundColor = '#e0e0e0';" +
                        "    msgDiv.style.marginBottom = '5px';" +
                        "    $0.appendChild(msgDiv);" +
                        "    $0.scrollTop = $0.scrollHeight;" + // Scrollt automatisch nach unten
                        "  }" +
                        "};" +
                        "window.sendMessage = function(msg) { socket.send(msg); };",
                    domElement
        );
    }
}
