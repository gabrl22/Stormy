package com.example.android.stormy.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.example.android.stormy.R;
import com.example.android.stormy.adapters.HourAdapter;
import com.example.android.stormy.weather.Day;
import com.example.android.stormy.weather.Hour;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HourlyForecastActivity extends AppCompatActivity {

    private Hour[] mHours;
    @Bind(R.id.hourly_layout)
    RelativeLayout mHourlyLayout;
    @Bind(R.id.recycler_view)RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);


        MainActivity a = new MainActivity();

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        //int minute = cal.get(Calendar.MINUTE);
        //int second = cal.get(Calendar.SECOND);
        Log.i(a.TAG, hour + "");

        if (hour >= 6 && hour < 12) {
            mHourlyLayout.setBackgroundResource(R.drawable.bg_gradient_morning);
        } else if (hour >= 12 && hour < 19) {
            mHourlyLayout.setBackgroundResource(R.drawable.bg_gradient_evening);
        } else {
            mHourlyLayout.setBackgroundResource(R.drawable.bg_gradient_night);
        }

        HourAdapter adapter = new HourAdapter(this, mHours);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
    }

}
