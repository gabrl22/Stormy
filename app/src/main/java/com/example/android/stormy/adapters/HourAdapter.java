package com.example.android.stormy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.stormy.R;
import com.example.android.stormy.weather.Hour;

/**
 * Created by Gabriel on 8/23/15.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder>  {


    private Hour[] mHours;
    private Context mContext;

    public HourAdapter(Context context, Hour[] hours) {


        mContext = context;
        mHours = hours;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hourly_list_item, viewGroup, false);
        HourViewHolder viewHolder = new HourViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder hourViewHolder, int i) {
        hourViewHolder.bindHour(mHours[i]);

    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }


    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTimeLabel;
        TextView mTemperatureLabel;
        TextView mSummaryLabel;
        ImageView mIconImageView;

        public HourViewHolder(View itemView) {
            super(itemView);

            mTimeLabel = (TextView) itemView.findViewById(R.id.time_label);
            mTemperatureLabel = (TextView) itemView.findViewById(R.id.temperature_label);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summary_label);
            mIconImageView = (ImageView) itemView.findViewById(R.id.icon_imageview);

            itemView.setOnClickListener(this);
        }

        public void bindHour(Hour hour) {

            mTimeLabel.setText(hour.getHour());
            mTemperatureLabel.setText(hour.getTemperature() + "");
            mSummaryLabel.setText(hour.getSummary());
            mIconImageView.setImageResource(hour.getIconId());

        }
        @Override
        public void onClick(View v) {
            String time = mTimeLabel.getText().toString();
            String temperature = mTemperatureLabel.getText().toString();
            String summary = mSummaryLabel.getText().toString();
            String message = String.format("At %s it will be %s and %s",
                    time, temperature, summary);
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }

    }


}
