package com.example.othello;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class GameView extends View implements GestureDetector.OnGestureListener {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final GameBoard gameBoard = GameBoard.getGameBoard(GameLevel.MEDIUM);
    private float gridWidth;
    private GameLogic gameLogic;
    private float gridSeparatorSize;
    private float cellWidth;
    private GestureDetector gestureDetector;

    // Static players for now
    public static Player joueur1 = new Player("Léa", Color.WHITE);
    public static Player joueur2 = new Player("Aymeric", Color.BLACK);
    private Player currentPlayer = joueur1; // Start with joueur1
    private Player adversaire = joueur2;

    // TextView to show turn information (assumes that MainActivity provides it)
    private TextView textViewdeux = MainActivity.textView;

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
        gridSeparatorSize = (w / 9f) / 20f;
        gridWidth = w; // Square grid
        cellWidth = gridWidth / 8f; // 8x8 grid
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(cellWidth * 0.7f);

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int backgroundColor = Color.rgb(144, 238, 144);

                // Draw the cell background
                paint.setColor(backgroundColor);
                canvas.drawRect(x * cellWidth, y * cellWidth, (x + 1) * cellWidth, (y + 1) * cellWidth, paint);

                if (gameBoard.cells[y][x].player != null) {
                    // Draw the player disc
                    paint.setColor(gameBoard.cells[y][x].player.getColor());
                    canvas.drawArc(x * cellWidth, y * cellWidth, (x + 1) * cellWidth, (y + 1) * cellWidth, 0, 360, true, paint);
                }
            }
        }

        // Draw the grid lines
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

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (e.getY() < gridWidth) {
            int cellX = (int) (e.getX() / cellWidth);
            int cellY = (int) (e.getY() / cellWidth);

            // Check if the clicked cell is empty
            if (gameBoard.cells[cellY][cellX].player == null) {
                // Try to place a disc for the current player
                if (isValidMove(cellX, cellY, currentPlayer)) {
                    placeDisc(cellX, cellY, currentPlayer);
                    togglePlayer();
                    updateTurnText();
                    postInvalidate(); // Redraw the view
                }
            }
            return true;
        }
        return false;
    }

    private boolean isValidMove(int cellX, int cellY, Player player) {
        // Validate if the move is legal
        return gameBoard.isValidMove(cellX, cellY, player);
    }

    private void placeDisc(int cellX, int cellY, Player player) {
        gameBoard.cells[cellY][cellX].player = player;
        // Flip opponent's discs if necessary
        gameBoard.flipDiscs(cellX, cellY, player);
    }

    private void togglePlayer() {
        // Toggle the current player and opponent
        if (currentPlayer == joueur1) {
            currentPlayer = joueur2;
            adversaire = joueur1;
        } else {
            currentPlayer = joueur1;
            adversaire = joueur2;
        }
    }

    private void updateTurnText() {
        textViewdeux.setText("Au tour de: " + currentPlayer.getNom());
    }

    // Unused gesture methods
    @Override
    public void onLongPress(MotionEvent e) {}

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }
}
