package com.daksoftwareproducts.kevin.wso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;


/**
 * Created by Kevin on 6/21/2017.
 */

public class DayForecastFragment extends Fragment {

    private DayForecast dayForecast;
    private ImageView iconWeather;
    String city,lang;

    public void setForecast(DayForecast dayForecast) {
        this.dayForecast = dayForecast;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dayforecast_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        city = "Williamstown, USA";
        lang = "en";

        new JSONWeatherTask().execute();

        TextView weekDayView = (TextView) view.findViewById(R.id.weekday);
        TextView highTempView = (TextView) view.findViewById(R.id.highTemp);
        TextView lowTempView = (TextView) view.findViewById(R.id.lowTemp);
        weekDayView.setText( "Weekday" );
        highTempView.setText( (int) (dayForecast.forecastTemp.temp_max- 275.15) );
        lowTempView.setText( (int) (dayForecast.forecastTemp.temp_min - 275.15) );
        //descView.setText(dayForecast.weather.currentCondition.getDescr());
        iconWeather = (ImageView) view.findViewById(R.id.forCondIcon);
        // Now we retrieve the weather icon
        //new JSONIconWeatherTask().execute(new String[]{dayForecast.weather.currentCondition.getIcon()});

    }


    private class JSONIconWeatherTask extends AsyncTask<String, Void, byte[]> {

        @Override
        protected byte[] doInBackground(String... params) {
            byte[] data = null;

            try {
                // Let's retrieve the icon
                data = ( (new WeatherHttpClient()).getImage(params[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(byte[] data) {
            super.onPostExecute(data);

            if (data != null) {
                Bitmap img = BitmapFactory.decodeByteArray(data, 0, data.length);
                iconWeather.setImageBitmap(img);
            }
        }
    }


    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        WeatherHttpClient client = (WeatherHttpClient) new WeatherHttpClient();
        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( client.getWeatherData(city,lang));
            try {
                weather = JSONWeatherParser.getWeather(data);
                System.out.println("Weather ["+weather+"]");
                // Let's retrieve the icon
                //weather.iconData = ( client.getImage(weather.currentCondition.getIcon()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                iconWeather.setImageBitmap(img);
            }
/*
            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            temp.setText("" + Math.round((weather.temperature.getTemp() - 275.15)));
            condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
*/
			/*

			temp.setText("" + Math.round((weather.temperature.getTemp() - 275.15)) + "°C");
			hum.setText("" + weather.currentCondition.getHumidity() + "%");
			press.setText("" + weather.currentCondition.getPressure() + " hPa");
			windSpeed.setText("" + weather.wind.getSpeed() + " mps");
			windDeg.setText("" + weather.wind.getDeg() + "°");
			*/
        }
    }
}
