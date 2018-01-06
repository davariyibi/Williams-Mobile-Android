package com.daksoftwareproducts.kevin.wso;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.json.JSONException;


public class ForecastActivity extends AppCompatActivity {

    TextView locationName;
    private String city;
    private String lang;
    private String fdm;

    private MyRecyclerForecastAdapter weatherAdapter;
    ProgressDialog fcProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        city = getIntent().getStringExtra("city");
        lang = getIntent().getStringExtra("lang");
        fdm = getIntent().getStringExtra("fdn");

        locationName = (TextView) findViewById(R.id.locationName);
        locationName.setText(city);

        new ForecastTask().execute();
    }

    // Title AsyncTask
    private class ForecastTask extends AsyncTask<Void, Void, Void> {
        WeatherForecast forecast = new WeatherForecast();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            fcProgressDialog = new ProgressDialog(ForecastActivity.this);
//            fcProgressDialog.setTitle("Forecast for " + city);
//            fcProgressDialog.setMessage("Loading...");
//            fcProgressDialog.setIndeterminate(false);
//            fcProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String data = ( (new WeatherHttpClient()).getForecastWeatherData(city, lang, fdm));

            try {
                forecast = JSONWeatherParser.getForecastWeather(data);
                // Let's retrieve the icon
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_forecast);
            recyclerView.setLayoutManager(new LinearLayoutManager(ForecastActivity.this));
            weatherAdapter = new MyRecyclerForecastAdapter(recyclerView,forecast.get(),ForecastActivity.this);
            recyclerView.setAdapter(weatherAdapter);
            recyclerView.offsetTopAndBottom(100);
        }
    }
}
