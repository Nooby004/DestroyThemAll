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
 * Created by m.lallemant on 29/11/2017.
 */

public class SpeedPlusBonus implements BonusInterface {

    //UI
    private Rect rect;
    private int bloc_width;
    private int bloc_height;

    private TextPaint textPaint;
    private Paint background;
    private String s = "Speed++";

    //UTILS
    private int timeToFall = 2000;


    public SpeedPlusBonus(Context context, int width, int height){
        bloc_width = width / 25;
        bloc_height = height / 30;

        background = new Paint();
        background.setColor(ContextCompat.getColor(context, R.color.background_color));

        textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(bloc_width);

        float w;
        float textSize;
        w = textPaint.measureText(s);
        textSize = textPaint.getTextSize();

        rect = new Rect(Float.valueOf(-w).intValue() , Float.valueOf(-textSize).intValue(), Float.valueOf(w).intValue(), Float.valueOf(+textSize).intValue());
    }

    public void draw(Canvas canvas){

        canvas.drawRect(rect, background);
        canvas.drawText(s,rect.width() + 2 * rect.left , rect.height() - rect.bottom , textPaint);
    }

    public int getHeight() {
        return rect.height();
    }

    public int getWidth() {
        return rect.width();
    }

    public int getTop() {
        return rect.top;
    }

    public  int getBottom() {
        return rect.bottom;
    }

    public int getLeft() {
        return rect.left;
    }

    public int getRight() {
        return rect.right;
    }

    public int getTimeToFall() {
        return timeToFall;
    }
}
