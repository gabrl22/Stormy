package com.example.android.stormy.ui;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.example.android.stormy.R;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DailyForecastActivity extends ListActivity {

    @Bind(R.id.daily_layout)
    RelativeLayout mDailyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        ButterKnife.bind(this);
        MainActivity a = new MainActivity();

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY) -1;
        //int minute = cal.get(Calendar.MINUTE);
        //int second = cal.get(Calendar.SECOND);
        Log.i(a.TAG, hour + "");

        if (hour >= 6 && hour < 12) {
            mDailyLayout.setBackgroundResource(R.drawable.bg_gradient_morning);
        } else if (hour >= 12 && hour < 7) {
            mDailyLayout.setBackgroundResource(R.drawable.bg_gradient_evening);
        } else {
            mDailyLayout.setBackgroundResource(R.drawable.bg_gradient_night);
        }





        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
                "Saturday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                daysOfWeek);

        setListAdapter(adapter);
    }


}
