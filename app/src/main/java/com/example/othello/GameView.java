package com.example.othello;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class GameView extends View implements GestureDetector.OnGestureListener {
    private static final int NUM_ROWS = 8;
    private static final int NUM_COLS = 8;
    // Static players for now
    public static Player joueur1 = new Player( Color.WHITE);
    public static Player joueur2 = new Player( Color.BLACK);
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final GameBoard gameBoard = GameBoard.getGameBoard(GameLevel.MEDIUM);
    private float gridWidth;
    private GameLogic gameLogic;
    private float gridSeparatorSize;
    private float cellWidth;
    private GestureDetector gestureDetector;
    private Player currentPlayer = joueur1; // Start with joueur1
    private Player adversaire = joueur2;
    // TextView to show turn information (assumes that MainActivity provides it)
    private TextView textViewdeux = MainActivity.textView;
    private int cellSize; // Taille d'une cellule (en pixels)
    private OnCellClickListener onCellClickListener;

    public GameView(Context context) {
        super(context);
        this.init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    private void init() {
        gestureDetector = new GestureDetector(getContext(), this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("GameView", "onDraw appelé");
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(cellWidth * 0.7f);


        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int backgroundColor = Color.rgb(144, 238, 144);

                // Draw the cell background
                paint.setColor(backgroundColor);
                canvas.drawRect(x * cellWidth, y * cellWidth, (x + 1) * cellWidth, (y + 1) * cellWidth, paint);

                if (gameBoard.cells[y][x].player != null) {
                    Log.d("GameView", "Pion trouvé à [" + y + ", " + x + "], couleur: " + gameBoard.cells[y][x].player.getColor());
                    // Draw the player disc
                    paint.setColor(gameBoard.cells[y][x].player.getColor());
                    float radius = cellWidth * 0.4f;
                    canvas.drawCircle((x + 0.5f) * cellWidth, (y + 0.5f) * cellWidth, radius, paint);                }
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

    // Appelé lorsque la taille de la vue change (ex: à l'affichage initial)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int boardSize = Math.min(w, h); // on garde un plateau carré
        cellSize = boardSize / 8;       // 8x8 cellules pour Othello
        cellWidth = cellSize;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            int col = (int) (x / cellSize);
            int row = (int) (y / cellSize);

            if (onCellClickListener != null) {
                onCellClickListener.onCellClick(row, col);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public void setOnCellClickListener(OnCellClickListener listener) {
        this.onCellClickListener = listener;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (e.getY() < getHeight()) {
            int cellX = (int) (e.getX() / cellWidth);
            int cellY = (int) (e.getY() / cellWidth);

            Log.d("GameView", "Cell selected: " + cellX + "," + cellY);

            // Check if the clicked cell is empty
            if (gameBoard.cells[cellY][cellX].player == null) {
                // Try to place a disc for the current player
                if (gameLogic != null && gameLogic.isValidMove(cellX, cellY, currentPlayer)) {
                    Log.d("GameView", "Valid move for player: " + currentPlayer.getColor());
                    placeDisc(cellX, cellY, currentPlayer);
                    togglePlayer();
                    updateTurnText();
                    postInvalidate(); // Redraw the view
                } else {
                    Log.d("GameView", "Invalid move for player: " + currentPlayer.getColor());
                }
            }
            return true;
        }
        return false;
    }

    private void placeDisc(int cellX, int cellY, Player player) {
        gameBoard.cells[cellY][cellX].player = player;
        Log.d("GameView", "Disc placed at: " + cellX + "," + cellY);
        gameBoard.flipDiscs(cellX, cellY, player);
        postInvalidate();
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

    private String getColorName(Player player) {
        return player.getColor() == Color.BLACK ? "Noir" : "Blanc";
    }

    private void updateTurnText() {
        textViewdeux.setText("Au tour de: " + getColorName(currentPlayer));
    }


    // Unused gesture methods
    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    public interface OnCellClickListener {
        void onCellClick(int row, int col);
    }
}
