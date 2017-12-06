package com.example.mlallemant.destroythemall.Bonus.Draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;

import com.example.mlallemant.destroythemall.Bonus.BonusInterface;
import com.example.mlallemant.destroythemall.R;

/**
 * Created by m.lallemant on 30/11/2017.
 */

public class WeaponShotSpeedMinusBonus implements BonusInterface {

    //UI
    private int radius_circle_back;
    private int radius_circle_center;
    private int x;
    private int y;

    private Rect top;
    private Rect left;
    private Rect right;
    private Rect bottom;

    private Rect minus;

    private Paint background;
    private Paint paint;

    //UTILS
    private int timeToFall = 7000;


    public WeaponShotSpeedMinusBonus(Context context, int width, int height){
        radius_circle_back = width / 28;
        radius_circle_center = width / 33;

        x = radius_circle_back + 2*radius_circle_back/3;
        y = radius_circle_back + 2*radius_circle_back/3;

        int delta = radius_circle_back - radius_circle_center;

        top = newRectangle(x  - radius_circle_center - delta/2, y, delta, 2*radius_circle_back/3, 0);
        left =  newRectangle(x  - radius_circle_center - delta/2, y, delta, 2*radius_circle_back/3, 1);
        bottom = newRectangle(x  + radius_circle_center + delta/2, y, delta, 2*radius_circle_back/3, 0);
        right = newRectangle(x  + radius_circle_center + delta/2, y, delta, 2*radius_circle_back/3, 1);

        minus = newRectangle(x,y,delta, 2*radius_circle_back/3,1);

        background = new Paint();
        background.setColor(ContextCompat.getColor(context, R.color.background_color));

        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.orange_color));

    }

    public void draw(Canvas canvas){
        canvas.drawCircle(x,y,radius_circle_back, paint);
        canvas.drawCircle(x,y,radius_circle_center, background);
        canvas.drawRect(top, paint);
        canvas.drawRect(left, paint);
        canvas.drawRect(bottom, paint);
        canvas.drawRect(right, paint);

        canvas.drawRect(minus, paint);
    }


    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param rotation : 0 --> VERTICAL / 1 --> HORIZONTAL
     * @return
     */
    private Rect newRectangle(int x, int y, int width, int height, int rotation){
        Rect rect;
        if (rotation == 0){
            rect = new Rect(y-width/2, x-height/2, y+width/2, x+height/2);
        }else {
            rect = new Rect(x-height/2, y-width/2, x+height/2, y+width/2);
        }

        return rect;
    }

    public int getHeight() {
        return radius_circle_back + bottom.height();
    }

    public int getWidth() {
        return  radius_circle_back*2 + left.height();
    }

    public int getTop() {
        return 0;
    }

    public  int getBottom() {
        return bottom.bottom;
    }

    public int getLeft() {
        return 0;
    }

    public int getRight() {
        return 0;
    }

    public int getTimeToFall() {
        return timeToFall;
    }

}
