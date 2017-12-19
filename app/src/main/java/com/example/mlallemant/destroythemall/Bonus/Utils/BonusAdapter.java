package com.example.mlallemant.destroythemall.Bonus.Utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mlallemant.destroythemall.Bonus.BonusView;
import com.example.mlallemant.destroythemall.R;

import java.util.ArrayList;

/**
 * Created by m.lallemant on 08/12/2017.
 */

public class BonusAdapter extends ArrayAdapter<BonusView>  {

    private ArrayList<BonusView> dataSet;
    Context mContext;

    private static class ViewHolder {
        ProgressBar pg;
        View view;
    }

    public BonusAdapter(ArrayList<BonusView> data, Context context){
        super(context, R.layout.item_bonus, data);
        this.dataSet = data;
        this.mContext = context;
    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Get the data item for this position
        BonusView bonusView = getItem(position);

        //Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder;
        final View result;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_bonus, parent, false);

            viewHolder.pg = (ProgressBar) convertView.findViewById(R.id.item_bonus_pg);
            viewHolder.view = (View) convertView.findViewById(R.id.item_bonus_view);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        if (bonusView != null) {

            try{

                BonusView bonusView1 = new BonusView(mContext, 800,1700, BonusView.WEAPON_SHOT_SPEED_MINUS);

                viewHolder.view.setBackgroundColor(Color.RED);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return convertView;
    }



}
