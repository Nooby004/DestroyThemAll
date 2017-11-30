package com.example.mlallemant.destroythemall.Enemy.Draw;

import android.content.Context;
import android.content.res.Resources;
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
 * Created by m.lallemant on 29/11/2017.
 */

public class SlowEnemy implements EnemyInterface {

    //UI
    private Rect bloc;
    private Paint paint;
    private Paint backgroundPaint;
    private Path triangle_eye_left;
    private Path triangle_eye_right;
    private Rect bloc_tmp;

    private Rect mouse_bloc;

    private List<Rect> bloc_life_point;
    private Paint paintLifePoint;

    private int bloc_width;
    private int bloc_height;

    //UTILS
    private int lifePoint = 3;
    private int timeToFall = 10000;

    public SlowEnemy(Context context, int width, int height){
        bloc_width = width / 12;
        bloc_height = height / 25;

        bloc = new Rect(bloc_width-bloc_width/2, bloc_height-bloc_height/2, bloc_width+bloc_width/2, bloc_height+bloc_height/2);
        bloc_tmp = new Rect(bloc.left, bloc.top-20, bloc.left, bloc.top);

        triangle_eye_left = newTriangle(new Point(bloc.left + bloc_width/10, bloc.top + 4*bloc_height/10), new Point(bloc.left + bloc_width/2 - bloc_width/10,bloc.top + 4*bloc_height/10),
                new Point(bloc.left + bloc_width/2 - bloc_width/10, bloc.top + bloc_height/8));

        triangle_eye_right = newTriangle( new Point(bloc.right - bloc_width/10, bloc.top + 4*bloc_height/10), new Point(bloc.right - bloc_width/2 + bloc_width/10,bloc.top + 4*bloc_height/10),
                new Point(bloc.right - bloc_width/2 + bloc_width/10, bloc.top + bloc_height/8));

        mouse_bloc = new Rect(bloc.left + bloc_width/10, bloc.bottom - 4*bloc_height/10,  bloc.right - bloc_width/10, bloc.bottom-bloc_height/10 );

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
            Rect bloc_tmp = new Rect(bloc.left + i * bloc.width()/lifePoint, bloc.top - 18, bloc.left + (i+1) * bloc.width()/lifePoint, bloc.top - 13);
            //Rect bloc_tmp = new Rect(bloc.left + i * bloc.width()/lifePoint, 15, bloc.left + (i+1) * bloc.width()/lifePoint, 20);
            bloc_life_point.add(bloc_tmp);
        }
    }

    public void draw(Canvas canvas){
        canvas.drawRect(bloc, paint);
        //canvas.drawRect(bloc_tmp, backgroundPaint);
        canvas.drawPath(triangle_eye_left, backgroundPaint);
        canvas.drawPath(triangle_eye_right, backgroundPaint);
        canvas.drawRect(mouse_bloc, backgroundPaint);
        for (int i = 0; i < lifePoint; i++){
            canvas.drawRect(bloc_life_point.get(i), paintLifePoint);
        }
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
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
