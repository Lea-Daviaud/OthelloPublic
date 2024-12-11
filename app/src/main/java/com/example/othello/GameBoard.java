package com.example.othello;


import android.graphics.Color;

/**
 * This class represent the grid. (of type GameCell).
 */
public class GameBoard {

    /**
     * This class represent one cell and it's informations.
     */
    public static class GameCell {

        public Player player;

        public GameCell(Player player) {
            this.player = player;

        }
    }

    public GameLevel level;

    public int currentCellX = -1;
    public int currentCellY = -1;


    public GameCell[][] cells;

    /**
     * The class constructor
     *
     * @param level The associated level.
     * @param cells The states for each cells of the grid.
     */
    private GameBoard(GameLevel level, GameCell[][] cells) {
        this.level = level;
        this.cells = cells;
    }

    /**
     * Return the currently selected value. A cell must be selected, otherwise 0 is returned.
     */
    public Player getSelectedValue() {
        // We need to know the current cell
        if (this.currentCellX == -1) return new Player();
        if (this.currentCellY == -1) return new Player();

        GameCell currentCell = this.cells[this.currentCellY][this.currentCellX];
        return currentCell.player;
    }

    /**
     * This method change the state of the selected cell for this grid.
     * If no cell is selected, the method do nothing.
     * We cannot change the state af an initial state.
     *
     * @param value The value to insert in the selected cell
     * @return
     */
    public void pushValue(Player value) {
        // We need to know the current cell
        if (this.currentCellX == -1) return;
        if (this.currentCellY == -1) return;

        GameCell currentCell = this.cells[this.currentCellY][this.currentCellX];
        // We cannot update an initial cell

        if (currentCell.player == new Player()) {
            // Modifier la valeur supposée
            currentCell.player = value;

        }
    }

    /**
     * A factory method that produce an initial grid to solve.
     *
     * @param level Just the medium level is actually supported
     * @return A new grid to solve.
     */
    public static GameBoard getGameBoard(GameLevel level) {

        if (level != GameLevel.MEDIUM) throw new RuntimeException("Not actually implemented");

        // TODO add code for generate differents Grid for each level

        return new GameBoard(level, new GameCell[][]{
                {new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player())},
                {new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player())},
                {new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player())},

                {new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(GameView.joueur1), new GameCell(GameView.joueur2), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player())},
                {new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(GameView.joueur2), new GameCell(GameView.joueur1), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player())},
                {new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player())},

                {new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player())},
                {new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player()), new GameCell(new Player()),
                        new GameCell(new Player()), new GameCell(new Player())}
        });
    }

}