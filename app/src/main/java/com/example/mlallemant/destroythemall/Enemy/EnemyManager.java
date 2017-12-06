package com.example.mlallemant.destroythemall.Enemy;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.example.mlallemant.destroythemall.UI.MainActivity;
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
    private ArrayList<EnemyView> enemyViewList;
    private MainView mainView;
    private EnemyScenario enemyScenario;
    private int level = 1;
    private int score = 0;

    //RUNTIME
    private Runnable enemyWaveRunnable;
    private Handler enemyWaveHandler;

    private Handler shotHandler;

    public EnemyManager(MainView mainView){
        this.mainView = mainView;
        enemyViewList = new ArrayList<>();

        enemyScenario = new EnemyScenario();
        this.onFinishEventListener = null;
        shotHandler = new Handler();
    }

    public void setOnFinishEventListener(OnFinishEventListener listener){
        this.onFinishEventListener = listener;
    }


    private void generateEnemy(int enemyType){
        final EnemyView enemyView = new EnemyView(mainView.getContext(), mainView.getWidth(), mainView.getHeight(), enemyType);

        mainView.addView(enemyView , 0);
        enemyView.bringToFront();
        int x = getRandomIntBetween(mainView.getVehicleView().getWidthTotal(), mainView.getWidth() - mainView.getVehicleView().getWidthTotal());
        enemyView.setPosX(x + enemyView.getWidthTotal()/2 );

        ObjectAnimator translateXAnimation = ObjectAnimator.ofFloat(enemyView,"translationX" , x, x);
        ObjectAnimator translateYAnimation = ObjectAnimator.ofFloat(enemyView, "translationY", -50 , mainView.getHeight());

        enemyViewList.add(enemyView);

        final AnimatorSet set = new AnimatorSet();
        set.setDuration(enemyView.getTimeToFall());
        set.playTogether(translateXAnimation, translateYAnimation);
        set.start();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                generateEnemyShot(enemyView);
                shotHandler.postDelayed(this, enemyView.getRateOfFire());
            }
        };
        enemyView.setShotRunnable(runnable);
        shotHandler.post(runnable);

        translateYAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float yPosition = (Float) valueAnimator.getAnimatedValue();

                enemyView.setPosY(Float.valueOf(yPosition + enemyView.getHeightTotal()).intValue());
                if (enemyViewList.contains(enemyView)){
                    enemyViewList.set(enemyViewList.indexOf(enemyView), enemyView);

                    int xMinE = enemyView.getPosX() - enemyView.getWidthTotal()/2;
                    int xMaxE = enemyView.getPosX() + enemyView.getWidthTotal()/2;

                    if (yPosition < mainView.getVehicleView().getPosY()){
                        if (verifyImpactEnemyToVehicle(xMinE,xMaxE,Float.valueOf(yPosition).intValue())){
                            set.cancel();
                            stopEnemyShooting(runnable);
                        }
                    }

                    if (yPosition >= mainView.getVehicleView().getBottom_()){
                        set.cancel();
                        finishGame();
                    }

                    if (yPosition > mainView.getHeight()+50){
                        stopEnemyShooting(runnable);
                        set.cancel();
                        enemyView.clearAnimation();
                        mainView.removeView(enemyView);
                        enemyViewList.remove(enemyView);
                    }
                }
            }
        });
    }

    private void generateBoss(int bossType){
        final EnemyView enemyView = new EnemyView(mainView.getContext(), mainView.getWidth(), mainView.getHeight(), bossType);

        mainView.addView(enemyView , 0);
        enemyView.bringToFront();

        int x = mainView.getWidth()/2 - enemyView.getWidthTotal()/2;
        enemyView.setPosX(x);

        ObjectAnimator translateXAnimation = ObjectAnimator.ofFloat(enemyView,"translationX" , x - enemyView.getWidthTotal()/2, x- enemyView.getWidthTotal()/2);
        ObjectAnimator translateYAnimation = ObjectAnimator.ofFloat(enemyView, "translationY", -50 , enemyView.getHeightTotal()/2);
        enemyViewList.add(enemyView);

        final AnimatorSet set = new AnimatorSet();
        set.setDuration(enemyView.getTimeToFall());
        set.playTogether(translateXAnimation, translateYAnimation);
        set.start();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                generateEnemyShot(enemyView);
                shotHandler.postDelayed(this, enemyView.getRateOfFire());
            }
        };
        enemyView.setShotRunnable(runnable);
        shotHandler.post(runnable);

        translateYAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float yPosition = (Float) valueAnimator.getAnimatedValue();
                enemyView.setPosY(Float.valueOf(yPosition + enemyView.getHeightTotal()).intValue());
            }
        });
    }


    public void stopEnemyShooting(Runnable runnable){
        shotHandler.removeCallbacks(runnable);
    }


    public void stopShooting(){
        shotHandler.removeCallbacksAndMessages(null);
    }

    private void generateEnemyShot(EnemyView enemyView){
        final int shot_width;
        int shot_height;

        if (enemyView.getEnemyType() >= EnemyView.BOSS_1) {
            shot_width = enemyView.getWidthTotal() / 10;
            shot_height = enemyView.getHeightTotal() / 7;
        } else {
            shot_width = enemyView.getWidthTotal() / 5;
            shot_height = enemyView.getHeightTotal() / 3;
        }


        final RelativeLayout rl = new RelativeLayout(enemyView.getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(shot_width, shot_height);
        rl.setLayoutParams(layoutParams);
        rl.setBackgroundColor(Color.RED);
        mainView.addView(rl,0);
        rl.bringToFront();

        int posXEnemy = enemyView.getPosX() + enemyView.getWidthTotal()/2;
        int posYEnemy = enemyView.getPosY() + enemyView.getHeightTotal()/2;

        ObjectAnimator translateXAnimation = ObjectAnimator.ofFloat(rl,"translationX" ,posXEnemy-shot_width/2, mainView.getVehicleView().getPosX());
        ObjectAnimator translateYAnimation = ObjectAnimator.ofFloat(rl, "translationY", posYEnemy , mainView.getHeight() + 20);

        final AnimatorSet set = new AnimatorSet();
        set.setDuration(3000);
        set.playTogether(translateXAnimation, translateYAnimation);
        set.start();


        final Point positionShot = new Point(posXEnemy,posYEnemy);

        translateXAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float xPosition = (Float) valueAnimator.getAnimatedValue();
                positionShot.set(Float.valueOf(xPosition).intValue(), positionShot.y);
            }
        });

        translateYAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float yPosition = (Float) valueAnimator.getAnimatedValue();
                positionShot.set(positionShot.x, Float.valueOf(yPosition).intValue());

                if (yPosition > mainView.getHeight()){
                    set.cancel();
                    mainView.removeView(rl);
                }

                int xMinE = positionShot.x - shot_width/2;
                int xMaxE = positionShot.x + shot_width/2;
                if (verifyImpactEnemyToVehicle(xMinE, xMaxE, positionShot.y)){
                    set.cancel();
                    mainView.removeView(rl);
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
                TIME_BETWEEN_ENEMY_MS = enemyScenario.getRandomTimeFromLevel(level);
                enemyWaveHandler.postDelayed(this, TIME_BETWEEN_ENEMY_MS);
            }
        };
        enemyWaveHandler.postDelayed(enemyWaveRunnable,100);
    }

    private void generateRandomScenario(){
        int rand = getRandomIntBetween(0, 101);

        int prop_0 = enemyScenario.getProportionByTypeAndLevel(EnemyScenario.BASIC_ENEMY_POS, level);
        int prop_1 = enemyScenario.getProportionByTypeAndLevel(EnemyScenario.SLOW_ENEMY_POS, level);
        int prop_2 = enemyScenario.getProportionByTypeAndLevel(EnemyScenario.PRINCE_ENEMY_POS, level);

        if (rand >= 0 && rand < prop_0) generateEnemy(EnemyView.BASIC_ENEMY);
        if (rand >= prop_0 && rand < prop_0 + prop_1) generateEnemy(EnemyView.SLOW_ENEMY);
        if (rand >= prop_0 + prop_1 && rand < 100) generateEnemy(EnemyView.PRINCE_ENEMY);
    }

    private int getRandomIntBetween(int x1, int x2){
        Random r = new Random();
        return r.nextInt(x2 - x1) + x1;
    }

    private boolean verifyImpactEnemyToVehicle(int xMinE, int xMaxE, int yPosE){
        boolean impact = false;

        int xMinV = mainView.getVehicleView().getPosX() - mainView.getVehicleView().getWidthTotal()/2;
        int xMaxV = mainView.getVehicleView().getPosX() + mainView.getVehicleView().getWidthTotal()/2;

        if (xMinV <= xMaxE && xMaxV >= xMinE && yPosE > mainView.getVehicleView().getTop_() && yPosE <mainView.getVehicleView().getBottom_()){
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
        stopShooting();
        if (!enemyViewList.isEmpty()) {
            for (EnemyView enemyView : enemyViewList) {
                enemyView.clearAnimation();
                mainView.removeView(enemyView);
            }
            enemyViewList.clear();
        }
        onFinishEventListener.onFinish();
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setScore(int score) {
        if (score == 50 || score == (51 * Math.pow(2, level -1 ) -1)){
            stopEnemyWave();
            generateBoss(EnemyView.BOSS_1);
        }

        this.score = score;
    }

    public void stopEnemyWave(){
        enemyWaveHandler.removeCallbacks(enemyWaveRunnable);
    }
}
