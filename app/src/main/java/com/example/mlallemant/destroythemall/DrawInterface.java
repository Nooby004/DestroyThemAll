package com.example.mlallemant.destroythemall;

import android.graphics.Canvas;

/**
 * Created by m.lallemant on 27/11/2017.
 */

public interface DrawInterface {

    void draw(Canvas canvas);
    int getHeight();
    int getWidth();
    int getTop();
    int getBottom();
    int getLeft();
    int getRight();
}
