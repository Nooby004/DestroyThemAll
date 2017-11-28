package com.example.mlallemant.destroythemall.Enemy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;

import com.example.mlallemant.destroythemall.DrawInterface;
import com.example.mlallemant.destroythemall.R;

/**
 * Created by m.lallemant on 27/11/2017.
 */

public class BasicEnemy implements DrawInterface {

    //UI
    private Rect bloc;
    private Paint paint;
    private Paint backgroundPaint;
    private Path triangle_eye_left;
    private Path triangle_eye_right;
    private Path triangle_mouse;

    private int bloc_width;
    private int bloc_height;

    public BasicEnemy(Context context, int width, int height) {
        bloc_width = width / 15;
        bloc_height = height / 30;

        bloc = new Rect(bloc_width-bloc_width/2, bloc_height-bloc_height/2, bloc_width+bloc_width/2, bloc_height+bloc_height/2);
        triangle_eye_left = newTriangle(new Point(bloc.left + bloc_width/10, bloc.top + bloc_height/8), new Point(bloc.left + bloc_width/10, bloc.top + 4*bloc_height/10),
                new Point(bloc.left + bloc_width/2 - bloc_width/10,bloc.top + 4*bloc_height/10));
        triangle_eye_right  = newTriangle(new Point(bloc.right - bloc_width/10, bloc.top + bloc_height/8), new Point(bloc.right - bloc_width/10, bloc.top + 4*bloc_height/10),
                new Point(bloc.right - bloc_width/2 + bloc_width/10,bloc.top + 4*bloc_height/10));
        triangle_mouse = newTriangle(new Point(bloc.left + bloc_width/10, bloc.bottom - 4*bloc_height/10), new Point(bloc.left+bloc_width/2, bloc.bottom-bloc_height/10),
                new Point(bloc.right - bloc_width/10,bloc.bottom - 4*bloc_height/10 ));

        paint = new Paint();
        paint.setColor(Color.WHITE);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(ContextCompat.getColor(context, R.color.background_color));
    }

    public void draw(Canvas canvas){
        canvas.drawRect(bloc, paint);
        canvas.drawPath(triangle_eye_left, backgroundPaint);
        canvas.drawPath(triangle_eye_right, backgroundPaint);
        canvas.drawPath(triangle_mouse, backgroundPaint);
    }

    public int getHeight(){
        return bloc_height;
    }

    public int getWidth(){
        return bloc_width;
    }

    public int getTop(){
        return bloc_height-bloc_height/2;
    }

    public int getBottom(){
        return bloc_height+bloc_height/2;
    }

    public int getLeft(){
        return bloc_width-bloc_width/2;
    }

    public int getRight(){
        return bloc_width+bloc_width/2;
    }

    private Path newTriangle(Point a, Point b, Point c){
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();
        return path;
    }

}
