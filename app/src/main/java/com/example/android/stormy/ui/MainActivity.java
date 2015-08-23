package com.example.android.stormy.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.stormy.R;
import com.example.android.stormy.weather.Current;
import com.example.android.stormy.weather.Day;
import com.example.android.stormy.weather.Forecast;
import com.example.android.stormy.weather.Hour;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Current mCurrent;
    private Forecast mForecast;
    public static final String DAILY_FORECAST = "DAILY_FORECAST";

    @Bind(R.id.time_label)
    TextView mTimeLabel;
    @Bind(R.id.temperature_label)
    TextView mTemperatureLabel;
    @Bind(R.id.humidity_value)
    TextView mHumidityValue;
    @Bind(R.id.precip_value)
    TextView mPrecipValue;
    @Bind(R.id.summary_label)
    TextView mSummaryLabel;
    @Bind(R.id.icon_imageview)
    ImageView mIconImageView;
    @Bind(R.id.location_label)
    TextView mLocationLabel;
    @Bind(R.id.refresh_button)
    ImageView mRefreshButton;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.daily_button)
    Button mDailyButton;
    @Bind(R.id.main_layout)
    RelativeLayout mMainLayout;

    //Hola

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE);

        //Method call to start running the comunication with the api
        getForecast();
        Log.d(TAG, "Code is running");

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast();
            }
        });

        //This code below is used to change the background of our app depending what time is it
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        //int minute = cal.get(Calendar.MINUTE);
        //int second = cal.get(Calendar.SECOND);
        Log.i(TAG, hour + "");

        if (hour >= 6 && hour < 12) {
            mMainLayout.setBackgroundResource(R.drawable.bg_gradient_morning);
        } else if (hour >= 12 && hour < 19) {
            mMainLayout.setBackgroundResource(R.drawable.bg_gradient_evening);
        } else {
            mMainLayout.setBackgroundResource(R.drawable.bg_gradient_night);
        }
    }

    //Metodo que consigue la informacion de la api de forecast
    private void getForecast() {
        String apiKey = "6bdeb587cb42fa93bc6d74b77c56cb21";
        double latitude = 8.983333;//37.8267;
        double longitude = -79.516670;//-122.423;
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude +
                "," + longitude;

        //Se hace una condicion para ver si la red esta disponible
        if (ifNetworkAvailable()) {

            toggleRefresh();

            //Inicia la comunicacion
            OkHttpClient client = new OkHttpClient();//Inicia el cliente
            Request request = new Request.Builder().url(forecastUrl).build();//construye el pedido

            Call call = client.newCall(request);//La llamada
            //Comienza a buscar la informacion en un thread diferent(no el principal)
            call.enqueue(new Callback() {

                //Si falla la llamdda
                @Override
                public void onFailure(Request request, IOException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                //La la respuesta se da
                @Override
                public void onResponse(Response response) throws IOException {

                    /*Si la respuesta es correcta se llama al thread principal para que haga un
                    * cambio en la interface(solamente se puede modificar la interface en el thread
                    * principal de la aplicacion)*/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {

                        /*La respuesta que manda la pagina*/
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecast(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught", e);
                    }
                }
            });
        } else {

            //Este codigo corre si no hay red
            NetworkUnavailableDialogFragment dialog = new NetworkUnavailableDialogFragment();
            dialog.show(getFragmentManager(), "network_unavailable");
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshButton.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {

        Current current = mForecast.getCurrent();

        mTemperatureLabel.setText(current.getTemperature() + "");
        mSummaryLabel.setText(current.getSummary());
        mTimeLabel.setText(current.getFormattedTime());
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mLocationLabel.setText(current.getTimeZone());
        mIconImageView.setImageResource(current.getIconId());

    }

    private Forecast parseForecast(String jsonData) throws JSONException {

        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));

        return forecast;
    }

    private Day[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for (int i = 0; i < data.length(); i++) {

            JSONObject jsonDay = data.getJSONObject(i);

            Day day = new Day();
            day.setTime(jsonDay.getLong("time"));
            day.setIcon(jsonDay.getString("icon"));
            day.setSummary(jsonDay.getString("summary"));
            day.setTimezone(timezone);
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));

            days[i] = day;

        }

        return days;

    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");


        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for (int i = 0; i < data.length(); i++) {

            JSONObject jsonHour = data.getJSONObject(i);

            Hour hour = new Hour();

            hour.setTimezone(timezone);
            hour.setTime(jsonHour.getLong("time"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setSummary(jsonHour.getString("summary"));
            hour.setIcon(jsonHour.getString("icon"));

            hours[i] = hour;
        }

        return hours;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {

        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject currently = forecast.getJSONObject("currently");
        Current current = new Current();
        current.setTime(currently.getLong("time"));
        current.setHumidity(currently.getDouble("humidity"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setIcon(currently.getString("icon"));
        current.setTimeZone(timezone);
        Log.d(TAG, current.getFormattedTime());
        Log.i(TAG, "The place is " + timezone);
        return current;
    }

    //Este codigo revisa si hay red
    private boolean ifNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isAvailable()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    public void startDailyActivity(View view) {
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDailyForecast());
        startActivity(intent);

    }

}
