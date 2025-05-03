package com.example.othello;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameAgainstComputerActivity extends AppCompatActivity {
    private GameView gameView;
    private TextView statusText;
    private Button resetButton;

    private boolean isPlayerTurn = true;  // Pour savoir si c'est le tour du joueur ou de l'ordinateur
    private GameLogic gameLogic;  // Classe logique pour gérer les règles du jeu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_against_computer);

        gameView = findViewById(R.id.gameView);
        statusText = findViewById(R.id.statusText);
        resetButton = findViewById(R.id.resetButton);

        gameLogic = new GameLogic();  // Initialisation de la logique du jeu

        // Affichage du statut initial
        statusText.setText("Au tour du joueur");

        // Gestion du bouton de réinitialisation
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        // Initialisation du jeu
        gameView.setGameLogic(gameLogic);

        // Quand le joueur clique sur un pion pour jouer
        gameView.setOnCellClickListener(new GameView.OnCellClickListener() {
            @Override
            public void onCellClick(int row, int col) {
                if (isPlayerTurn) {
                    if (gameLogic.isValidMove(row, col, GameLogic.PLAYER)) {
                        gameLogic.makeMove(row, col, GameLogic.PLAYER);
                        gameView.invalidate();  // Rafraîchit l'affichage du jeu
                        checkGameStatus();
                        isPlayerTurn = false;
                        statusText.setText("Au tour de l'ordinateur");

                        // Après un léger délai, l'ordinateur joue
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);  // Attendre avant de jouer
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            computerTurn();
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }
        });
    }

    private void computerTurn() {
        // L'ordinateur fait son mouvement
        int[] move = gameLogic.getBestMove(GameLogic.COMPUTER);
        if (move != null) {
            gameLogic.makeMove(move[0], move[1], GameLogic.COMPUTER);
            gameView.invalidate();
            checkGameStatus();
            isPlayerTurn = true;
            statusText.setText("Au tour du joueur");
        }
    }

    private void checkGameStatus() {
        if (gameLogic.isGameOver()) {
            // Si la partie est terminée
            int winner = gameLogic.getWinner();
            if (winner == GameLogic.PLAYER) {
                statusText.setText("Le joueur gagne !");
            } else if (winner == GameLogic.COMPUTER) {
                statusText.setText("L'ordinateur gagne !");
            } else {
                statusText.setText("Match nul !");
            }
        }
    }

    private void resetGame() {
        gameLogic.resetGame();
        gameView.invalidate();
        statusText.setText("Au tour du joueur");
        isPlayerTurn = true;
    }
}