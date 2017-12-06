package com.example.mlallemant.destroythemall.Bonus.Draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;

import com.example.mlallemant.destroythemall.Bonus.BonusInterface;
import com.example.mlallemant.destroythemall.R;

/**
 * Created by m.lallemant on 30/11/2017.
 */

public class SpeedMinusBonus  implements BonusInterface {

    //UI
    private int radius_circle_back;
    private int radius_circle_center;
    private int x;
    private int y;
    private Path arrow_left;
    private Path arrow_right;


    private Paint background;
    private Paint paint;

    //UTILS
    private int timeToFall = 8000;


    public SpeedMinusBonus(Context context, int width, int height){
        radius_circle_back = width / 28;
        radius_circle_center = width / 33;

        x = radius_circle_back;
        y = radius_circle_back;

        arrow_left = newHeadArrow(x-radius_circle_center/3,y, 2* radius_circle_center/3, 4*radius_circle_center/3, 0);
        arrow_right = newHeadArrow( x+radius_circle_center/3, y, 2* radius_circle_center/3, 4*radius_circle_center/3, 0);

        background = new Paint();
        background.setColor(ContextCompat.getColor(context, R.color.background_color));

        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.orange_color));
    }

    public void draw(Canvas canvas){

        canvas.drawCircle(x,y,radius_circle_back, paint);
        canvas.drawCircle(x,y,radius_circle_center, background);
        canvas.drawPath(arrow_left, paint);
        canvas.drawPath(arrow_right, paint);
    }


    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param direction : 0 --> Left / 1 --> Right
     * @return
     */
    private Path newHeadArrow(int x, int y, int width, int height, int direction){
        Path path = new Path();

        if (direction == 0){
            path.moveTo(x + width / 2 , y - height / 2); // 1st
            path.lineTo(x, y - height/2);
            path.lineTo(x - width / 2, y);
            path.lineTo(x, y + height / 2);
            path.lineTo(x + width / 2, y + height / 2 );
            path.lineTo(x, y);
            path.lineTo(x + width / 2 , y - height / 2);
        } else {
            path.moveTo(x - width / 2 , y - height / 2); // 1st
            path.lineTo(x, y - height/2);
            path.lineTo(x + width / 2, y);
            path.lineTo(x, y + height / 2);
            path.lineTo(x - width / 2, y + height / 2 );
            path.lineTo(x, y);
            path.lineTo(x - width / 2 , y - height / 2);
        }

        return  path;

    }

    public int getHeight() {
        return radius_circle_back*2;
    }

    public int getWidth() {
        return  radius_circle_back*2;
    }

    public int getTop() {
        return 0;
    }

    public  int getBottom() {
        return 0;
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
