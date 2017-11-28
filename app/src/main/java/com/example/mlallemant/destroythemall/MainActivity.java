package com.example.mlallemant.destroythemall;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.print.PrintAttributes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mlallemant.destroythemall.Enemy.EnemyView;
import com.example.mlallemant.destroythemall.Vehicle.VehicleView;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MainActivity extends AppCompatActivity {

    //UI
    private LinearLayout ll_parent;
    private CustomLayout mainView;
    private VehicleView vehicleView;
    private Button btn_play;
    private TextView tv_score;

    //UTILS
    private final String TAG = "MainActivity";
    private int movePosX = 0;

    private final int PERIOD_MS = 10;
    private final int RATIO = 830;
    private int MOVE_SPEED;
    private int TIME_BETWEEN_SHOOTS_MS = 200;
    private int TIME_BETWEEN_ENEMY_MS = 1000;
    private int score = 0;

    private ArrayList<EnemyView> enemyViewList;

    private Handler enemyWaveHandler;
    private Handler shootingHandler;
    private Handler difficultyHandler;
    private Runnable enemyWaveRunnable;
    private Runnable shootingRunnable;
    private Runnable difficultyRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initListener();
        enemyViewList = new ArrayList<>();
    }

    private void initUI(){
        ll_parent = findViewById(R.id.main_ll_parent);

        //MAIN VIEW
        mainView = new CustomLayout(this);
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

        //OBSERVER GET SIZE SCREEN
        ViewTreeObserver observer = mainView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                MOVE_SPEED = (PERIOD_MS * mainView.getWidth()) / RATIO;
                vehicleView = new VehicleView(getApplicationContext(),
                        mainView.getWidth()/2,
                        mainView.getHeight()-Double.valueOf(0.06 * mainView.getHeight()).intValue(),
                        mainView.getHeight(),
                        VehicleView.BASIC_SHIP);

                mainView.addView(vehicleView);
                mainView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
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

                TIME_BETWEEN_ENEMY_MS = 1000;
                launchShooting();
                launchEnemyWave();
                increaseDifficulty();
            }
        });
    }

    private void moveLeft(){
        if (vehicleView.getPosX() > vehicleView.getWidthTotal()/2) {
            movePosX -= MOVE_SPEED;
            if (movePosX < -mainView.getWidth() / 2 + vehicleView.getWidthTotal() / 2) {
                movePosX = -mainView.getWidth() / 2 + vehicleView.getWidthTotal() / 2;
                vehicleView.animate().x(movePosX).setDuration(0).start();
                vehicleView.setPosX(vehicleView.getWidthTotal() / 2);
                vehicleView.clearAnimation();
            } else {
                vehicleView.animate().x(movePosX).setDuration(0).start();
                vehicleView.setPosX(vehicleView.getPosX() - MOVE_SPEED);
                vehicleView.clearAnimation();
            }
        }

    }

    private void moveRight() {
        if (vehicleView.getPosX() < mainView.getWidth() - vehicleView.getWidthTotal()/2) {
            movePosX+=MOVE_SPEED;
            if (movePosX > mainView.getWidth() / 2 + vehicleView.getWidthTotal() / 2) {
                movePosX = mainView.getWidth() / 2 + vehicleView.getWidthTotal() / 2;
                vehicleView.animate().x(movePosX).setDuration(0).start();
                vehicleView.setPosX( mainView.getWidth() - vehicleView.getWidthTotal() / 2);
                vehicleView.clearAnimation();
            } else {
                vehicleView.animate().x(movePosX).setDuration(0).start();
                vehicleView.setPosX(vehicleView.getPosX() + MOVE_SPEED);
                vehicleView.clearAnimation();
            }
        }
    }

    private void increaseDifficulty(){
        HandlerThread handlerThread = new HandlerThread("MyHandlerThread");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        difficultyHandler = new Handler(looper);
        difficultyRunnable = new Runnable() {
            @Override
            public void run() {
                TIME_BETWEEN_ENEMY_MS--;
                difficultyHandler.postDelayed(this, 1000);
            }
        };
        difficultyHandler.post(difficultyRunnable);
    }

    private void launchEnemyWave(){
        enemyWaveHandler = new Handler();
        enemyWaveRunnable = new Runnable() {
            @Override
            public void run() {
                generateEnemy();
                enemyWaveHandler.postDelayed(this, TIME_BETWEEN_ENEMY_MS);
            }
        };
        enemyWaveHandler.postDelayed(enemyWaveRunnable,100);
    }

    private boolean verifyImpactEnemyToVehicle(int xMinE, int xMaxE, int yPosE){
        boolean impact = false;

        int xMinV = vehicleView.getPosX() - vehicleView.getWidthTotal()/2;
        int xMaxV = vehicleView.getPosX() + vehicleView.getWidthTotal()/2;

        if (xMinV <= xMaxE && xMaxV >= xMinE && yPosE > vehicleView.getTop_()){
            impact = true;
            stopShooting();
            stopEnemyWave();
            finishGame();
        }

        return impact;
    }

    private void generateEnemy(){
        final EnemyView enemyView = new EnemyView(getApplicationContext(), mainView.getWidth(), mainView.getHeight(), EnemyView.BASIC_BLOC);

        mainView.addView(enemyView , 0);

        int x = getRandomIntBetween(vehicleView.getWidthTotal(), mainView.getWidth() - vehicleView.getWidthTotal());
        enemyView.setPosX(x + enemyView.getWidthTotal()/2 );

        ObjectAnimator translateXAnimation = ObjectAnimator.ofFloat(enemyView,"translationX" , x, x);
        ObjectAnimator translateYAnimation = ObjectAnimator.ofFloat(enemyView, "translationY", -50 , mainView.getHeight());

        enemyViewList.add(enemyView);

        final AnimatorSet set = new AnimatorSet();
        set.setDuration(4000);
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

                    if (yPosition < vehicleView.getPosY()){
                        if (verifyImpactEnemyToVehicle(xMinE,xMaxE,Float.valueOf(yPosition).intValue())){
                            set.cancel();
                        }
                    }

                    if (yPosition >= vehicleView.getBottom_()){
                        set.cancel();
                        stopShooting();
                        stopEnemyWave();
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

    private void stopEnemyWave(){
        enemyWaveHandler.removeCallbacks(enemyWaveRunnable);
    }

    private boolean verifyImpactShotToEnemy(int xMinS, int xMaxS, int shotPosY){
        boolean impact = false;

        if (!enemyViewList.isEmpty()) {
            for (EnemyView enemyView : enemyViewList) {

                int xMinE = enemyView.getPosX() ;
                int xMaxE = enemyView.getPosX() + enemyView.getWidthTotal() ;
                int yImpact = enemyView.getPosY() + enemyView.getHeightTotal() / 2;

                if (xMinS <= xMaxE && xMaxS >= xMinE && shotPosY < yImpact) {
                    impact = true;
                    enemyView.clearAnimation();
                    mainView.removeView(enemyView);
                    enemyViewList.remove(enemyView);

                    score++;
                    tv_score.setText(score+"");
                    generateExplosion(20,enemyView.getPosX() + enemyView.getWidthTotal()/2, enemyView.getPosY());
                    break;
                }
            }
        }
        return impact;
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
                shootingHandler.postDelayed(this, TIME_BETWEEN_SHOOTS_MS);
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

                if (verifyImpactShotToEnemy(xMinS, xMaxS, Float.valueOf(yPosition).intValue())){
                    set.cancel();
                    rl.clearAnimation();
                    mainView.removeView(rl);
                }

                if (yPosition < 0){
                    set.cancel();
                    rl.clearAnimation();
                    mainView.removeView(rl);
                }
            }
        });
    }

    private void finishGame(){
        if (!enemyViewList.isEmpty()) {
            for (EnemyView enemyView : enemyViewList) {
                enemyView.clearAnimation();
                mainView.removeView(enemyView);
            }
            enemyViewList.clear();
        }
        score = 0;
        difficultyHandler.removeCallbacks(difficultyRunnable);
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


    private int getRandomIntBetween(int x1, int x2){
        Random r = new Random();
        return r.nextInt(x2 - x1) + x1;
    }

    public class CustomLayout extends RelativeLayout {

        private int x = 0;

        public CustomLayout(Context context){
            super(context);
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
                        if (x > 0 && x < mainView.getWidth()/2){
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
                    if (x > 0 && x <mainView.getWidth()/2){
                        postDelayed(moveLeftContinuous, 10);
                    } else {
                        postDelayed(moveRightContinuous, 10);
                    }
                    invalidate();
                    return true;
                case MotionEvent.ACTION_POINTER_UP :
                    //x = 0;
                    //removeCallbacks(moveLeftContinuous);
                    //removeCallbacks(moveRightContinuous);
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



    }
}
