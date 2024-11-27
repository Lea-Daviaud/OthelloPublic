package com.example.othello;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class GameView extends View implements GestureDetector.OnGestureListener {
    private Paint paint =new Paint ( Paint.ANTI_ALIAS_FLAG );
    private GameBoard gameBoard = GameBoard.getGameBoard( GameLevel.MEDIUM );
    private float gridWidth;
    private float gridSeparatorSize;
    private float cellWidth;
    private float buttonWidth;
    private float buttonRadius;
    private float buttonMargin;
    private GestureDetector gestureDetector;

    public static Player joueur1 = new Player("Léa",Color.WHITE);
    public static Player joueur2 = new Player("Aymeric",Color.BLACK);
    public GameView( Context context ) {
        super( context );
        this.init();
    }
    private void init() {
        gestureDetector = new GestureDetector( getContext(),  this );
    }
    public GameView( Context context, @Nullable AttributeSet attrs ) {
        super( context, attrs ); this.init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // We compute some sizes
        gridSeparatorSize = (w / 9f) / 20f;

        gridWidth = w;                                  // Size of the grid (it's a square)
        cellWidth = gridWidth / 9f;                     // Size of a cell (it's a square too)
        buttonWidth = w / 7f;                           // Size of a button
        buttonRadius = buttonWidth / 10f;               // Size of the rounded corner for a button
        buttonMargin = (w - 6*buttonWidth) / 7f;        // Margin between two buttons

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // --- Draw cells ---

        paint.setTextAlign( Paint.Align.CENTER );
        paint.setTextSize( cellWidth*0.7f );

        for( int y=0; y<8; y++ ) {
            for( int x=0; x<8; x++ ) {
                int backgroundColor = Color.rgb(144,238,144);

                // Draw the background for the current cell
                paint.setColor( backgroundColor );
                canvas.drawRect(x * cellWidth,
                        y * cellWidth ,
                        (x+1) * cellWidth,
                        (y+1) * cellWidth,
                        paint);

                if (gameBoard.cells[y][x].player != new Player()) {

                    // Draw the assumed value for the cell.
                    paint.setColor(gameBoard.cells[y][x].player.getColor());
                    paint.setTextSize(cellWidth * 0.7f);
                    canvas.drawArc(x * cellWidth,
                            y * cellWidth ,
                            (x+1) * cellWidth,
                            (y+1) * cellWidth,
                            0,
                            360,
                            true,
                            paint);
                }
            }
        }

        // --- Draw the grid lines ---
        paint.setColor( Color.GRAY );
        paint.setStrokeWidth( gridSeparatorSize/2 );
        for( int i=0; i<=8; i++ ) {
            canvas.drawLine( i*cellWidth, 0, i*cellWidth, cellWidth*8, paint );
            canvas.drawLine( 0,i*cellWidth, cellWidth*8, i*cellWidth, paint );
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
    Player currentPlayer=getCurrentPlayer();

    public Player getCurrentPlayer(){
        if (currentPlayer==joueur1){
            return joueur2;
        }else {
            return joueur1;
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // --- Check grid cell click ---
        if (e.getY() < gridWidth) {
            Log.i("Lea","dans if 1");
            int cellX = (int) (e.getX() / cellWidth);
            int cellY = (int) (e.getY() / cellWidth);
            Log.i("Leatrois","celly: "+cellY+" / cellX :  "+cellX);
            Player adversaire = currentPlayer;
            Log.i("Leatrois",adversaire.getColor()+"");
            // Check if the cell is already occupied

            //diagonale droite haut
            int diaDH ;
            if ((8-cellX)<cellY){
                diaDH=(8-cellX);
            }else {
                diaDH=cellY;
            }

            //diagonale bas droite
            int diaBD ;
            if ((8-cellX)<(8-cellY)){
                diaBD=(8-cellX);
            }else {
                diaBD=(8-cellY);
            }

            //diagonale haut gauche
            int diaHG ;
            if (cellX<cellY){
                diaHG=cellX;
            }else {
                diaHG=cellY;
            }

            //diagonale bas gauche
            int diaBG ;
            if (cellX<(8-cellY)){
                diaBG=cellX;
            }else {
                diaBG=(8-cellY);
            }


            for(int g = 1; g <=8; g++){
                for(int b = 1; b <= cellX; b++){
            for(int i = 1; i <= 8-cellY; i++){
                for(int k = 1; k <= 7-cellX; k++){
                    for(int m = 1; m <= diaBG; m++){
                        for(int h = 1; h <= diaHG; h++){
                            for(int p = 1; p <= diaBD; p++){
                                for(int o = 1; o <= diaDH; o++){
            if (gameBoard.cells[cellY][cellX].player.getColor() == new Player().getColor()
            && (
                    //diagonale droite haut
                    (gameBoard.cells[cellY+1][cellX+1].player.getColor() == adversaire.getColor() &&
                    gameBoard.cells[cellY+o][cellX+o].player.getColor() == getCurrentPlayer().getColor() )
                            ||
                            //vertical bas
                    ( gameBoard.cells[cellY+1][cellX].player.getColor() == adversaire.getColor() &&
                            gameBoard.cells[cellY+i][cellX].player.getColor() == getCurrentPlayer().getColor() )
                            ||
                    ( gameBoard.cells[cellY][cellX+1].player.getColor() == adversaire.getColor()
                            &&
                            gameBoard.cells[cellY][cellX+k].player.getColor() == getCurrentPlayer().getColor() )
                            ||
                            //diagonale bas gauche
                    ( gameBoard.cells[cellY-1][cellX-1].player.getColor() == adversaire.getColor()
                            &&
                            gameBoard.cells[cellY-m][cellX-m].player.getColor() == getCurrentPlayer().getColor() )
                            ||
                    (gameBoard.cells[cellY][cellX-1].player.getColor() == adversaire.getColor()
                    && gameBoard.cells[cellY][cellX-b].player.getColor() == getCurrentPlayer().getColor() )
                            ||
                    (gameBoard.cells[cellY-1][cellX].player.getColor() == adversaire.getColor()
                    && gameBoard.cells[cellY-g][cellX].player.getColor() == getCurrentPlayer().getColor() )
                            ||
                            //diagonale gauche haut
                    (gameBoard.cells[cellY+1][cellX-1].player.getColor() == adversaire.getColor()
                    && gameBoard.cells[cellY+h][cellX-h].player.getColor() == getCurrentPlayer().getColor() )
                            ||
                            //diagonale droite bas
                    (gameBoard.cells[cellY-1][cellX+1].player.getColor() == adversaire.getColor()
                    && gameBoard.cells[cellY-p][cellX+p].player.getColor() == getCurrentPlayer().getColor() )
            )) {

                // Change the color of the cell to the current player's color
                Log.i("Lea", "dans if deux");
                currentPlayer = getCurrentPlayer();
                gameBoard.cells[cellY][cellX].player = currentPlayer;
            }else{
            }
                                }}}}}}}}

            gameBoard.currentCellX = cellX;
            gameBoard.currentCellY = cellY;
            postInvalidate(); // Redraw the view
            return true;
        }

        return true;
    }



    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

}

