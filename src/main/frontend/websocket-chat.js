window.ChatWebSocket = {
  socket: null,
  messageContainer: null,

  connect: function () {
    let socketUrl = (window.location.protocol === "https:" ? "wss://" : "ws://") + window.location.host + "/chat";
    this.socket = new WebSocket(socketUrl);
    this.messageContainer = document.getElementById("chatArea");

    this.socket.onmessage = (event) => {
      let data = JSON.parse(event.data);
      if (data.type === "chat") {
        this.displayMessage(data.text);
      }
    };
  },

  displayMessage: function (message) {
    if (!this.messageContainer) return;

    const msgDiv = document.createElement("div");
    msgDiv.textContent = message;
    msgDiv.style.wordWrap = "break-word";
    msgDiv.style.overflowWrap = "break-word";
    msgDiv.style.whiteSpace = "pre-wrap";
    msgDiv.style.maxWidth = "100%";
    msgDiv.style.padding = "5px";
    msgDiv.style.borderRadius = "5px";
    msgDiv.style.backgroundColor = "#e0e0e0";
    msgDiv.style.marginBottom = "5px";

    this.messageContainer.appendChild(msgDiv);
    this.messageContainer.scrollTop = this.messageContainer.scrollHeight; // Automatisch nach unten scrollen
  },

  sendMessage: function (msg) {
    if (this.socket) {
      this.socket.send(msg);
    }
  }
};

