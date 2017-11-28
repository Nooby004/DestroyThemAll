package com.example.mlallemant.destroythemall.Vehicle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.mlallemant.destroythemall.DrawInterface;

/**
 * Created by m.lallemant on 27/11/2017.
 */

public class BasicShip implements DrawInterface {


    //UI
    private Rect bloc_left;
    private Rect bloc_right;
    private Rect bloc_center;
    private Rect bloc_bottom_left;
    private Rect bloc_bottom_right;

    private Path triangle_bottom;
    private Path triangle_top;
    private Path triangle_left_top;
    private Path triangle_right_top;

    //Utils
    private Paint paint;
    private int bloc_center_length;
    private int bloc_width;
    private int bloc_small_width;

    public BasicShip(int posX, int posY, int length_screen){

        bloc_center_length = Double.valueOf(0.05 * length_screen).intValue();
        bloc_width = bloc_center_length/2;
        bloc_small_width = bloc_width/2;

        bloc_center = new Rect(posX-bloc_width/2,posY-bloc_center_length/2,posX+bloc_width/2,posY+bloc_center_length/2);
        bloc_left = new Rect(bloc_center.left - bloc_width, bloc_center.top + bloc_width, bloc_center.left, bloc_center.bottom);
        bloc_right = new Rect(bloc_center.right, bloc_center.top + bloc_width, bloc_center.right + bloc_width, bloc_center.bottom);
        bloc_bottom_left = new Rect(bloc_left.centerX()- bloc_small_width/2, bloc_left.bottom, bloc_left.centerX() + bloc_small_width/2, bloc_left.bottom + bloc_small_width);
        bloc_bottom_right = new Rect(bloc_right.centerX() - bloc_small_width/2, bloc_right.bottom, bloc_right.centerX() + bloc_small_width/2, bloc_right.bottom + bloc_small_width);

        triangle_bottom = newTriangle(new Point(bloc_center.left, bloc_center.bottom), new Point(bloc_center.centerX(), bloc_bottom_left.bottom), new Point(bloc_center.right, bloc_center.bottom));
        triangle_top = newTriangle(new Point(bloc_center.centerX()-bloc_small_width/2, bloc_center.top), new Point(bloc_center.centerX(), bloc_center.top - bloc_small_width), new Point(bloc_center.centerX()+bloc_small_width/2, bloc_center.top));
        triangle_left_top = newTriangle(new Point(bloc_left.centerX(), bloc_left.top), new Point(bloc_left.right, bloc_left.top), new Point(bloc_center.left, bloc_center.top));
        triangle_right_top = newTriangle(new Point(bloc_right.centerX(), bloc_right.top), new Point(bloc_right.left,bloc_right.top), new Point(bloc_center.right, bloc_center.top));

        paint = new Paint();
        paint.setColor(Color.WHITE);

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

     public void draw(Canvas canvas){
         canvas.drawRect(bloc_center, paint);
         canvas.drawRect(bloc_left, paint);
         canvas.drawRect(bloc_right, paint);
         canvas.drawRect(bloc_bottom_left, paint);
         canvas.drawRect(bloc_bottom_right, paint);
         canvas.drawPath(triangle_bottom, paint);
         canvas.drawPath(triangle_top, paint);
         canvas.drawPath(triangle_left_top, paint);
         canvas.drawPath(triangle_right_top, paint);
     }

    public int getHeight(){
        return bloc_bottom_left.bottom - bloc_center.top - bloc_small_width;
    }

    public int getWidth(){
        return bloc_right.right - bloc_left.left;
    }

    public int getTop(){
        return bloc_center.top - bloc_small_width;
    }

    public int getBottom(){
        return bloc_bottom_left.bottom;
    }

    public int getLeft(){
        return bloc_left.left;
    }

    public int getRight(){
        return bloc_right.right;
    }

}
