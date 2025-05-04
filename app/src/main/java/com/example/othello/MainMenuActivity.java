package com.example.othello;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    private Button playAgainstComputerButton;
    private Button playOnlineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        playAgainstComputerButton = findViewById(R.id.playAgainstComputer);
        playOnlineButton = findViewById(R.id.playOnline);

        // Jouer contre l'ordinateur
        playAgainstComputerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité de jeu contre l'ordinateur
                Intent intent = new Intent(MainMenuActivity.this, GameAgainstComputerActivity.class);
                startActivity(intent);
            }
        });

        // Jouer contre un autre joueur en ligne
        playOnlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité de jeu en ligne
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);  // Remplacer par l'activité en ligne
                startActivity(intent);
            }
        });
    }
}