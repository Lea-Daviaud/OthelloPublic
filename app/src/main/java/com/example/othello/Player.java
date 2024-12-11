package com.example.othello;

import static android.graphics.Color.WHITE;

import android.graphics.Color;

public class Player {
    String nom;
    int color;

    public Player() {
        this.nom = "vide";
        this.color = 0;
    }

    public Player(String nom, int color) {
        this.nom = nom;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setColor(int color) {
        this.color = color;
    }
}


