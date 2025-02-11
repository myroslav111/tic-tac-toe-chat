package com.firstgame.application.views.tictactoe;

import com.firstgame.application.game.TicTacToeGameProcess;
import com.firstgame.application.service.WebSocketsManager;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TicTacToeBoardComponent extends VerticalLayout {
//    private final Div gameBoard;
    private final Div gameBoard = new Div();
    private final Button[][] buttons;
//    Speichert die Spielzustände
    private final String[][] board = new String[3][3];
    public String currentPlayer; // Startspieler wird aus WebSocket gesetzt
    public Button resetButton;
    public short move = 1;
    WebSocketsManager gameWebSocketManager = new WebSocketsManager(gameBoard.getElement());
//    UI
    public TicTacToeBoardComponent() {
//        gameBoard = new Div();
//        3×3-Array für die 9 Spielknöpfe
        buttons = new Button[3][3];
        resetButton = new Button("Reset");
        resetButton.setEnabled(false);
        resetButton.setId("resetButton");

        setupGameBoard();
        add(gameBoard, resetButton);

        // WebSocket-Manager erstellen und einrichten

//        Startet die WebSocket-Kommunikation für Multiplayer-Funktionalität
        gameWebSocketManager.setupWebSocketGame();
    }

//    Spielfeld-Setup
    private void setupGameBoard() {
        gameBoard.setId("gameBoard");
        gameBoard.addClassName("game-board");

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = createButton(row, col);
                gameBoard.add(buttons[row][col]);
            }
        }
    }

    private Button createButton(int row, int col) {
        Button button = new Button("");
        button.setId("btn-" + row + "-" + col);
        //    Gestaltung
        button.addClassName("button-of-board");

        button.addClickListener(e -> handleMove(row, col));
        return button;
    }

    private void handleMove(int row, int col) {
        System.out.println(move);
        move ++;
//        Liest den aktuellen Spieler über JavaScript
        getElement().executeJs("return window.currentPlayer || 'X';").then(player -> {
            currentPlayer = player.asString();
            System.out.println("Aktueller Spieler: " + currentPlayer);
            System.out.println("board " + board[row][col]);

            if (board[row][col] == null) {
                board[row][col] = currentPlayer;
                buttons[row][col].setText(currentPlayer);

                int[][] winner = TicTacToeGameProcess.checkWinner(board);
                System.out.println("currentPlayer" + currentPlayer);

                String nextPlayer = currentPlayer.equals("X") ? "O" : "X";

                System.out.println("nextPlayer" + nextPlayer);

//                Sendet den Spielzug an den Server, damit der andere Spieler die Aktualisierung erhält.
                getElement().executeJs(
                        "window.sendMessage(JSON.stringify({type: 'game', row: $0, col: $1, player: $2, nextPlayer: $3, winnerRow: $4, winnerCol: $5}))",
                        row, col, currentPlayer, nextPlayer, null, null
                );
//                getElement().executeJs(
//                        "window.TicTacToeWebSocket.sendMessage(JSON.stringify({type: 'game', row: $0, col: $1, player: $2, nextPlayer: $3, winnerRow: $4, winnerCol: $5}))",
//                        row, col, currentPlayer, nextPlayer, null, null
//                );

                if (move == 10) {
                    resetButton.setEnabled(true);
                    resetButton.addClickListener(e -> resetBoard());
                }

                if (winner != null) {
                    highlightWinner(winner);
                    Notification.show("Spieler " + currentPlayer + " hat gewonnen!");
                    resetButton.setEnabled(true);
                    resetButton.addClickListener(e -> resetBoard());

                } else {
                    currentPlayer = nextPlayer;
                    System.out.println("currentPlayer" + currentPlayer);
                }
            } else {
                Notification.show("Feld belegt oder du bist nicht an der Reihe!");
            }
        });
    }

//    Gewinner markieren
    private void highlightWinner(int[][] winner) {
        int winnerRow;
        int winnerCol;
        for (int[] pos : winner) {
            winnerRow = pos[0];
            winnerCol = pos[1];
            buttons[pos[0]][pos[1]].addClassName("button-highlight-winner");
            getElement().executeJs(
                    "window.sendMessage(JSON.stringify({type: 'game', row: $0, col: $1, player: $2, nextPlayer: $3, winnerRow: $4, winnerCol: $5}))",
                    winnerRow, winnerCol, currentPlayer, null, winnerRow, winnerCol
            );
//            getElement().executeJs(
//                    "window.TicTacToeWebSocket.sendMessage(JSON.stringify({type: 'game', row: $0, col: $1, player: $2, nextPlayer: $3, winnerRow: $4, winnerCol: $5}))",
//                    winnerRow, winnerCol, currentPlayer, null, winnerRow, winnerCol
//            );
        }
    }

    private void resetBoard() {
//        Setzt alle Felder auf leer.
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                board[row][col] = null;
                buttons[row][col].addClassName("button-highlight-winner-remove");
            }
        }
//        Sendet ein Reset-Signal an WebSockets.
        getElement().executeJs("window.sendMessage(JSON.stringify({type: 'reset'}))");
//        getElement().executeJs("window.TicTacToeWebSocket.sendMessage(JSON.stringify({type: 'reset'}))");
        resetButton.setEnabled(false);
        System.out.println(move);
        move = 1;
    }
}
