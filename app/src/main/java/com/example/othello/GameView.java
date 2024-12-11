package com.example.othello;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class GameView extends View implements GestureDetector.OnGestureListener {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final GameBoard gameBoard = GameBoard.getGameBoard(GameLevel.MEDIUM);
    private float gridWidth;
    private float gridSeparatorSize;
    private float cellWidth;
    private GestureDetector gestureDetector;
    TextView textViewdeux = MainActivity.textView;

    public static Player joueur1 = new Player("Léa", Color.WHITE);
    public static Player joueur2 = new Player("Aymeric", Color.BLACK);

    public GameView(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        gestureDetector = new GestureDetector(getContext(), this);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // We compute some sizes
        gridSeparatorSize = (w / 9f) / 20f;
        gridWidth = w;                                  // Size of the grid (it's a square)
        cellWidth = gridWidth / 8f;                     // Size of a cell (it's a square too)

    }

    @Override
    protected void onDraw(Canvas canvas) {
Log.i("lealea", MainActivity.textView+"");
        // --- Draw cells ---

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(cellWidth * 0.7f);

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int backgroundColor = Color.rgb(144, 238, 144);

                // Draw the background for the current cell
                paint.setColor(backgroundColor);
                canvas.drawRect(x * cellWidth,
                        y * cellWidth,
                        (x + 1) * cellWidth,
                        (y + 1) * cellWidth,
                        paint);

                if (gameBoard.cells[y][x].player != new Player()) {

                    // Draw the assumed value for the cell.
                    paint.setColor(gameBoard.cells[y][x].player.getColor());
                    paint.setTextSize(cellWidth * 0.7f);
                    canvas.drawArc(x * cellWidth,
                            y * cellWidth,
                            (x + 1) * cellWidth,
                            (y + 1) * cellWidth,
                            0,
                            360,
                            true,
                            paint);
                }
            }
        }




        // --- Draw the grid lines ---
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(gridSeparatorSize / 2);
        for (int i = 0; i <= 7; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, cellWidth * 8, paint);
            canvas.drawLine(0, i * cellWidth, cellWidth * 8, i * cellWidth, paint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    // Override from OnGestureDectector
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    Player adversaire;
    Player currentPlayer = getCurrentPlayer();


    public Player getCurrentPlayer() {
        if (currentPlayer == joueur2) {
            adversaire = joueur2;
            return joueur1;
        } else {
            adversaire = joueur1;
            return joueur2;
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // --- Check grid cell click ---
        if (e.getY() < gridWidth) {
            Log.i("Lea", "dans if 1");
            int cellX = (int) (e.getX() / cellWidth);
            int cellY = (int) (e.getY() / cellWidth);
            Log.i("Leatrois", "celly: " + cellY + " / cellX :  " + cellX);
            Log.i("Leatrois", adversaire.getColor() + "");
            // Check if the cell is already occupied

            //diagonale droite haut 4
            int diaDH;
            if ((7 - cellX) < 7 - cellY) {
                diaDH = (7 - cellX);
            } else {
                diaDH = (7 - cellY);
            }

            //diagonale bas droite 4
            int diaBD;
            if ((7 - cellX) < (cellY)) {
                diaBD = (7 - cellX);
            } else {
                diaBD = ( cellY);
            }

            //diagonale haut gauche 4
            int diaHG;
            if (cellX < (7-cellY)) {
                diaHG = cellX;
            } else {
                diaHG = 7-cellY;
            }

            //diagonale haut gauche 4 laaaa !
            int diaBG;
            if (cellX < (cellY)) {
                diaBG = cellX;
            } else {
                diaBG = (cellY);
            }
            try {
                if (gameBoard.cells[cellY][cellX].player.getColor() == new Player().getColor()) {
                    for (int o = 0; o < diaDH; o++) {
                        if (//diagonale droite haut
                                ((cellX + 1) < 8 && (cellY + 1) < 8 &&
                                        gameBoard.cells[cellY + 1][cellX + 1].player.getColor() == adversaire.getColor() &&
                                        gameBoard.cells[cellY + 1 + o][cellX + 1 + o].player.getColor() != new Player().getColor()
                                        && gameBoard.cells[cellY + 1 + o][cellX + 1 + o].player.getColor() == currentPlayer.getColor())) {

                            //  Log.i("lea", gameBoard.cells[cellY + 2][cellX]+"");

                            // Change the color of the cell to the current player's color
                            Log.i("Lea", "dans if deux");

                            gameBoard.cells[cellY][cellX].player = currentPlayer;


                            //changer couleur dans l'intervalle
                            for (int a = 0; a <= o; a++) {
                                gameBoard.cells[cellY + 1 + a][cellX + 1 + a].player = currentPlayer;
                            }
                        }
                    }

                    for (int i = 0; i < 7 - cellY; i++) {
                        if (
                            //vertical bas
                                ((cellY + 1) < 8 &&
                                        gameBoard.cells[cellY + 1][cellX].player.getColor() == adversaire.getColor() &&
                                        gameBoard.cells[cellY + 1 + i][cellX].player.getColor() != new Player().getColor()
                                        && gameBoard.cells[cellY + 1 + i][cellX].player.getColor() == currentPlayer.getColor())
                        ) {

                            //  Log.i("lea", gameBoard.cells[cellY + 2][cellX]+"");

                            // Change the color of the cell to the current player's color
                            Log.i("Lea", "dans if deux");

                            gameBoard.cells[cellY][cellX].player = currentPlayer;

                            //changer couleur dans l'intervalle
                            for (int a = 0; a <= i; a++) {
                                gameBoard.cells[cellY + 1 + a][cellX].player = currentPlayer;
                            }
                        }
                    }

                    for (int k = 0; k < 7 - cellX; k++) {
                        if (
                                ((cellX + 1) < 8 &&
                                        gameBoard.cells[cellY][cellX + 1].player.getColor() == adversaire.getColor()
                                        && gameBoard.cells[cellY][cellX + 1 + k].player.getColor() != new Player().getColor() &&
                                        gameBoard.cells[cellY][cellX + 1 + k].player.getColor() == currentPlayer.getColor())
                        ) {

                            //  Log.i("lea", gameBoard.cells[cellY + 2][cellX]+"");

                            // Change the color of the cell to the current player's color
                            Log.i("Lea", "dans if deux");

                            gameBoard.cells[cellY][cellX].player = currentPlayer;
                            //changer couleur dans l'intervalle
                            for (int a = 0; a <= k; a++) {
                                gameBoard.cells[cellY][cellX + 1 + a].player = currentPlayer;

                            }
                        }
                    }

                    for (int m = 0; m < diaBG; m++) {
                        if (
                            //diagonale bas gauche
                                ((cellX - 1) >= 0 && (cellY - 1) >= 0 &&
                                        gameBoard.cells[cellY - 1][cellX - 1].player.getColor() == adversaire.getColor()
                                        && gameBoard.cells[cellY - 1 - m][cellX - 1 - m].player.getColor() != new Player().getColor() &&
                                        gameBoard.cells[cellY - 1 - m][cellX - 1 - m].player.getColor() == currentPlayer.getColor())
                        ) {

                            //  Log.i("lea", gameBoard.cells[cellY + 2][cellX]+"");

                            // Change the color of the cell to the current player's color
                            Log.i("Lea", "dans if deux");

                            gameBoard.cells[cellY][cellX].player = currentPlayer;

                            //changer couleur dans l'intervalle
                            for (int a = 0; a <= m; a++) {
                                gameBoard.cells[cellY - 1 - a][cellX - 1 - a].player = currentPlayer;

                            }
                        }
                    }

                    for (int b = 0; b < cellX; b++) {
                        if (
                                ((cellX - 1) >= 0 &&
                                        gameBoard.cells[cellY][cellX - 1].player.getColor() == adversaire.getColor()
                                        && gameBoard.cells[cellY][cellX - 1 - b].player.getColor() != new Player().getColor() &&
                                        gameBoard.cells[cellY][cellX - 1 - b].player.getColor() == currentPlayer.getColor())
                        ) {

                            //  Log.i("lea", gameBoard.cells[cellY + 2][cellX]+"");

                            // Change the color of the cell to the current player's color
                            Log.i("Lea", "dans if deux");

                            gameBoard.cells[cellY][cellX].player = currentPlayer;

                            //changer couleur dans l'intervalle
                            for (int a = 0; a <= b; a++) {
                                gameBoard.cells[cellY][cellX - 1 - a].player = currentPlayer;
                            }
                        }
                    }


                    for (int g = 0; g < cellY; g++) {
                        if (
                                ((cellY - 1) >= 0 &&
                                        gameBoard.cells[cellY - 1][cellX].player.getColor() == adversaire.getColor()
                                        && gameBoard.cells[cellY - 1 - g][cellX].player.getColor() != new Player().getColor() &&
                                        gameBoard.cells[cellY - 1 - g][cellX].player.getColor() == currentPlayer.getColor())
                        ) {

                            //  Log.i("lea", gameBoard.cells[cellY + 2][cellX]+"");

                            // Change the color of the cell to the current player's color
                            Log.i("Lea", "dans if deux");

                            gameBoard.cells[cellY][cellX].player = currentPlayer;
                            //changer couleur dans l'intervalle
                            for (int a = 0; a <= g; a++) {
                                gameBoard.cells[cellY - 1 - a][cellX].player = currentPlayer;
                            }
                        }
                    }


                    for (int h = 0; h < diaHG; h++) {
                        if (
                            //diagonale gauche haut
                                ((cellX - 1) >= 0 && (cellY + 1) < 8 &&
                                        gameBoard.cells[cellY + 1][cellX - 1].player.getColor() == adversaire.getColor()
                                        && gameBoard.cells[cellY + 1 + h][cellX - 1 - h].player.getColor() != new Player().getColor()
                                        && gameBoard.cells[cellY + 1 + h][cellX - 1 - h].player.getColor() == currentPlayer.getColor())
                        ) {

                            //  Log.i("lea", gameBoard.cells[cellY + 2][cellX]+"");

                            // Change the color of the cell to the current player's color
                            Log.i("Lea", "dans if deux");

                            gameBoard.cells[cellY][cellX].player = currentPlayer;

                            //changer couleur dans l'intervalle
                            for (int a = 0; a <= h; a++) {
                                gameBoard.cells[cellY + 1 + a][cellX - 1 - a].player = currentPlayer;
                            }
                        }


                    }
                    for (int p = 0; p < diaBD; p++) {
                        if (
                            //diagonale droite bas
                                ((cellY - 1) >= 0 && (cellX + 1) < 8 &&
                                        gameBoard.cells[cellY - 1][cellX + 1].player.getColor() == adversaire.getColor()
                                        && gameBoard.cells[cellY - 1 - p][cellX + 1 + p].player.getColor() != new Player().getColor()
                                        && gameBoard.cells[cellY - 1 - p][cellX + 1 + p].player.getColor() == currentPlayer.getColor())
                        ) {

                            //  Log.i("lea", gameBoard.cells[cellY + 2][cellX]+"");

                            // Change the color of the cell to the current player's color
                            Log.i("Lea", "dans if deux");

                            gameBoard.cells[cellY][cellX].player = currentPlayer;

                            //changer couleur dans l'intervalle
                            for (int a = 0; a <= p; a++) {
                                gameBoard.cells[cellY - 1 - a][cellX + 1 + a].player = currentPlayer;
                            }
                        }
                    }
                    currentPlayer = getCurrentPlayer();
                    Log.i("icic", "onSingleTapUp: "+MainActivity.textView.getText());
                    MainActivity.textView.setText("Au tour de : " + currentPlayer.getNom()+"");
                }

                gameBoard.currentCellX = cellX;
                gameBoard.currentCellY = cellY;
                postInvalidate(); // Redraw the view
                return true;
            } catch (Resources.NotFoundException i) {
                Log.i("ERROR", "Value Not fetched");
            }
        }

        return true;
    }


    @Override
    public boolean onScroll(MotionEvent
                                    e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent
                                    e) {

    }

    @Override
    public boolean onFling(MotionEvent
                                   e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

}

