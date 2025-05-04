package com.example.othello;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente le plateau de jeu (de type GameCell).
 */
public class GameBoard {

    public static final int SIZE = 8;

    public GameLevel level;
    public int currentCellX = -1;
    public int currentCellY = -1;
    public GameCell[][] cells;

    /**
     * Constructeur privé de la classe GameBoard.
     */
    private GameBoard(GameLevel level, GameCell[][] cells) {
        this.level = level;
        this.cells = cells;
    }
    public GameBoard() {
        cells = new GameCell[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new GameCell(null);
            }
        }

        // Positionnement initial
        cells[3][3] = new GameCell(new Player(Color.BLACK));
        cells[3][4] = new GameCell(new Player(Color.WHITE));
        cells[4][3] = new GameCell(new Player(Color.WHITE));
        cells[4][4] = new GameCell(new Player(Color.BLACK));
    }
    /**
     * Méthode de fabrique pour créer un plateau de jeu initial.
     * Actuellement, seul le niveau MEDIUM est supporté.
     */
    public static GameBoard getGameBoard(GameLevel level) {
        if (level != GameLevel.MEDIUM) throw new RuntimeException("Niveau non implémenté");

        GameCell[][] cells = new GameCell[SIZE][SIZE];
        // Initialisation avec des cellules vides (player == null)
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new GameCell(null);
            }
        }

        // Placer les pièces initiales pour un plateau standard
        cells[3][3] = new GameCell(GameView.joueur1);
        cells[3][4] = new GameCell(GameView.joueur2);
        cells[4][3] = new GameCell(GameView.joueur2);
        cells[4][4] = new GameCell(GameView.joueur1);

        return new GameBoard(level, cells);
    }

    /**
     * Met à jour le GameBoard à partir du tableau de la logique.
     */
    public void updateFromLogic(int[][] board) {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                int color = board[y][x];
                if (color == Player.BLACK) {
                    cells[y][x].player = new Player(Player.BLACK);
                } else if (color == Player.WHITE) {
                    cells[y][x].player = new Player(Player.WHITE);
                } else {
                    cells[y][x].player = null;
                }
            }
        }
    }

    public Player getSelectedValue() {
        if (currentCellX == -1 || currentCellY == -1) return null;
        return cells[currentCellY][currentCellX].player;
    }

    public void pushValue(Player value) {
        if (currentCellX == -1 || currentCellY == -1) return;
        GameCell cell = cells[currentCellY][currentCellX];
        if (cell.player == null) {
            cell.player = value;
        }
    }

    public void flipDiscs(int x, int y, Player player) {
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},          {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];
            int i = x + dx;
            int j = y + dy;

            List<GameCell> toFlip = new ArrayList<>();

            while (i >= 0 && i < SIZE && j >= 0 && j < SIZE) {
                Player p = cells[j][i].player;
                if (p == null) break;
                if (!p.isOpponent(player)) {

                    if (!toFlip.isEmpty()) {
                        for (GameCell cell : toFlip) {
                            cell.player = player;
                        }
                    }
                    break;
                } else {
                    toFlip.add(cells[j][i]);
                }
                i += dx;
                j += dy;
            }
        }
    }

    public static class GameCell {
        public Player player;

        public GameCell(Player player) {
            this.player = player;
        }
    }
}