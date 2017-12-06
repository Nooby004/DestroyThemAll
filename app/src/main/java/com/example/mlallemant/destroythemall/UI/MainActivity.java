package com.example.mlallemant.destroythemall.UI;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mlallemant.destroythemall.Bonus.BonusManager;
import com.example.mlallemant.destroythemall.Enemy.EnemyManager;
import com.example.mlallemant.destroythemall.Enemy.EnemyView;
import com.example.mlallemant.destroythemall.R;
import com.example.mlallemant.destroythemall.Utils.SharedPreferencesManager;
import com.example.mlallemant.destroythemall.Vehicle.VehicleView;


import java.util.ArrayList;
import java.util.List;

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
    private List<ImageView> iv_background_list;

    //UTILS
    private final String TAG = "MainActivity";
    private EnemyManager enemyManager;
    private BonusManager bonusManager;
    private int score = 0;
    private int level = 1;
    private SharedPreferencesManager sharedPreferencesManager;
    private ValueAnimator backgroundAnimator;
    private MediaPlayer backgroundMedia;

    //RUNTIME
    private Handler shootingHandler;
    private Runnable shootingRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferencesManager = new SharedPreferencesManager(this);

        backgroundMedia = MediaPlayer.create(getApplicationContext(), R.raw.background_song);
        backgroundMedia.setLooping(true);

        initUI();
        initListener();
    }

    @Override
    public void onStop(){
        super.onStop();

        enemyManager.stopEnemyWave();
        bonusManager.stopBonusWave();
        stopShooting();
        backgroundMedia.pause();
        if (backgroundAnimator != null) backgroundAnimator.cancel();
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


        //BACKGROUND
        RelativeLayout.LayoutParams background_param = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        iv_background_list = new ArrayList<>();

        ImageView i0 = new ImageView(this);
        i0.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.diapositive1));
        i0.setLayoutParams(background_param);

        ImageView i1 = new ImageView(this);
        i1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.diapositive2));
        i1.setLayoutParams(background_param);

        ImageView i2 = new ImageView(this);
        i2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.diapositive3));
        i2.setLayoutParams(background_param);

        ImageView i3 = new ImageView(this);
        i3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.diapositive4));
        i3.setLayoutParams(background_param);

        ImageView i4 = new ImageView(this);
        i4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.diapositive5));
        i4.setLayoutParams(background_param);

        ImageView i5 = new ImageView(this);
        i5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.diapositive1));
        i5.setLayoutParams(background_param);

        iv_background_list.add(i0);
        iv_background_list.add(i1);
        iv_background_list.add(i2);
        iv_background_list.add(i3);
        iv_background_list.add(i4);
        iv_background_list.add(i5);

        for (int i =0 ; i <iv_background_list.size(); i++) {
            mainView.addView(iv_background_list.get(i), 0);
        }

        initAnimationBackground();

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
                tv_best_score.setText("Lvl " + level);

                vehicleView.setSpeedRatio(840);
                enemyManager.setTIME_BETWEEN_ENEMY_MS(1500);
                vehicleView.setTIME_BETWEEN_SHOT(200);
                vehicleView.setShotType(VehicleView.NORMAL_SHOT);

                enemyManager.launchEnemyWave();
                bonusManager.launchBonusWave();
                launchShooting();
                launchAnimationBackground();

                backgroundMedia.start();

            }
        });
    }

    private void initAnimationBackground(){
        backgroundAnimator = ValueAnimator.ofFloat(0.0f, 5.0f);
        backgroundAnimator.setRepeatCount(ValueAnimator.INFINITE);
        backgroundAnimator.setInterpolator(new LinearInterpolator());
        backgroundAnimator.setDuration(40000L);
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final float progress = (float) valueAnimator.getAnimatedValue();
                final float height = iv_background_list.get(0).getHeight();
                final float translationY = height * progress;

                for (int i = 0; i<iv_background_list.size(); i++){
                    iv_background_list.get(i).setTranslationY(translationY - i*height);
                }
            }
        });
    }

    private void launchAnimationBackground(){

        if (!backgroundAnimator.isStarted()) backgroundAnimator.start();
        else backgroundAnimator.resume();

    }

    private void verifyImpactShotToEnemy(int xMinS, int xMaxS, int shotPosY, RelativeLayout shotView, AnimatorSet set){
        if (!enemyManager.getEnemyViewList().isEmpty()) {
            for (EnemyView enemyView : enemyManager.getEnemyViewList()) {

                int xMinE = enemyView.getPosX() ;
                int xMaxE = enemyView.getPosX() + enemyView.getWidthTotal() ;
                int yImpact = enemyView.getPosY() + enemyView.getHeightTotal() / 2;

                Log.e(TAG, "bottom : " + enemyView.getPosY());

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
                        enemyManager.setScore(score);
                        tv_score.setText(score+"");
                        level = getLevel();
                        enemyManager.setLevel(level);
                        tv_best_score.setText("Lvl " + level);

                        enemyManager.stopEnemyShooting(enemyView.getShotRunnable());
                        generateExplosion(20,enemyView.getPosX() + enemyView.getWidthTotal()/2, enemyView.getPosY());
                        if (enemyView.getEnemyType() >= EnemyView.BOSS_1){
                            enemyManager.stopShooting();
                            enemyManager.launchEnemyWave();
                        }
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

                switch (vehicleView.getShotType()){
                    case VehicleView.NORMAL_SHOT :
                        generateShot(0);
                        break;
                    case VehicleView.TRIPLE_SHOT :
                        generateShot(0);
                        generateShot(-1);
                        generateShot(1);
                        break;
                    case VehicleView.GATLING_SHOT :
                        break;
                }

                shootingHandler.postDelayed(this, vehicleView.getTIME_BETWEEN_SHOT());
            }
        };
        shootingHandler.postDelayed(shootingRunnable,100);
    }

    /**
     *
     * @param typeShot : 0 --> CENTER / -1 --> LEFT / 1 --> RIGHT
     */

    private void generateShot(int typeShot){
        final int shot_width = vehicleView.getHeightTotal() / 10;
        int shot_height = vehicleView.getHeightTotal() / 7;

        final RelativeLayout rl = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(shot_width, shot_height);
        rl.setLayoutParams(layoutParams);
        rl.setBackgroundColor(Color.CYAN);
        mainView.addView(rl,0);
        rl.bringToFront();

        final int posXVehicle = vehicleView.getPosX();
        int posYVehicle = vehicleView.getTop_()-shot_height;

        ObjectAnimator translateXAnimation;
        switch (typeShot){
            case 0 : translateXAnimation = ObjectAnimator.ofFloat(rl,"translationX" ,posXVehicle-shot_width/2, posXVehicle-shot_width/2);break;
            case -1 : translateXAnimation = ObjectAnimator.ofFloat(rl,"translationX" ,posXVehicle-shot_width/2, posXVehicle-5*shot_width -shot_width/2);break;
            case 1 : translateXAnimation = ObjectAnimator.ofFloat(rl,"translationX" ,posXVehicle-shot_width/2, posXVehicle+5*shot_width -shot_width/2);break;
            default: translateXAnimation = ObjectAnimator.ofFloat(rl,"translationX" ,posXVehicle-shot_width/2, posXVehicle-shot_width/2);break;
        }
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
        level = 1;
        btn_play.setVisibility(View.VISIBLE);
        backgroundMedia.pause();
        backgroundAnimator.pause();
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
            explode.bringToFront();
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

    private int getLevel(){
        if (score<51) return 1;
        else return Double.valueOf((Math.log((score-1)/50) / Math.log(2)) + 2).intValue();
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


}
