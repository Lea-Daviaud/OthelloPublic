package com.example.othello;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {

    public static final int EMPTY = 0;
    public static final int PLAYER = Color.BLACK;
    public static final int COMPUTER = Color.WHITE;
    private GameBoard gameBoard;
    private int[][] board;  // Plateau de jeu 8x8
    private boolean gameOver = false;

    public GameLogic(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.board = new int[8][8];
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
    public boolean hasValidMove(int playerType) {
        Player player = new Player(playerType);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(j, i, player)) return true;
            }
        }
        return false;
    }

    public boolean isValidMove(int x, int y, Player player) {
        // Vérifie si la cellule est vide
        if (board[y][x] != EMPTY) {
            return false;
        }

        int opponent = (player.getColor() == PLAYER) ? COMPUTER : PLAYER;

        // Vérifie si le mouvement capture des pions adverses
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];
            int i = x + dx;
            int j = y + dy;
            boolean foundOpponent = false;


            while (i >= 0 && i < 8 && j >= 0 && j < 8) {
                int cell = board[j][i];
                if (cell == opponent) {
                    foundOpponent = true;
                } else if (cell == player.getColor()) {
                    if (foundOpponent) return true;
                    break;
                } else {
                    break;
                }
                i += dx;
                j += dy;
            }
        }

        return false;  // Aucune capture n'a été trouvée, mouvement invalide
    }



    public void makeMove(int row, int col, int player) {
        board[row][col] = player;
        gameBoard.cells[row][col].player = new Player(player); // Ajoute ce pion au plateau visuel

        int opponent = (player == PLAYER) ? COMPUTER : PLAYER;

        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},          {0, 1},
                {1, -1}, {1, 0},  {1, 1}
        };

        for (int[] dir : directions) {
            int dx = dir[0], dy = dir[1];
            int x = col + dx, y = row + dy;

            List<int[]> toFlip = new ArrayList<>();

            while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                if (board[y][x] == opponent) {
                    toFlip.add(new int[]{y, x});
                } else if (board[y][x] == player) {
                    for (int[] pos : toFlip) {
                        board[pos[0]][pos[1]] = player;
                        gameBoard.cells[pos[0]][pos[1]].player = new Player(player); // Retourne les pions visuellement
                    }
                    break;
                } else {
                    break;
                }
                x += dx;
                y += dy;
            }
        }
    }


    public boolean isGameOver() {
        return !hasValidMove(PLAYER) && !hasValidMove(COMPUTER);
    }

    public int getWinner() {
        int playerScore = 0, computerScore = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == PLAYER) playerScore++;
                if (board[i][j] == COMPUTER) computerScore++;
            }
        }
        if (playerScore > computerScore) return PLAYER;
        if (computerScore > playerScore) return COMPUTER;
        return 0;
    }

    public int[] getBestMove(int player) {
        List<int[]> validMoves = new ArrayList<>();
        Player p = new Player(player);

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (isValidMove(x, y, p)) {
                    validMoves.add(new int[]{y, x});
                }
            }
        }

        if (validMoves.isEmpty()) return null;

        // Choisir un coup aléatoire parmi les coups valides
        return validMoves.get((int) (Math.random() * validMoves.size()));
    }
    public int[][] getBoard() {
        return board;
    }
}
