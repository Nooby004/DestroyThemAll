package com.example.mlallemant.destroythemall.Vehicle;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.example.mlallemant.destroythemall.DrawInterface;

/**
 * Created by m.lallemant on 23/11/2017.
 */



public class VehicleView extends View {

    //UTILS
    private int posX;
    private int posY;

    private DrawInterface drawInterface;
    public final static int BASIC_SHIP = 0;

    public VehicleView(Context context, int posX, int posY, int length_screen, int VEHICLE_TYPE){
        super(context);
        this.posX = posX;
        this.posY = posY;

        switch (VEHICLE_TYPE){
            case BASIC_SHIP :
                drawInterface = new BasicShip(posX, posY, length_screen); break;
            default: break;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawInterface.draw(canvas);
    }

    public int getHeightTotal(){
        return drawInterface.getHeight();
    }

    public int getWidthTotal(){
        return drawInterface.getWidth();
    }

    public int getTop_(){
        return drawInterface.getTop();
    }

    public int getBottom_(){
        return  drawInterface.getBottom();
    }

    public int getLeft_(){
        return drawInterface.getLeft();
    }

    public int getRight_(){
        return drawInterface.getRight();
    }

    public int getPosX(){
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

}
