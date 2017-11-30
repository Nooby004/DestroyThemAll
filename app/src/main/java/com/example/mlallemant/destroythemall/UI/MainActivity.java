package com.example.mlallemant.destroythemall.UI;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mlallemant.destroythemall.Bonus.BonusManager;
import com.example.mlallemant.destroythemall.Enemy.EnemyManager;
import com.example.mlallemant.destroythemall.Enemy.EnemyView;
import com.example.mlallemant.destroythemall.R;
import com.example.mlallemant.destroythemall.Utils.SharedPreferencesManager;
import com.example.mlallemant.destroythemall.Vehicle.VehicleView;


import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MainActivity extends AppCompatActivity {

    //UI
    private LinearLayout ll_parent;
    private MainView mainView;
    private VehicleView vehicleView;
    private Button btn_play;
    private TextView tv_score;
    private TextView tv_best_score;

    //UTILS
    private final String TAG = "MainActivity";
    private EnemyManager enemyManager;
    private BonusManager bonusManager;
    private int score = 0;
    private SharedPreferencesManager sharedPreferencesManager;

    //RUNTIME
    private Handler shootingHandler;
    private Runnable shootingRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        initUI();
        initListener();
    }

    @Override
    public void onStop(){
        super.onStop();

        enemyManager.stopEnemyWave();
        bonusManager.stopBonusWave();
        stopShooting();
    }

    private void initUI(){
        ll_parent = findViewById(R.id.main_ll_parent);

        //MAIN VIEW
        mainView = new MainView(this, vehicleView);
        RelativeLayout.LayoutParams ll = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainView.setLayoutParams(ll);
        ll_parent.addView(mainView);

        //BUTTON PLAY
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        btn_play = new Button(this);
        btn_play.setText("Play");
        btn_play.setTextColor(ContextCompat.getColor(this, R.color.background_color));
        btn_play.setLayoutParams(params);
        mainView.addView(btn_play);

        //TEXT VIEW SCORE
        RelativeLayout.LayoutParams tv_score_param = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv_score_param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        tv_score_param.addRule(RelativeLayout.ALIGN_PARENT_START);
        tv_score_param.setMargins(dpToPx(15), dpToPx(5),0,0);
        tv_score = new TextView(this);
        tv_score.setText("0");
        tv_score.setTextSize(20);
        tv_score.setTextColor(Color.WHITE);
        tv_score.setLayoutParams(tv_score_param);
        tv_score.setVisibility(View.INVISIBLE);
        mainView.addView(tv_score);

        //TEXT VIEW  BEST SCORE
        RelativeLayout.LayoutParams tv_best_score_param = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv_best_score_param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        tv_best_score_param.addRule(RelativeLayout.ALIGN_PARENT_END);
        tv_best_score_param.setMargins(0 , dpToPx(5),dpToPx(15),0);
        tv_best_score = new TextView(this);
        tv_best_score.setTextSize(20);
        tv_best_score.setTextColor(Color.WHITE);
        tv_best_score.setLayoutParams(tv_best_score_param);
        tv_best_score.setVisibility(View.VISIBLE);
        tv_best_score.setText("Best : " + sharedPreferencesManager.getBestScore());
        mainView.addView(tv_best_score);

        //OBSERVER GET SIZE SCREEN
        ViewTreeObserver observer = mainView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                vehicleView = new VehicleView(getApplicationContext(),
                        mainView.getWidth()/2,
                        mainView.getHeight()-Double.valueOf(0.06 * mainView.getHeight()).intValue(),
                        mainView.getHeight(),
                        mainView.getWidth(),
                        VehicleView.BASIC_SHIP);

                mainView.setVehicleView(vehicleView);
                mainView.addView(vehicleView);
                mainView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                enemyManager = new EnemyManager(mainView);
                enemyManager.setOnFinishEventListener(new EnemyManager.OnFinishEventListener() {
                    @Override
                    public void onFinish() {
                        finishGame();
                    }
                });
                bonusManager = new BonusManager(mainView);

            }
        });
    }

    private void initListener(){
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_play.setVisibility(View.INVISIBLE);
                tv_score.setVisibility(View.VISIBLE);
                tv_score.setText("0");
                tv_best_score.setVisibility(View.INVISIBLE);

                vehicleView.setSpeedRatio(840);
                enemyManager.setTIME_BETWEEN_ENEMY_MS(1500);
                enemyManager.launchEnemyWave();
                bonusManager.launchBonusWave();
                launchShooting();
            }
        });
    }


    private void verifyImpactShotToEnemy(int xMinS, int xMaxS, int shotPosY, RelativeLayout shotView, AnimatorSet set){

        if (!enemyManager.getEnemyViewList().isEmpty()) {
            for (EnemyView enemyView : enemyManager.getEnemyViewList()) {

                int xMinE = enemyView.getPosX() ;
                int xMaxE = enemyView.getPosX() + enemyView.getWidthTotal() ;
                int yImpact = enemyView.getPosY() + enemyView.getHeightTotal() / 2;

                if (xMinS <= xMaxE && xMaxS >= xMinE && shotPosY < yImpact) {

                    set.cancel();
                    shotView.clearAnimation();
                    mainView.removeView(shotView);

                    enemyView.setLifePoint(enemyView.getLifePoint()-1);

                    if (enemyView.getLifePoint() < 1){
                        enemyView.clearAnimation();
                        mainView.removeView(enemyView);
                        enemyManager.getEnemyViewList().remove(enemyView);

                        score++;
                        tv_score.setText(score+"");
                        generateExplosion(20,enemyView.getPosX() + enemyView.getWidthTotal()/2, enemyView.getPosY());
                        break;
                    }

                }
            }
        }
    }

    private void stopShooting(){
        shootingHandler.removeCallbacks(shootingRunnable);
    }

    private void launchShooting(){
        shootingHandler = new Handler();
        shootingRunnable = new Runnable() {
            @Override
            public void run() {
                generateShot();
                shootingHandler.postDelayed(this, vehicleView.getTIME_BETWEEN_SHOT());
            }
        };
        shootingHandler.postDelayed(shootingRunnable,100);
    }

    private void generateShot(){
        final int shot_width = vehicleView.getHeightTotal() / 10;
        int shot_height = vehicleView.getHeightTotal() / 7;

        final RelativeLayout rl = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(shot_width, shot_height);
        rl.setLayoutParams(layoutParams);
        rl.setBackgroundColor(Color.CYAN);
        mainView.addView(rl,0);

        final int posXVehicle = vehicleView.getPosX();
        int posYVehicle = vehicleView.getTop_()-shot_height;

        ObjectAnimator translateXAnimation = ObjectAnimator.ofFloat(rl,"translationX" ,posXVehicle-shot_width/2, posXVehicle-shot_width/2);
        ObjectAnimator translateYAnimation = ObjectAnimator.ofFloat(rl, "translationY", posYVehicle , -10);

        final AnimatorSet set = new AnimatorSet();
        set.setDuration(2000);
        set.playTogether(translateXAnimation, translateYAnimation);
        set.start();

        translateYAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float yPosition = (Float) valueAnimator.getAnimatedValue();

                int xMinS = posXVehicle - shot_width/2;
                int xMaxS = posXVehicle + shot_width/2;

                verifyImpactShotToEnemy(xMinS, xMaxS, Float.valueOf(yPosition).intValue(), rl, set);

                if (yPosition < 0){
                    set.cancel();
                    rl.clearAnimation();
                    mainView.removeView(rl);
                }
            }
        });
    }

    private void finishGame(){
        stopShooting();
        bonusManager.stopBonusWave();
        if (sharedPreferencesManager.getBestScore() < score) {
            sharedPreferencesManager.setBestScore(score);
        }
        tv_best_score.setText("Best : " + sharedPreferencesManager.getBestScore()+"");
        tv_best_score.setVisibility(View.VISIBLE);
        score = 0;
        btn_play.setVisibility(View.VISIBLE);
    }

    private void generateExplosion(int nbPoint, int x, int y){

        double angleRef = 360 / nbPoint;
        int explodeWidth = mainView.getWidth()/250;

        for (int i = 1 ; i < nbPoint+1 ; i++){
            int x_ = Double.valueOf(x + 15* explodeWidth * cos(angleRef*i*0.0174532925)).intValue();
            int y_ = Double.valueOf(y + 15* explodeWidth * sin(angleRef*i*0.0174532925)).intValue();

            final FrameLayout explode = new FrameLayout(this);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(explodeWidth, explodeWidth);
            explode.setLayoutParams(params);
            explode.setBackgroundColor(Color.WHITE);

            mainView.addView(explode,0);
            ObjectAnimator translateXAnimation = ObjectAnimator.ofFloat(explode,"translationX" ,x, x_);
            ObjectAnimator translateYAnimation = ObjectAnimator.ofFloat(explode, "translationY", y , y_);
            final AnimatorSet set = new AnimatorSet();
            set.setDuration(500);
            set.playTogether(translateXAnimation, translateYAnimation);
            set.start();

            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mainView.removeView(explode);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


}
