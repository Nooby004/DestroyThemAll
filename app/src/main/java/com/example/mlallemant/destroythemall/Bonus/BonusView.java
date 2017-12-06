package com.example.mlallemant.destroythemall.Bonus;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.example.mlallemant.destroythemall.Bonus.Draw.SpeedMinusBonus;
import com.example.mlallemant.destroythemall.Bonus.Draw.SpeedPlusBonus;
import com.example.mlallemant.destroythemall.Bonus.Draw.WeaponShotSpeedMinusBonus;
import com.example.mlallemant.destroythemall.Bonus.Draw.WeaponShotSpeedPlusBonus;
import com.example.mlallemant.destroythemall.Bonus.Draw.WeaponTripleShotBonus;

/**
 * Created by m.lallemant on 29/11/2017.
 */

public class BonusView extends View {

    private BonusInterface bonusInterface;

    public final static int VEHICLE_SPEED_PLUS = 0;
    public final static int VEHICLE_SPEED_MINUS = 1;
    public final static int WEAPON_SHOT_SPEED_PLUS = 2;
    public final static int WEAPON_SHOT_SPEED_MINUS = 3;
    public final static int SHIELD = 4;
    public final static int WEAPON_TRIPLE_SHOT  = 5;
    public final static int WEAPON_GATLING_SHOT = 6;

    private int posX;
    private int posY;

    public BonusView(Context context, int width, int height, int BONUS_TYPE){
        super(context);

        switch (BONUS_TYPE) {
            case VEHICLE_SPEED_PLUS :
                bonusInterface = new SpeedPlusBonus(context, width, height);
                break;
            case VEHICLE_SPEED_MINUS :
                bonusInterface = new SpeedMinusBonus(context, width, height);
                break;
            case WEAPON_SHOT_SPEED_PLUS :
                bonusInterface = new WeaponShotSpeedPlusBonus(context, width, height);
                break;
            case WEAPON_SHOT_SPEED_MINUS :
                bonusInterface = new WeaponShotSpeedMinusBonus(context, width, height);
                break;
            case SHIELD : break;
            case WEAPON_TRIPLE_SHOT :
                bonusInterface = new WeaponTripleShotBonus(context, width, height);
                break;
            case WEAPON_GATLING_SHOT : break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        bonusInterface.draw(canvas);
    }

    public int getHeightTotal(){
        return bonusInterface.getHeight();
    }

    public int getWidthTotal(){
        return bonusInterface.getWidth();
    }

    public int getTop_(){
        return bonusInterface.getTop();
    }

    public int getBottom_(){
        return  bonusInterface.getBottom();
    }

    public int getLeft_(){
        return bonusInterface.getLeft();
    }

    public int getRight_(){
        return bonusInterface.getRight();
    }

    public int getTimeToFall(){
        return bonusInterface.getTimeToFall();
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

}
