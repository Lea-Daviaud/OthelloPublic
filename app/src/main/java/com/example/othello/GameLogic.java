package com.example.othello;

public class GameLogic {

    public static final int EMPTY = 0;
    public static final int PLAYER = 1;
    public static final int COMPUTER = 2;

    private int[][] board;  // Plateau de jeu 8x8
    private boolean gameOver = false;

    public GameLogic() {
        board = new int[8][8];
        resetGame();
    }

    public void resetGame() {
        // Initialiser le plateau avec les valeurs de départ
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = EMPTY;
            }
        }
        board[3][3] = PLAYER;
        board[4][4] = PLAYER;
        board[3][4] = COMPUTER;
        board[4][3] = COMPUTER;
        gameOver = false;
    }

    public boolean isValidMove(int row, int col, int player) {
        // Implémente la logique pour vérifier si un mouvement est valide
        // Exemple simplifié (à étendre pour Othello complet)
        return board[row][col] == EMPTY;
    }

    public void makeMove(int row, int col, int player) {
        board[row][col] = player;
    }

    public boolean isGameOver() {
        // Vérifie si la partie est terminée
        return gameOver;
    }

    public int getWinner() {
        // Détermine le gagnant (exemple simplifié)
        int playerScore = 0, computerScore = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == PLAYER) playerScore++;
                if (board[i][j] == COMPUTER) computerScore++;
            }
        }
        if (playerScore > computerScore) {
            return PLAYER;
        } else if (computerScore > playerScore) {
            return COMPUTER;
        } else {
            return 0;  // Match nul
        }
    }

    public int[] getBestMove(int player) {
        // Pour simplifier, on retourne un mouvement aléatoire pour l'ordinateur
        return new int[]{(int) (Math.random() * 8), (int) (Math.random() * 8)};
    }
}
