package com.example.mlallemant.destroythemall.Bonus.Draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;

import com.example.mlallemant.destroythemall.Bonus.BonusInterface;
import com.example.mlallemant.destroythemall.R;

/**
 * Created by m.lallemant on 06/12/2017.
 */

public class WeaponTripleShotBonus implements BonusInterface {

    //UI
    private int radius_circle_back;
    private int radius_circle_center;
    private int x;
    private int y;

    private Path bullet1;
    private Path bullet2;
    private Path bullet3;


    private Paint background;
    private Paint paint;
    private Paint bullet_paint;

    //UTILS
    private int timeToFall = 5000;


    public WeaponTripleShotBonus(Context context, int width, int height){
        radius_circle_back = width / 25;
        radius_circle_center = width / 25;

        x = radius_circle_back;
        y = radius_circle_back;

        bullet1 = newBullet(x - radius_circle_center/2, y, radius_circle_center/ 3, 5 * radius_circle_center / 4);
        bullet2 = newBullet(x , y, radius_circle_center/ 3, 5 * radius_circle_center / 4);
        bullet3 = newBullet(x + radius_circle_center/2, y, radius_circle_center/ 3, 5 * radius_circle_center / 4);



        background = new Paint();
        background.setColor(ContextCompat.getColor(context, R.color.background_color));

        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.orange_color));

        bullet_paint = new Paint();
        bullet_paint.setStrokeWidth(2);
        bullet_paint.setColor(ContextCompat.getColor(context, R.color.orange_color));
        bullet_paint.setStyle(Paint.Style.STROKE);
    }

    public void draw(Canvas canvas){

        canvas.drawPath(bullet1, bullet_paint);
        canvas.drawPath(bullet2, bullet_paint);
        canvas.drawPath(bullet3, bullet_paint);

    }


     private Path newBullet(int x, int y, int width, int height){
        Path path = new Path();

        int middle_height = 3*height/5;
        path.moveTo(x - width/2 , y - middle_height/2);
        path.lineTo(x - width/4, y - middle_height/2 - height/5);
        path.lineTo(x + width/4, y - middle_height/2 - height/5);
        path.lineTo(x + width/2 , y - middle_height/2);
        path.lineTo(x - width/2 , y - middle_height/2);
        path.lineTo(x - width/2, y + middle_height/2);
        path.lineTo(x - width/4, y + middle_height/2);
        path.lineTo(x - width/4, y + middle_height/2 + height/10);
        path.lineTo(x + width/4, y + middle_height/2 + height/10);
        path.lineTo(x + width/4, y + middle_height/2);
        path.lineTo(x - width/4, y + middle_height/2);
        path.lineTo(x + width/2, y + middle_height/2);
        path.lineTo(x + width/2, y - middle_height/2);

        return path;
    }

    public int getHeight() {
        return radius_circle_back;
    }

    public int getWidth() {
        return  3*radius_circle_back / 2;
    }

    public int getTop() {
        return 0;
    }

    public  int getBottom() {
        return radius_circle_back;
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
