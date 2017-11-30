package com.example.mlallemant.destroythemall.UI;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.example.mlallemant.destroythemall.Vehicle.VehicleView;

/**
 * Created by m.lallemant on 29/11/2017.
 */

public class MainView extends RelativeLayout {


    private final int PERIOD_MS = 10;
    private int x = 0;
    private int movePosX = 0;

    private VehicleView vehicleView;

    public MainView(Context context, VehicleView vehicleView){
        super(context);
        this.vehicleView = vehicleView;
        setClickable(true);
        setGravity(Gravity.CENTER);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        setContinuous(true);

        int pointerIndex = ev.getActionIndex();
        MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
        ev.getPointerCoords(pointerIndex, pointerCoords);

        x = Float.valueOf(pointerCoords.x).intValue();

        switch( ev.getActionMasked() ) {
            case MotionEvent.ACTION_DOWN :
                if(cont)
                {
                    x = Float.valueOf(pointerCoords.x).intValue();
                    removeCallbacks(moveLeftContinuous);
                    removeCallbacks(moveRightContinuous);
                    if (x > 0 && x < getWidth()/2){
                        postDelayed(moveLeftContinuous, 10);
                    } else {
                        postDelayed(moveRightContinuous, 10);
                    }
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE :
                invalidate();
                return true;
            case MotionEvent.ACTION_UP :
                x = 0;
                removeCallbacks(moveLeftContinuous);
                removeCallbacks(moveRightContinuous);
                invalidate();
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                x = Float.valueOf(pointerCoords.x).intValue();
                removeCallbacks(moveLeftContinuous);
                removeCallbacks(moveRightContinuous);
                if (x > 0 && x < getWidth()/2){
                    postDelayed(moveLeftContinuous, 10);
                } else {
                    postDelayed(moveRightContinuous, 10);
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_POINTER_UP :
                invalidate();
                return true;
            default :
                return super.onTouchEvent(ev);
        }
    }

    public boolean cont = false;

    // sets input to continuous
    public void setContinuous(boolean b) { cont = b; }


    public Runnable moveLeftContinuous = new Runnable()
    {

        @Override
        public void run() {
            if(x != 0)
            {
                moveLeft();
                postDelayed(this, PERIOD_MS);
            }
        }

    };

    public Runnable moveRightContinuous = new Runnable()
    {

        @Override
        public void run() {
            if(x != 0)
            {
                moveRight();
                postDelayed(this, PERIOD_MS);
            }
        }

    };

    public VehicleView getVehicleView() {
        return vehicleView;
    }

    public void setVehicleView(VehicleView vehicleView) {
        this.vehicleView = vehicleView;
    }

    private void moveLeft(){
        if (vehicleView.getPosX() > vehicleView.getWidthTotal()/2) {
            movePosX -= vehicleView.getSpeed();
            if (movePosX < - getWidth() / 2 + vehicleView.getWidthTotal() / 2) {
                movePosX = - getWidth() / 2 + vehicleView.getWidthTotal() / 2;
                vehicleView.animate().x(movePosX).setDuration(0).start();
                vehicleView.setPosX(vehicleView.getWidthTotal() / 2);
                vehicleView.clearAnimation();
            } else {
                vehicleView.animate().x(movePosX).setDuration(0).start();
                vehicleView.setPosX(vehicleView.getPosX() - vehicleView.getSpeed());
                vehicleView.clearAnimation();
            }
        }
    }

    private void moveRight() {
        if (vehicleView.getPosX() < getWidth() - vehicleView.getWidthTotal()/2) {
            movePosX += vehicleView.getSpeed();
            if (movePosX >  (getWidth() / 2 - vehicleView.getWidthTotal() / 2)) {
                movePosX =  (getWidth() / 2 - vehicleView.getWidthTotal() / 2);
                vehicleView.animate().x(movePosX).setDuration(0).start();
                vehicleView.setPosX( getWidth() - vehicleView.getWidthTotal()/2);
                vehicleView.clearAnimation();
            } else {
                vehicleView.animate().x(movePosX).setDuration(0).start();
                vehicleView.setPosX(vehicleView.getPosX() + vehicleView.getSpeed());
                vehicleView.clearAnimation();
            }
        }
    }


}
