package com.example.othello;

import android.graphics.Color;

public class Player {
    public static final int EMPTY = 0;
    public static final int BLACK = 1;  // Humain ou joueur 1
    public static final int WHITE = 2;  // Ordinateur ou joueur 2
    private  int color;

    public Player() {
        this.color = EMPTY;
    }
    public Player(int color) {
        if (color != Color.BLACK && color != Color.WHITE) {
            throw new IllegalArgumentException("Couleur invalide pour un joueur");
        }
        this.color = color;
    }

    public int getColor() {
        return color;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Player)) return false;
        Player other = (Player) obj;
        return this.color == other.color;
    }

    public void setColor(int color) {
        if (color != BLACK && color != WHITE && color != EMPTY) {
            throw new IllegalArgumentException("Couleur invalide pour un joueur");
        }
        this.color = color;
    }

    public boolean isEmpty() {
        return this.color == EMPTY;
    }

    public boolean isOpponent(Player other) {
        return other != null && this.color != EMPTY && other.color != this.color;
    }
}