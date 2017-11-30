package com.example.mlallemant.destroythemall.Vehicle;

import android.graphics.Canvas;

/**
 * Created by m.lallemant on 27/11/2017.
 */

public interface VehicleInterface {

    void draw(Canvas canvas);
    int getHeight();
    int getWidth();
    int getTop();
    int getBottom();
    int getLeft();
    int getRight();
    int getSpeed();
    void setSpeed(int speed);
    int getSpeedRatio();
    void setSpeedRatio(int ratio);
}
