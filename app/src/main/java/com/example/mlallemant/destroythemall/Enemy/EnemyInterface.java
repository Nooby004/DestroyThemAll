package com.example.mlallemant.destroythemall.Enemy;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by m.lallemant on 29/11/2017.
 */

public interface EnemyInterface {

    void draw(Canvas canvas);
    int getHeight();
    int getWidth();
    int getTop();
    int getBottom();
    int getLeft();
    int getRight();
    int getTimeToFall();
    int getLifePoint();
    void setLifePoint(int lifePoint);
    void setColorForImpactEffect(int color);
    int getRateOfFire();
}
