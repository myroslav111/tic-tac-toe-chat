package com.firstgame.application.views.tictactoe;

import com.firstgame.application.game.TicTacToeGameProcess;
import com.firstgame.application.service.WebSocketsManager;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TicTacToeBoardView extends VerticalLayout {
    private final Div gameBoard;
    private final Button[][] buttons;
    private final String[][] board = new String[3][3];
    private String currentPlayer; // Startspieler wird aus WebSocket gesetzt
    public Button resetButton;

    public TicTacToeBoardView() {
        gameBoard = new Div();
        buttons = new Button[3][3];
        resetButton = new Button("Reset");
        resetButton.setEnabled(false);
        resetButton.setId("resetButton");

        setupGameBoard();
        add(gameBoard, resetButton);

        // WebSocket-Manager erstellen und einrichten
        WebSocketsManager webSocketManager = new WebSocketsManager(gameBoard.getElement());
        webSocketManager.setupWebSocketGame();
    }

    private void setupGameBoard() {
        gameBoard.setId("gameBoard");
        gameBoard.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(3, 100px)")
                .set("grid-template-rows", "repeat(3, 100px)")
                .set("gap", "5px")
                .set("justify-content", "center");

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
        button.getStyle()
                .set("width", "100%")
                .set("height", "100%")
                .set("font-size", "24px")
                .set("text-align", "center")
                .set("border", "2px solid black")
                .set("border-color", "#f0f0f0")
                .set("cursor", "pointer")
                .set("border-radius", "10px");

        button.addClickListener(e -> handleMove(row, col));
        return button;
    }

    private void handleMove(int row, int col) {
        getElement().executeJs("return window.currentPlayer || 'X';").then(player -> {
            currentPlayer = player.asString();
            System.out.println("Aktueller Spieler: " + currentPlayer);
            System.out.println("board " + board[row][col]);

            if (board[row][col] == null) {
                board[row][col] = currentPlayer;
                buttons[row][col].setText(currentPlayer);

                int[][] winner = TicTacToeGameProcess.checkWinner(board);

                String nextPlayer = currentPlayer.equals("X") ? "O" : "X";
                getElement().executeJs(
                        "window.sendMessage(JSON.stringify({type: 'game', row: $0, col: $1, player: $2, nextPlayer: $3, winnerRow: $4, winnerCol: $5}))",
                        row, col, currentPlayer, nextPlayer, null, null
                );

                if (winner != null) {
                    highlightWinner(winner);
                    Notification.show("Spieler " + currentPlayer + " hat gewonnen!");
                    resetButton.setEnabled(true);
                    resetButton.addClickListener(e -> resetBoard());
                } else {
                    currentPlayer = nextPlayer;
                }
            } else {
                Notification.show("Feld belegt oder du bist nicht an der Reihe!");
            }
        });
    }

    private void highlightWinner(int[][] winner) {
        int winnerRow;
        int winnerCol;
        for (int[] pos : winner) {
            winnerRow = pos[0];
            winnerCol = pos[1];
            buttons[pos[0]][pos[1]].getStyle().set("background-color", "#90EE90");
            getElement().executeJs(
                    "window.sendMessage(JSON.stringify({type: 'game', row: $0, col: $1, player: $2, nextPlayer: $3, winnerRow: $4, winnerCol: $5}))",
                    winnerRow, winnerCol, currentPlayer, null, winnerRow, winnerCol
            );
        }
    }

    private void resetBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                board[row][col] = null;
                buttons[row][col].getStyle().set("background-color", "inherit");
            }
        }
        getElement().executeJs("window.sendMessage(JSON.stringify({type: 'reset'}))");
        resetButton.setEnabled(false);
    }
}
