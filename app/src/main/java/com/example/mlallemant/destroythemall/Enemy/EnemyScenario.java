package com.example.mlallemant.destroythemall.Enemy;

import java.util.Random;

/**
 * Created by m.lallemant on 04/12/2017.
 */

public class EnemyScenario {

    public final static int BASIC_ENEMY_POS = 0;
    public final static int SLOW_ENEMY_POS = 1;
    public final static int PRINCE_ENEMY_POS = 2;

    private final static int NB_ENEMY = 3;
    private final static int NB_LEVEL = 10;

    private int enemyProportion[][];

    public EnemyScenario(){
        enemyProportion = new int[NB_ENEMY][NB_LEVEL];
        fillEnemyProportion();
    }


    private void fillEnemyProportion(){
        enemyProportion[BASIC_ENEMY_POS][0] = 80;enemyProportion[SLOW_ENEMY_POS][0] = 20;enemyProportion[PRINCE_ENEMY_POS][0] = 0;
        enemyProportion[BASIC_ENEMY_POS][1] = 75;enemyProportion[SLOW_ENEMY_POS][1] = 23;enemyProportion[PRINCE_ENEMY_POS][1] = 2;
        enemyProportion[BASIC_ENEMY_POS][2] = 70;enemyProportion[SLOW_ENEMY_POS][2] = 28;enemyProportion[PRINCE_ENEMY_POS][2] = 2;
        enemyProportion[BASIC_ENEMY_POS][3] = 60;enemyProportion[SLOW_ENEMY_POS][3] = 35;enemyProportion[PRINCE_ENEMY_POS][3] = 5;
        enemyProportion[BASIC_ENEMY_POS][4] = 55;enemyProportion[SLOW_ENEMY_POS][4] = 40;enemyProportion[PRINCE_ENEMY_POS][4] = 5;
        enemyProportion[BASIC_ENEMY_POS][5] = 50;enemyProportion[SLOW_ENEMY_POS][5] = 42;enemyProportion[PRINCE_ENEMY_POS][5] = 8;
        enemyProportion[BASIC_ENEMY_POS][6] = 45;enemyProportion[SLOW_ENEMY_POS][5] = 45;enemyProportion[PRINCE_ENEMY_POS][5] = 10;
        enemyProportion[BASIC_ENEMY_POS][7] = 40;enemyProportion[SLOW_ENEMY_POS][5] = 45;enemyProportion[PRINCE_ENEMY_POS][5] = 15;
        enemyProportion[BASIC_ENEMY_POS][8] = 30;enemyProportion[SLOW_ENEMY_POS][5] = 50;enemyProportion[PRINCE_ENEMY_POS][5] = 20;
        enemyProportion[BASIC_ENEMY_POS][9] = 20;enemyProportion[SLOW_ENEMY_POS][5] = 50;enemyProportion[PRINCE_ENEMY_POS][5] = 30;
    }


    private int getRandomIntBetween(int x1, int x2){
        Random r = new Random();
        return r.nextInt(x2 - x1) + x1;
    }


    public int getProportionByTypeAndLevel(int type, int level){
        return enemyProportion[type][level-1];
    }

    private int[] getTimeBetweenEnemyByLevel(int level){
        int times[] = new int[2];

        int timeMin = 1000 - ((level-1) * 150);
        int timeMax = timeMin + 1000;

        times[0] = timeMin;
        times[1] = timeMax;

        return times;
    }

    public int getRandomTimeFromLevel(int level){
        int times[] = getTimeBetweenEnemyByLevel(level);
        return getRandomIntBetween(times[0], times[1]);

    }

}
