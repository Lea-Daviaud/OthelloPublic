package com.example.othello;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameAgainstComputerActivity extends AppCompatActivity {
    private GameView gameView;
    private TextView statusText;
    private Button resetButton;

    private boolean isPlayerTurn = true;  // Pour savoir si c'est le tour du joueur ou de l'ordinateur
    private GameLogic gameLogic;  // Classe logique pour gérer les règles du jeu
    public int[] getBestMove(int playerType) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameLogic.isValidMove(i, j, new Player(playerType))) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
    private void computerTurn() {
        int[] move = gameLogic.getBestMove(GameLogic.COMPUTER);
        if (move != null) {
            gameLogic.makeMove(move[0], move[1], GameLogic.COMPUTER);
            gameView.invalidate();
            checkGameStatus();
            isPlayerTurn = true;
            Log.d("Game", "Tour de l'ordinateur : " + move[0] + ", " + move[1]);
            statusText.setText("Au tour du joueur");
        } else {
            // Aucun coup possible pour l'ordinateur
            if (gameLogic.hasValidMove(GameLogic.PLAYER)) {
                isPlayerTurn = true;
                statusText.setText("Aucun coup possible pour l'ordinateur. À vous de jouer !");
            } else {
                // Aucun coup possible pour les deux → fin du jeu
                checkGameStatus();
            }
        }
    }
GameBoard gameBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_against_computer);

        gameView = findViewById(R.id.gameView);
        statusText = findViewById(R.id.statusText);
        resetButton = findViewById(R.id.resetButton);

        gameBoard = new GameBoard(); // ou récupéré autrement
        gameLogic = new GameLogic(gameBoard);
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
                    // Créer un objet Player pour le joueur humain
                    int playerColor = GameLogic.PLAYER;
                    if (playerColor != Color.BLACK && playerColor != Color.WHITE) {
                        Log.e("Player Color", "Couleur invalide: " + playerColor);
                        return;  // Ne pas créer un joueur si la couleur est invalide
                    }

                    // Créer un objet Player pour le joueur humain
                    Player player = new Player(playerColor);
                    // Vérifier si le mouvement est valide
                    if (gameLogic.isValidMove(row, col, player)) {
                        Log.d("Game", "Coup valide : " + row + ", " + col);
                        gameLogic.makeMove(row, col, playerColor);
                        Log.d("Game", "Coup joué : " + row + ", " + col);
                        gameView.invalidate();  // Rafraîchit l'affichage du jeu
                        checkGameStatus();
                        isPlayerTurn = false;
                        Log.d("Game", "Tour passé à l'ordinateur");
                        statusText.setText("Au tour de l'ordinateur");

                        // Après un léger délai, l'ordinateur joue
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);  // Attente d'un demi-seconde avant que l'ordinateur ne joue
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            computerTurn();  // Tour de l'ordinateur
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        Log.d("Game", "Coup invalide : " + row + ", " + col);
                    }
                }
            }

        });
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
