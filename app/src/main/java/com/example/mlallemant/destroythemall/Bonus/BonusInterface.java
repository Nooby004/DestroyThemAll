package com.example.mlallemant.destroythemall.Bonus;

import android.graphics.Canvas;

/**
 * Created by m.lallemant on 29/11/2017.
 */

public interface BonusInterface {

    void draw(Canvas canvas);
    int getHeight();
    int getWidth();
    int getTop();
    int getBottom();
    int getLeft();
    int getRight();
    int getTimeToFall();
}
