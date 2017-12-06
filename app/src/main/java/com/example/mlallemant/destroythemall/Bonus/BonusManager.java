package com.example.mlallemant.destroythemall.Bonus;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.util.Log;

import com.example.mlallemant.destroythemall.UI.MainView;
import com.example.mlallemant.destroythemall.Vehicle.VehicleView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by m.lallemant on 29/11/2017.
 */

public class BonusManager {

    //UTILS
    private MainView mainView;
    private int TIME_BETWEEN_BONUS_MS = 2000;
    private ArrayList<BonusView> bonusViewList;
    private boolean speedPlusBonusToggle = false;
    private boolean weaponShotSpeedPlusToggle = false;

    //RUNTIME
    private Runnable bonusRunnable;
    private Handler bonusHandler;

    public BonusManager(MainView mainView){
        this.mainView = mainView;
        bonusViewList = new ArrayList<>();
    }

    private void generateBonus(final int bonusType){
        final BonusView bonusView = new BonusView(mainView.getContext(), mainView.getWidth(), mainView.getHeight(), bonusType);

        mainView.addView(bonusView, 0);
        bonusView.bringToFront();

        int x;
        if (bonusType == BonusView.VEHICLE_SPEED_PLUS || bonusType == BonusView.WEAPON_SHOT_SPEED_PLUS){
            x =  getRandomIntBetween(0, mainView.getWidth() - bonusView.getWidthTotal());
        }else {
            x = mainView.getVehicleView().getPosX()-mainView.getVehicleView().getWidthTotal()/2;
        }
        bonusView.setPosX(x);

        ObjectAnimator translateXAnimation = ObjectAnimator.ofFloat(bonusView,"translationX" , x, x);
        ObjectAnimator translateYAnimation = ObjectAnimator.ofFloat(bonusView, "translationY", -50 , mainView.getHeight());

        bonusViewList.add(bonusView);

        final AnimatorSet set = new AnimatorSet();
        set.setDuration(bonusView.getTimeToFall());
        set.playTogether(translateXAnimation, translateYAnimation);
        set.start();

        translateYAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float yPosition = (Float) valueAnimator.getAnimatedValue();
                bonusView.setPosY(Float.valueOf(yPosition - bonusView.getHeightTotal()/2).intValue());

                if (bonusViewList.contains(bonusView)){
                    bonusViewList.set(bonusViewList.indexOf(bonusView), bonusView);

                    int xMinE = bonusView.getPosX();
                    int xMaxE = bonusView.getPosX() + bonusView.getWidthTotal();

                    if (yPosition > mainView.getVehicleView().getY()){
                            if (verifyImpactEnemyToVehicle(xMinE,xMaxE,Float.valueOf(yPosition).intValue(), bonusType)){
                            set.cancel();
                            bonusView.clearAnimation();
                            mainView.removeView(bonusView);
                            bonusViewList.remove(bonusView);
                        }
                    }

                    if (yPosition > mainView.getHeight()+50){
                        set.cancel();
                        bonusView.clearAnimation();
                        mainView.removeView(bonusView);
                        bonusViewList.remove(bonusView);
                    }
                }
            }
        });

    }

    public void launchBonusWave(){
        bonusHandler = new Handler();
        bonusRunnable = new Runnable() {
            @Override
            public void run() {
                generateBonusScenario();
                TIME_BETWEEN_BONUS_MS = getRandomIntBetween(5000, 25000);
                bonusHandler.postDelayed(this, TIME_BETWEEN_BONUS_MS);
            }
        };
        bonusHandler.postDelayed(bonusRunnable,5000);
    }

    private boolean verifyImpactEnemyToVehicle(int xMinE, int xMaxE, int yPosE, int bonusType){
        boolean impact = false;

        int xMinV = mainView.getVehicleView().getPosX() - mainView.getVehicleView().getWidthTotal()/2;
        int xMaxV = mainView.getVehicleView().getPosX() + mainView.getVehicleView().getWidthTotal()/2;

        if (xMinV < xMaxE && xMaxV > xMinE && yPosE > mainView.getVehicleView().getTop_() && yPosE < mainView.getVehicleView().getBottom_()){
            impact = true;
            activateBonusByType(bonusType);
        }

        return impact;
    }

    private void generateBonusScenario(){

       int rand1 = getRandomIntBetween(0, 100);
        int rand2 = getRandomIntBetween(0,2);
        if (rand1 > 70 ) {
            if (rand2 == 0 && !speedPlusBonusToggle) generateBonus(BonusView.VEHICLE_SPEED_PLUS);
            if (rand2 == 1 && !weaponShotSpeedPlusToggle)  generateBonus(BonusView.WEAPON_SHOT_SPEED_PLUS);
        }
        else if (rand1 < 61) {
            if (rand2 == 0) generateBonus(BonusView.VEHICLE_SPEED_MINUS);
            if (rand2 == 1) generateBonus(BonusView.WEAPON_SHOT_SPEED_MINUS);
        } else {
            generateBonus(BonusView.WEAPON_TRIPLE_SHOT);
        }
    }

    private void activateBonusByType(int bonusType){

        switch (bonusType){
            case BonusView.VEHICLE_SPEED_PLUS :
                mainView.getVehicleView().setSpeedRatio(mainView.getVehicleView().getSpeedRatio() - 70);
                speedPlusBonusToggle = true;
                break;
            case BonusView.VEHICLE_SPEED_MINUS :
                mainView.getVehicleView().setSpeedRatio(mainView.getVehicleView().getSpeedRatio() + 50);
                speedPlusBonusToggle = false;
                break;
            case BonusView.WEAPON_SHOT_SPEED_PLUS :
                mainView.getVehicleView().setTIME_BETWEEN_SHOT(mainView.getVehicleView().getTIME_BETWEEN_SHOT() - 70);
                weaponShotSpeedPlusToggle = true;
                break;
            case BonusView.WEAPON_SHOT_SPEED_MINUS :
                mainView.getVehicleView().setTIME_BETWEEN_SHOT(mainView.getVehicleView().getTIME_BETWEEN_SHOT() + 50);
                weaponShotSpeedPlusToggle = false;
                break;
            case BonusView.SHIELD : break;
            case BonusView.WEAPON_TRIPLE_SHOT :
                mainView.getVehicleView().setShotType(VehicleView.TRIPLE_SHOT);
                break;
            case BonusView.WEAPON_GATLING_SHOT : break;
        }

    }


    private int getRandomIntBetween(int x1, int x2){
        Random r = new Random();
        return r.nextInt(x2 - x1) + x1;
    }

    public void stopBonusWave(){
        bonusHandler.removeCallbacks(bonusRunnable);
    }


}
