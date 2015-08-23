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

    private Context mContext;

    private Day[] mDays;

    public DayAdapter(Context context, Day[] days){

        mContext = context;
        mDays = days;
    }

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
            //brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView)convertView.findViewById(R.id.icon_image_view);
            holder.temperatureLabel = (TextView)convertView.findViewById(R.id.temperature_label);
            holder.dayLabel = (TextView)convertView.findViewById(R.id.day_name_label);

            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        Day day = mDays[position];
        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(day.getTemperatureMax() + "");
        holder.dayLabel.setText(day.getDayOfTheWeek());
        return convertView;
    }

    public static class ViewHolder{

        ImageView iconImageView;
        TextView temperatureLabel;
        TextView dayLabel;

    }
}
