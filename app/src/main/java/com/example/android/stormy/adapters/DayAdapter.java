package com.example.android.stormy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.stormy.R;
import com.example.android.stormy.weather.Day;

/**
 * Created by Gabriel on 8/22/15.
 */
public class DayAdapter extends BaseAdapter {

    private Context mContextt;

    private Day[] mDays;


    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null){
            
        }
        return null;
    }

    public static class ViewHolder{

        ImageView iconImageView;
        TextView temperatureLabel;
        TextView dayLabel;

    }
}
