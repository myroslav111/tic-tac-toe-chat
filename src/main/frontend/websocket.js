export function setupWebSocketGame(updateGame, resetGame) {
  let socketUrl = (window.location.protocol === 'https:' ? 'wss://' : 'ws://') + window.location.host + '/chat';
  var socket = new WebSocket(socketUrl);

  socket.onopen = function() {
    socket.send(JSON.stringify({ type: 'state_request' }));
  };

  socket.onmessage = function(event) {
    let data = JSON.parse(event.data);
    console.log('WebSocket-Nachricht erhalten:', data);

    if (data.type === 'state_response') {
      window.currentPlayer = data.currentPlayer || 'X';
      console.log('Empfangener Spieler:', window.currentPlayer);
    }

    if (data.type === 'game') {
      updateGame(data.row, data.col, data.player, data.nextPlayer, data.winnerRow, data.winnerCol);
    }

    if (data.type === 'reset') {
      resetGame();
    }
  };

  window.sendMessage = function(msg) {
    socket.send(msg);
  };
}

export function setupWebSocketChat(chatContainer) {
  let socketUrl = (window.location.protocol === 'https:' ? 'wss://' : 'ws://') + window.location.host + '/chat';
  var socket = new WebSocket(socketUrl);

  socket.onmessage = function(event) {
    let data = JSON.parse(event.data);
    if (data.type === 'chat') {
      var msgDiv = document.createElement('div');
      msgDiv.textContent = data.text;
      msgDiv.style.backgroundColor = '#e0e0e0';
      chatContainer.appendChild(msgDiv);
      chatContainer.scrollTop = chatContainer.scrollHeight;
    }
  };

  window.sendChatMessage = function(msg) {
    socket.send(msg);
  };
}
