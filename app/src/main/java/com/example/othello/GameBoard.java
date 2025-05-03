package com.example.othello;

import android.graphics.Color;

/**
 * Cette classe représente le plateau de jeu (de type GameCell).
 */
public class GameBoard {

    /**
     * Cette classe représente une cellule et ses informations.
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
     * Constructeur privé de la classe GameBoard.
     *
     * @param level Le niveau associé.
     * @param cells L'état de chaque cellule du plateau.
     */
    private GameBoard(GameLevel level, GameCell[][] cells) {
        this.level = level;
        this.cells = cells;
    }

    /**
     * Méthode pour obtenir la valeur actuellement sélectionnée.
     * Si aucune cellule n'est sélectionnée, retourne un joueur vide.
     */
    public Player getSelectedValue() {
        // Vérifie si une cellule est sélectionnée
        if (this.currentCellX == -1 || this.currentCellY == -1) {
            return new Player(); // Retourne un joueur vide si aucune cellule n'est sélectionnée
        }

        GameCell currentCell = this.cells[this.currentCellY][this.currentCellX];
        return currentCell.player;
    }

    /**
     * Méthode pour changer l'état de la cellule sélectionnée.
     * Si aucune cellule n'est sélectionnée, la méthode ne fait rien.
     * On ne peut pas changer l'état d'une cellule initiale.
     *
     * @param value La valeur à insérer dans la cellule sélectionnée.
     */
    public void pushValue(Player value) {
        if (this.currentCellX == -1 || this.currentCellY == -1) {
            return; // Si aucune cellule n'est sélectionnée, ne rien faire
        }

        GameCell currentCell = this.cells[this.currentCellY][this.currentCellX];
        // Si la cellule est vide (non initialisée), on peut mettre la valeur
        if (currentCell.player == new Player()) {
            currentCell.player = value;
        }
    }

    /**
     * Méthode de fabrique pour créer un plateau de jeu initial.
     * Actuellement, seul le niveau MEDIUM est supporté.
     *
     * @param level Le niveau de difficulté (actuellement seul le niveau MEDIUM est supporté).
     * @return Un nouveau plateau de jeu à résoudre.
     */
    public static GameBoard getGameBoard(GameLevel level) {
        if (level != GameLevel.MEDIUM) throw new RuntimeException("Niveau non implémenté");

        // Initialisation du plateau avec des cases vides
        GameCell[][] cells = new GameCell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new GameCell(new Player()); // Remplir toutes les cases avec un joueur vide
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
     * Méthode pour inverser les pièces autour de la cellule sélectionnée, après un coup valide.
     */
    public void flipPieces(int x, int y, Player currentPlayer, Player adversaire) {
        // Logique d'inversion des pièces, selon les règles du jeu Othello.
        // Cette méthode pourrait être utilisée après un coup validé pour inverser les pièces autour.
        // Exemple de gestion pour la diagonale, les lignes et les colonnes :

        // (On pourrait ajouter des boucles pour vérifier chaque direction autour de la cellule)
        // Cette partie nécessite une logique spécifique, dépendant des règles d'othello.
    }
}
