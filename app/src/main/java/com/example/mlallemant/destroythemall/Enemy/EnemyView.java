package com.example.mlallemant.destroythemall.Enemy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.example.mlallemant.destroythemall.Enemy.Draw.BasicEnemy;
import com.example.mlallemant.destroythemall.Enemy.Draw.PrinceEnemy;
import com.example.mlallemant.destroythemall.Enemy.Draw.SlowEnemy;


/**
 * Created by m.lallemant on 27/11/2017.
 */

public class EnemyView extends View {

    private EnemyInterface enemyInterface;
    public final static int BASIC_ENEMY = 0;
    public final static int SLOW_ENEMY = 1;
    public final static int PRINCE_ENEMY = 2;

    private int posX;
    private int posY;

    public EnemyView(Context context, int width, int height, int ENEMY_TYPE){
        super(context);

        switch (ENEMY_TYPE){
            case BASIC_ENEMY :
                enemyInterface = new BasicEnemy(context, width, height); break;

            case SLOW_ENEMY :
                enemyInterface = new SlowEnemy(context, width, height); break;

            case PRINCE_ENEMY :
                enemyInterface = new PrinceEnemy(context, width, height); break;
            default:break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        enemyInterface.draw(canvas);
    }

    public int getHeightTotal(){
        return enemyInterface.getHeight();
    }

    public int getWidthTotal(){
        return enemyInterface.getWidth();
    }

    public int getTop_(){
        return enemyInterface.getTop();
    }

    public int getBottom_(){
        return  enemyInterface.getBottom();
    }

    public int getLeft_(){
        return enemyInterface.getLeft();
    }

    public int getRight_(){
        return enemyInterface.getRight();
    }

    public int getTimeToFall(){
        return enemyInterface.getTimeToFall();
    }

    public int getLifePoint(){
        return enemyInterface.getLifePoint();
    }

    public void setLifePoint(int lifePoint){
       if (lifePoint > 0) makeImpactEffect();
        enemyInterface.setLifePoint(lifePoint);
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

    private void makeImpactEffect(){
        enemyInterface.setColorForImpactEffect(Color.DKGRAY);
        invalidate();

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                enemyInterface.setColorForImpactEffect(Color.WHITE);
                invalidate();
            }
        };
        handler.postDelayed(runnable, 50);
    }

}
