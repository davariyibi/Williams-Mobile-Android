package com.daksoftwareproducts.kevin.wso;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class HomeActivity extends AppCompatActivity  {

    private static String forecastDaysNum = "3";

    String city;
    String lang;

    private HttpsURLConnection connection;
    private HttpsObject httpsObject;

    Button faceSearch, factSearch;

    Button forecastPage, diningPage, newsPage, mapPage, laundryPage, facebookPage, factrackPage;

    String dailyUrl, diningUrl, url, weatherUrl;
    EditText searchFB, searchFT;

    String html;
    String newUrl;
    ProgressDialog mProgressDialog;
    Document doc;
    Elements links;

    private MyRecyclerForecastAdapter weatherAdapter;

    WeatherForecast forecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        forecastPage = (Button) findViewById(R.id.forecast);
        diningPage = (Button) findViewById(R.id.diningButton);
        newsPage = (Button) findViewById(R.id.newsButton);
        mapPage = (Button) findViewById(R.id.mapButton);
        laundryPage = (Button) findViewById(R.id.laundryButton);
        facebookPage = (Button) findViewById(R.id.facebookButton);
        factrackPage = (Button) findViewById(R.id.factrackButton);

        faceSearch = (Button) findViewById(R.id.faceButton);
        factSearch = (Button) findViewById(R.id.factButton);

        searchFB = (EditText) findViewById(R.id.searchfb);
        searchFT = (EditText) findViewById(R.id.searchft);

        city = "Williamstown,USA";
        lang = "en";

        diningUrl = "https://dining.williams.edu/hours-venues/";
        dailyUrl = "https://web.williams.edu/messages/";

        url = getIntent().getStringExtra("newUrl");
        System.out.println("Starting URL: " + url);
        httpsObject = (HttpsObject) getIntent().getSerializableExtra("httpsObject");
        html = getIntent().getStringExtra("html");
        System.out.println(html);

        new JSONWeatherTask().execute();
        new JSONForecastWeatherTask().execute();
        new CurrentWeatherTask().execute();


        // Capture button click
        forecastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchForecastActivity();
            }
        });

        diningPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDiningActivity();
            }
        });

        newsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNewsActivity();
            }
        });

        mapPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMapActivity();
            }
        });

        laundryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLaundryActivity();
            }
        });

        facebookPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFacebookActivity();
            }
        });

        factrackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFactrakActivity();
            }
        });


    } // end of onCreate method


    private void launchForecastActivity(){
        Intent intent = new Intent(HomeActivity.this, ForecastActivity.class);
        intent.putExtra("city",city);
        intent.putExtra("lang",lang);
        intent.putExtra("fdn",forecastDaysNum);
        startActivity(intent);
    }

    private void launchDiningActivity(){
        Intent intent = new Intent(HomeActivity.this, DiningActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("httpsObject",httpsObject);
        intent.putExtra("id",3256);
        intent.putExtra("name","Driscoll");
        startActivity(intent);
    }

    private void launchMapActivity(){
        Intent intent = new Intent(HomeActivity.this, MapActivity.class);
        startActivity(intent);
    }

    private void launchLaundryActivity(){
        Intent intent = new Intent(HomeActivity.this, LaundryActivity.class);
        startActivity(intent);
    }

    private void launchNewsActivity(){
        Intent intent = new Intent(HomeActivity.this, NewsActivity.class);
        startActivity(intent);
    }

    private void launchFacebookActivity(){
        Intent intent = new Intent(this, FacebookActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("httpsObject",httpsObject);
        intent.putExtra("html",html);
        startActivity(intent);
    }


    private void launchFactrakActivity(){
        Intent intent = new Intent(this, FactrakActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("httpsObject",httpsObject);
        startActivity(intent);
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
                weather.iconData = ( client.getImage(weather.currentCondition.getIcon()));
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
                //imgView.setImageBitmap(img);
            }
/*
            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            temp.setText("" + Math.round((weather.temperature.getTemp() - 275.15)));
            condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
*/
        }



    }


    private class JSONForecastWeatherTask extends AsyncTask<String, Void, WeatherForecast> {

        @Override
        protected WeatherForecast doInBackground(String... params) {

            String data = ( (new WeatherHttpClient()).getForecastWeatherData(city, lang, forecastDaysNum));
            WeatherForecast forecast = new WeatherForecast();
            try {
                forecast = JSONWeatherParser.getForecastWeather(data);
                System.out.println("Weather ["+forecast+"]");
                // Let's retrieve the icon
                //weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return forecast;
        }

        @Override
        protected void onPostExecute(WeatherForecast forecastWeather) {
            super.onPostExecute(forecastWeather);

            DailyForecastPageAdapter adapter = new DailyForecastPageAdapter(Integer.parseInt(forecastDaysNum), getSupportFragmentManager(), forecastWeather);
            //pager.setAdapter(adapter);
        }
    }

    // Title AsyncTask
    private class CurrentWeatherTask extends AsyncTask<Void, Void, Void> {
        Weather currentWeather = new Weather();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String data = ( (new WeatherHttpClient()).getWeatherData(city, lang));

            try {
                currentWeather = JSONWeatherParser.getWeather(data);
                // Let's retrieve the icon
                currentWeather.iconData = ( (new WeatherHttpClient()).getImage(currentWeather.currentCondition.getIcon()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            ImageView weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
            TextView temp = (TextView) findViewById(R.id.temperature);
            TextView loc = (TextView) findViewById(R.id.location);
//            TextView cond = (TextView) findViewById(R.id.condition);
            TextView descr = (TextView) findViewById(R.id.description);
//            TextView hum = (TextView) findViewById(R.id.humidity);
//            TextView press = (TextView) findViewById(R.id.pressure);
//            TextView wind = (TextView) findViewById(R.id.wind);
//            TextView windDeg = (TextView) findViewById(R.id.windDegree);

            weatherIcon.setImageBitmap(BitmapFactory.decodeByteArray(currentWeather.iconData,0,currentWeather.iconData.length));
            Long fTemp = Math.round(((currentWeather.temperature.getTemp() * 9) / 5) - 459.67);
            temp.setText(fTemp.toString() + "ºF");
            loc.setText(" in " + city.substring(0, city.indexOf(",")));
            String descrStr = currentWeather.currentCondition.getDescr();
//            cond.setText(currentWeather.currentCondition.getCondition());
            descr.setText(descrStr.substring(0, 1).toUpperCase() + descrStr.substring(1));
//            hum.setText("Humidity: " + currentWeather.currentCondition.getHumidity() + "%");
//            press.setText("Pressure: " + currentWeather.currentCondition.getPressure() + " hPa");
//            wind.setText("Wind: " + currentWeather.wind.getSpeed() + " mph at " + currentWeather.wind.getDeg() + "°");
//            windDeg.setText("Wind Direction: " + );
        }
    } // End of CurrentWeatherTask method


    // Title AsyncTask
    private class WSO extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                doc = Jsoup.connect(url).get();
                System.out.println("\nSUCCESS\n");

                // div with class=box-item-div
                links = doc.select("a[href]");

                //System.out.println("\nBox Items : " + links.size());

            } catch (Exception e) {
                System.out.println("\nERROR\n");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Do something
        }
    } // End of WSO method

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wso_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home:
                new Home().execute();
                return true;
            case R.id.facebook:
                new Facebook().execute();
                return true;
            case R.id.factrak:
                new Factrak().execute();
            case R.id.dormtrak:
                // Code here
                return true;
            case R.id.listserv:
                // Code here
                return true;
            case R.id.wiki:
                // Code here
            case R.id.about:
                // Code here
            case R.id.logout:
                // Code here
                new Logout().execute();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Title AsyncTask
    private class Home extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(HomeActivity.this);
            mProgressDialog.setTitle("Williams Mobile");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document document = Jsoup.connect(url).get();
                // Get the html document title
                title = document.title();

                weatherUrl = "http://api.openweathermap.org/data/2.5/weather?q=Williamstown,USA&APPID=5001beb19a0b0fa04fb3aa969e984f68";

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            setTitle(title);
            mProgressDialog.dismiss();
        }
    } // End of Home method

    // Title AsyncTask
    private class Facebook extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(HomeActivity.this);
            mProgressDialog.setTitle("Williams Facebook");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            for (Element link : links) {
                if(link.attr("abs:href").toLowerCase().contains("facebook")) {
                    // Do something with the Williams facebook link
                }
            }
            mProgressDialog.dismiss();
        }
    } // End of Facebook method

    // Title AsyncTask
    private class Factrak extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(HomeActivity.this);
            mProgressDialog.setTitle("Williams Factrak");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            for (Element link : links) {
                if(link.attr("abs:href").toLowerCase().contains("factrak")) {
                    // Do something with the Williams factrak link
                }
            }
            mProgressDialog.dismiss();
        }
    } // End of Facttrak method

    // Title AsyncTask
    private class Logout extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(HomeActivity.this);
            mProgressDialog.setTitle("Logging Out");
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String newUrl = "https://wso.williams.edu/account/logout";
            try {
                URL obj = new URL(newUrl);
                connection = (HttpsURLConnection) obj.openConnection();
                httpsObject.openConnection(connection, "GET", url);
                //connection.setInstanceFollowRedirects(false);
                System.out.println("Printing Response Header : ");

                Map<String, List<String>> header = connection.getHeaderFields();
                for ( Map.Entry<String, List<String>> entry : header.entrySet()) {
                    System.out.println("Key : " + entry + " ,Value : " + entry.getValue());
                }

                System.out.println("POST Response : ");
                System.out.println(httpsObject.printResponse(connection));
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            System.exit(1);
        }
    } // End of Logout method

/*
    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
        Runtime.getRuntime().gc();
        System.gc();
    }
*/
}
