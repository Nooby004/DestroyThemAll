package com.example.mlallemant.destroythemall.Enemy;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.example.mlallemant.destroythemall.DrawInterface;

/**
 * Created by m.lallemant on 27/11/2017.
 */

public class EnemyView extends View {

    private DrawInterface drawInterface;
    public final static int BASIC_BLOC = 0;

    private int posX;
    private int posY;

    public EnemyView(Context context, int width, int height, int ENEMY_TYPE){
        super(context);

        switch (ENEMY_TYPE){
            case BASIC_BLOC :
                drawInterface = new BasicEnemy(context, width, height); break;
            default:break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawInterface.draw(canvas);
    }

    public int getHeightTotal(){
        return drawInterface.getHeight();
    }

    public int getWidthTotal(){
        return drawInterface.getWidth();
    }

    public int getTop_(){
        return drawInterface.getTop();
    }

    public int getBottom_(){
        return  drawInterface.getBottom();
    }

    public int getLeft_(){
        return drawInterface.getLeft();
    }

    public int getRight_(){
        return drawInterface.getRight();
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

}
