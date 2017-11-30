package com.example.mlallemant.destroythemall.Enemy;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;

import com.example.mlallemant.destroythemall.UI.MainView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by m.lallemant on 29/11/2017.
 */

public class EnemyManager {


    //INTERFACE
    private OnFinishEventListener onFinishEventListener;
    public interface OnFinishEventListener {
        void onFinish();
    }

    //UTILS
    private int TIME_BETWEEN_ENEMY_MS = 1500;
    private int MIN_TIME = 1000;
    private int MAX_TIME = 2000;
    private ArrayList<EnemyView> enemyViewList;
    private MainView mainView;

    //RUNTIME
    private Runnable enemyWaveRunnable;
    private Handler enemyWaveHandler;

    public EnemyManager(MainView mainView){
        this.mainView = mainView;
        enemyViewList = new ArrayList<>();

        this.onFinishEventListener = null;
    }

    public void setOnFinishEventListener(OnFinishEventListener listener){
        this.onFinishEventListener = listener;
    }


    private void generateEnemy(int enemyType){
        final EnemyView enemyView = new EnemyView(mainView.getContext(), mainView.getWidth(), mainView.getHeight(), enemyType);

        mainView.addView(enemyView , 0);

        int x = getRandomIntBetween(mainView.getVehicleView().getWidthTotal(), mainView.getWidth() - mainView.getVehicleView().getWidthTotal());
        enemyView.setPosX(x + enemyView.getWidthTotal()/2 );

        ObjectAnimator translateXAnimation = ObjectAnimator.ofFloat(enemyView,"translationX" , x, x);
        ObjectAnimator translateYAnimation = ObjectAnimator.ofFloat(enemyView, "translationY", -50 , mainView.getHeight());

        enemyViewList.add(enemyView);

        final AnimatorSet set = new AnimatorSet();
        set.setDuration(enemyView.getTimeToFall());
        set.playTogether(translateXAnimation, translateYAnimation);
        set.start();

        translateYAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float yPosition = (Float) valueAnimator.getAnimatedValue();

                enemyView.setPosY(Float.valueOf(yPosition).intValue());
                if (enemyViewList.contains(enemyView)){
                    enemyViewList.set(enemyViewList.indexOf(enemyView), enemyView);

                    int xMinE = enemyView.getPosX() - enemyView.getWidthTotal()/2;
                    int xMaxE = enemyView.getPosX() + enemyView.getWidthTotal()/2;

                    if (yPosition < mainView.getVehicleView().getPosY()){
                        if (verifyImpactEnemyToVehicle(xMinE,xMaxE,Float.valueOf(yPosition).intValue())){
                            set.cancel();
                        }
                    }

                    if (yPosition >= mainView.getVehicleView().getBottom_()){
                        set.cancel();
                        finishGame();
                    }

                    if (yPosition > mainView.getHeight()+50){
                        set.cancel();
                        enemyView.clearAnimation();
                        mainView.removeView(enemyView);
                        enemyViewList.remove(enemyView);
                    }
                }

            }
        });
    }

    public void launchEnemyWave(){
        enemyWaveHandler = new Handler();
        enemyWaveRunnable = new Runnable() {
            @Override
            public void run() {
                generateRandomScenario();
                int rand = getRandomIntBetween(0,5);
                if (rand == 0) MIN_TIME --;
                if (rand == 1) MAX_TIME --;

                TIME_BETWEEN_ENEMY_MS = getRandomIntBetween(MIN_TIME, MAX_TIME);
                enemyWaveHandler.postDelayed(this, TIME_BETWEEN_ENEMY_MS);
            }
        };
        enemyWaveHandler.postDelayed(enemyWaveRunnable,100);
    }

    private void generateRandomScenario(){
        int rand = getRandomIntBetween(0, 101);
        if (rand < 50 ) generateEnemy(EnemyView.BASIC_ENEMY);
        if (rand > 49 && rand < 85) generateEnemy(EnemyView.SLOW_ENEMY);
        if (rand > 84 && rand < 100) generateEnemy(EnemyView.PRINCE_ENEMY);
    }

    private int getRandomIntBetween(int x1, int x2){
        Random r = new Random();
        return r.nextInt(x2 - x1) + x1;
    }

    private boolean verifyImpactEnemyToVehicle(int xMinE, int xMaxE, int yPosE){
        boolean impact = false;

        int xMinV = mainView.getVehicleView().getPosX() - mainView.getVehicleView().getWidthTotal()/2;
        int xMaxV = mainView.getVehicleView().getPosX() + mainView.getVehicleView().getWidthTotal()/2;

        if (xMinV <= xMaxE && xMaxV >= xMinE && yPosE > mainView.getVehicleView().getTop_()){
            impact = true;
            finishGame();
        }

        return impact;
    }

    public int getTIME_BETWEEN_ENEMY_MS() {
        return TIME_BETWEEN_ENEMY_MS;
    }

    public void setTIME_BETWEEN_ENEMY_MS(int TIME_BETWEEN_ENEMY_MS) {
        this.TIME_BETWEEN_ENEMY_MS = TIME_BETWEEN_ENEMY_MS;
    }

    public ArrayList<EnemyView> getEnemyViewList() {
        return enemyViewList;
    }

    private void finishGame(){
        stopEnemyWave();
        if (!enemyViewList.isEmpty()) {
            for (EnemyView enemyView : enemyViewList) {
                enemyView.clearAnimation();
                mainView.removeView(enemyView);
            }
            enemyViewList.clear();
        }
        onFinishEventListener.onFinish();
    }

    public void stopEnemyWave(){
        enemyWaveHandler.removeCallbacks(enemyWaveRunnable);
    }
}
