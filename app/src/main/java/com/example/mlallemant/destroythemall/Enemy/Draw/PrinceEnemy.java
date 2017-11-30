package com.example.mlallemant.destroythemall.Enemy.Draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;

import com.example.mlallemant.destroythemall.Enemy.EnemyInterface;
import com.example.mlallemant.destroythemall.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.lallemant on 30/11/2017.
 */

public class PrinceEnemy implements EnemyInterface {

    //UI
    private Rect bloc;
    private Paint paint;
    private Paint backgroundPaint;
    private Path triangle_eye_left;
    private Path triangle_eye_right;
    private Rect bloc_mouse;
    private Path triangle_top_left;
    private Path triangle_top_center;
    private Path triangle_top_right;

    private int bloc_width;
    private int bloc_height;

    private List<Rect> bloc_life_point;
    private Paint paintLifePoint;


    //UTILS
    private int lifePoint = 5;
    private int timeToFall = 13000;


    public PrinceEnemy(Context context, int width, int height) {
        bloc_width = width / 13;
        bloc_height = height / 27;

        bloc = new Rect(bloc_width-bloc_width/2, bloc_height-bloc_height/2, bloc_width+bloc_width/2, bloc_height+bloc_height/2);
        triangle_eye_left = newTriangle(new Point(bloc.left + bloc_width/10, bloc.top + bloc_height/8), new Point(bloc.left + bloc_width/2 - bloc_width/10, bloc.top + bloc_height/8),
                new Point(bloc.left + bloc_width/2 - bloc_width/10,bloc.top + 4*bloc_height/10));
        triangle_eye_right  = newTriangle(new Point(bloc.right - bloc_width/10, bloc.top + bloc_height/8), new Point(bloc.right - bloc_width/2 + bloc_width/10, bloc.top + bloc_height/8),
                new Point(bloc.right - bloc_width/2 + bloc_width/10,bloc.top + 4*bloc_height/10));
        bloc_mouse = new Rect(bloc.centerX()-bloc_width/10, bloc.bottom - 4*bloc_height/10, bloc.centerX()+bloc_width/10, bloc.bottom-bloc_height/10);

        triangle_top_left = newTriangle(new Point(bloc.left, bloc.top), new Point(bloc.left + bloc_width/6, bloc.top - bloc_height/8), new Point(bloc.left + bloc_width/3, bloc.top));
        triangle_top_center = newTriangle(new Point(bloc.left + bloc_width/3, bloc.top), new Point(bloc.left + bloc_width/2, bloc.top - bloc_height/8), new Point(bloc.right - bloc_width/3, bloc.top));
        triangle_top_right = newTriangle(new Point(bloc.right - bloc_width/3, bloc.top), new Point(bloc.right - bloc_height/6, bloc.top - bloc_height/8), new Point(bloc.right, bloc.top));

        paint = new Paint();
        paint.setColor(Color.WHITE);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(ContextCompat.getColor(context, R.color.background_color));

        initLifePointDraw();
    }


    private void initLifePointDraw(){
        paintLifePoint = new Paint();
        paintLifePoint.setColor(Color.GREEN);

        bloc_life_point = new ArrayList<>();
        for (int i = 0; i < lifePoint; i++){
            Rect bloc_tmp = new Rect(bloc.left + i * bloc.width()/lifePoint, bloc.top - bloc_height/8 - 18, bloc.left + (i+1) * bloc.width()/lifePoint, bloc.top - bloc_height/8 - 13);
            //Rect bloc_tmp = new Rect(bloc.left + i * bloc.width()/lifePoint, 15, bloc.left + (i+1) * bloc.width()/lifePoint, 20);
            bloc_life_point.add(bloc_tmp);
        }
    }


    public void draw(Canvas canvas){
        canvas.drawRect(bloc, paint);
        canvas.drawPath(triangle_eye_left, backgroundPaint);
        canvas.drawPath(triangle_eye_right, backgroundPaint);
        canvas.drawPath(triangle_top_left, paint);
        canvas.drawPath(triangle_top_center, paint);
        canvas.drawPath(triangle_top_right, paint);
        canvas.drawRect(bloc_mouse, backgroundPaint);
        for (int i = 0; i < lifePoint; i++){
            canvas.drawRect(bloc_life_point.get(i), paintLifePoint);
        }
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

    public int getTimeToFall(){
        return timeToFall;
    }

    public int getLifePoint(){
        return lifePoint;
    }

    public void setLifePoint(int lifePoint){
        this.lifePoint = lifePoint;
    }

    public void setColorForImpactEffect(int color){
        paint.setColor(color);
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
