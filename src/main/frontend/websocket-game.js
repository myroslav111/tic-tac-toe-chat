window.TicTacToeWebSocket = {
  socket: null,

  connect: function () {
    let socketUrl = (window.location.protocol === 'https:' ? 'wss://' : 'ws://') + window.location.host + '/chat';
    this.socket = new WebSocket(socketUrl);

    this.socket.onopen = () => {
      console.log("WebSocket verbunden");
      this.socket.send(JSON.stringify({ type: 'state_request' }));
    };

    this.socket.onmessage = (event) => {
      let data = JSON.parse(event.data);
      console.log("WebSocket Nachricht erhalten:", data);

      if (data.type === 'state_response') {
        window.currentPlayer = data.currentPlayer || 'X';
        console.log("Empfangener Spieler:", window.currentPlayer);
      }

      if (data.type === 'game') {
        window.updateGame(data.row, data.col, data.player, data.nextPlayer, data.winnerRow, data.winnerCol);
        if (data.winnerRow !== null) {
          let resetButton = document.getElementById("resetButton");
          if (resetButton) {
            resetButton.disabled = false;
          }
        }
      }

      if (data.type === 'reset') {
        console.log("Reset wurde empfangen");
        window.resetGame();
      }
    };
  },

  sendMessage: function (msg) {
    if (this.socket) {
      this.socket.send(JSON.stringify(msg));
    }
  }
};

window.getCurrentPlayer = function () {
  return window.currentPlayer || 'X';
};

window.updateGame = function (row, col, player, nextPlayer, winnerRow, winnerCol) {
  let gameB = document.getElementById("gameBoard");
  let buttonArr = gameB.querySelectorAll("vaadin-button[id^=btn-]");
  let btn = document.getElementById(`btn-${row}-${col}`);

  if (btn && !btn.innerText) {
    btn.innerText = player;
  }

  if (winnerRow !== null) {
    let btnWin = document.getElementById(`btn-${winnerRow}-${winnerCol}`);
    if (btnWin) {
      btnWin.style.backgroundColor = "#90EE90";
    }

    buttonArr.forEach(button => {
      button.disabled = true;
      button.style.color = "black";
    });
  }

  window.currentPlayer = nextPlayer;
  console.log("Winner:", winnerRow, winnerCol);
};

window.resetGame = function () {
  let gameB = document.getElementById("gameBoard");
  let buttonArr = gameB.querySelectorAll("vaadin-button[id^=btn-]");
  let resetButton = document.getElementById("resetButton");

  buttonArr.forEach(btn => {
    btn.innerText = "";
    btn.style.backgroundColor = "";
    btn.disabled = false;
    btn.style.color = "hsl(22, 100%, 42%)";
  });

  if (resetButton) {
    resetButton.disabled = true;
    console.log("Reset-Button:", resetButton.tagName);
  }
};


