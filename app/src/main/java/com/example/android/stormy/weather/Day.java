package com.example.android.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Gabriel on 08/20/15.
 */
//Hay que implementar la interface Parcelable para poder pasar objetos atraves de los intent
public class Day implements Parcelable{

    private long mTime;
    private String mSummary;
    private double mTemperatureMax;
    private String mIcon;
    private String mTimezone;

    public Day(){}

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getTemperatureMax() {
        return (int)Math.round(mTemperatureMax);
    }

    public void setTemperatureMax(double temperatureMax) {
        mTemperatureMax = temperatureMax;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public String getDayOfTheWeek(){

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(
                TimeZone.getTimeZone(mTimezone)
        );
        Date dateTime = new Date(mTime * 1000);
        return formatter.format(dateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(mTime);
        dest.writeString(mSummary);
        dest.writeString(mIcon);
        dest.writeDouble(mTemperatureMax);
        dest.writeString(mTimezone);

    }

    private Day(Parcel in){
        mTime = in.readLong();
        mSummary = in.readString();
        mIcon = in.readString();
        mTemperatureMax = in.readDouble();
        mTimezone  = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>(){
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

}
