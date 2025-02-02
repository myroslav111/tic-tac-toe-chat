package com.firstgame.application.game;

public class TicTacToeGameProcess {

    public static int[][] checkWinner(String[][] board) {
        // Überprüfung der horizontalen Reihen
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == "") continue;

            if (board[row][0] != null &&
                    board[row][0].equals(board[row][1]) &&
                    board[row][1].equals(board[row][2])) {
                System.out.println("Winner is horizontal");
                return new int[][]{{row, 0}, {row, 1}, {row, 2}};

            }
        }

        for (int col = 0; col < 3; col++) {
            if (board[0][col] == "") continue;

            if (board[0][col] != null &&
                    board[0][col].equals(board[1][col]) &&
                    board[1][col].equals(board[2][col])) {
                System.out.println("Winner is vertical");
                return new int[][]{{0, col}, {1, col}, {2, col}};
            }
        }

        if (board[0][2] != null &&
                board[0][2].equals(board[1][1]) &&
                board[1][1].equals(board[2][0])) {
            System.out.println("Winner is diagonal");
            return new int[][]{{0, 2}, {1, 1}, {2, 0}};
        }

        if (board[0][0] != null &&
                board[0][0].equals(board[1][1]) &&
                board[1][1].equals(board[2][2])) {
            System.out.println("Winner is diagonal");
            return new int[][]{{0, 0}, {1, 1}, {2, 2}};
        }
        return null;
    }

}
