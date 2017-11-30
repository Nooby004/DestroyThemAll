package com.example.mlallemant.destroythemall.Vehicle;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.example.mlallemant.destroythemall.Vehicle.Draw.BasicShip;

/**
 * Created by m.lallemant on 23/11/2017.
 */



public class VehicleView extends View {

    //UTILS
    private int posX;
    private int posY;
    private VehicleInterface vehicleInterface;
    public final static int BASIC_SHIP = 0;
    private int TIME_BETWEEN_SHOT = 200;

    public VehicleView(Context context, int posX, int posY, int height_screen, int width_screen, int VEHICLE_TYPE){
        super(context);
        this.posX = posX;
        this.posY = posY;

        switch (VEHICLE_TYPE){
            case BASIC_SHIP :
                vehicleInterface = new BasicShip(posX, posY, height_screen, width_screen); break;
            default: break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        vehicleInterface.draw(canvas);
    }

    public int getHeightTotal(){
        return vehicleInterface.getHeight();
    }

    public int getWidthTotal(){
        return vehicleInterface.getWidth();
    }

    public int getTop_(){
        return vehicleInterface.getTop();
    }

    public int getBottom_(){
        return  vehicleInterface.getBottom();
    }

    public int getLeft_(){
        return vehicleInterface.getLeft();
    }

    public int getRight_(){
        return vehicleInterface.getRight();
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

    public int getSpeed() { return vehicleInterface.getSpeed(); }

    public void setSpeed(int speed) {
        vehicleInterface.setSpeed(speed);
    }

    public int getSpeedRatio() { return vehicleInterface.getSpeedRatio(); }

    public void setSpeedRatio(int speed) {
        vehicleInterface.setSpeedRatio(speed);
    }

    public int getTIME_BETWEEN_SHOT() {
        return TIME_BETWEEN_SHOT;
    }

    public void setTIME_BETWEEN_SHOT(int TIME_BETWEEN_SHOT) {
        this.TIME_BETWEEN_SHOT = TIME_BETWEEN_SHOT;
    }
}
