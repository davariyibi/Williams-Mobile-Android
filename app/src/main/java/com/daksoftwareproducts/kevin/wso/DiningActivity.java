package com.daksoftwareproducts.kevin.wso;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;


public class DiningActivity extends AppCompatActivity {

    String url, newUrl;
    private HttpsURLConnection connection;
    private HttpsObject httpsObject;
    boolean redirect = false;
    String name;
    int menuOid, breakfast = 36, lunch = 42, dinner = 49;
    TextView diningHall;
    Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
    int day = calendar.get(Calendar.DAY_OF_WEEK);

    Button nextButton, prevButton;

    List<Meal_Item> mealItems = new ArrayList<>();

    private MyRecyclerMealAdapter mealAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining);

        prevButton = (Button) findViewById(R.id.previous);
        nextButton = (Button) findViewById(R.id.next);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(DiningActivity.this));

        url = getIntent().getStringExtra("newUrl");
        System.out.println("Starting URL: " + url);
        httpsObject = (HttpsObject) getIntent().getSerializableExtra("httpsObject");
        name = getIntent().getStringExtra("name");
        menuOid = getIntent().getIntExtra("id",0);

        launchTask();

        diningHall = (TextView) findViewById(R.id.dining_hall);
        diningHall.setText(name);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealItems = new ArrayList<>();
                if( name.equals("Mission") ) {
                    name = "Whitman's";
                    diningHall.setText(name);
                } else if ( name.equals("Driscoll") ) {
                    name = "Mission";
                    diningHall.setText(name);
                } else if ( name.equals("Whitman's") ) {
                    name = "Driscoll";
                    diningHall.setText(name);
                }
                launchTask();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealItems = new ArrayList<>();
                if( name.equals("Driscoll") ) {
                    name = "Whitman's";
                    diningHall.setText(name);
                } else if ( name.equals("Whitman's") ) {
                    name = "Mission";
                    diningHall.setText(name);
                } else if ( name.equals("Mission") ) {
                    name = "Driscoll";
                    diningHall.setText(name);
                }
                launchTask();
            }
        });
    }

    public void launchTask(){
        if(name.equals("Whitman's")){
            menuOid = 3271;
            breakfast = 51;
            lunch = 57;
            dinner = 64;
        } else if(name.equals("Mission")) {
            menuOid = 3272;
            breakfast = 52;
            lunch = 58;
            dinner = 65;
        } else {
            menuOid = 3256;
            breakfast = 36;
            lunch = 42;
            dinner = 49;
        }

        if( day == 1 ) {
            // Handle Sunday differently
        }

        switch (day) {
            case 3: breakfast++;    // Tuesday
                lunch++;
                dinner++;
                break;
            case 4: breakfast += 2; // Wednesday
                lunch += 2;
                dinner += 2;
                break;
            case 5: breakfast += 3; // Thursday
                lunch += 3;
                dinner += 3;
                break;
            case 6: breakfast += 4; // Friday
                lunch += 4;
                dinner += 4;
                break;
            case 7: breakfast += 5; // Saturday
                lunch += 5;
                dinner += 5;
                break;
            default:break;
        }

        breakfast = (menuOid*100) + breakfast;
        lunch = (menuOid*100) + lunch;
        dinner = (menuOid*100) + dinner;


        new DiningTask().execute(""+breakfast);
        new DiningTask().execute(""+lunch);
        new DiningTask().execute(""+dinner);
    }

    // Title AsyncTask
    private class DiningTask extends AsyncTask<String, Void, Void> {

        StringBuffer response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                url = "http://nutrition.williams.edu/NetNutrition/1/Menu/SelectMenu";
                URL obj = new URL(url);
                HttpURLConnection insecureConnection;
                insecureConnection = (HttpURLConnection) obj.openConnection();
                // Param: HttpsURLConnection, RequestMethod String, Referer String, Post Parameters String
                httpsObject.sendDiningPost(insecureConnection,"POST","http://nutrition.williams.edu/NetNutrition/1","menuOid=" + params[0]);
                int responseCode = httpsObject.getResponseCode();
                // responseCode = connection.getResponseCode();
                if (responseCode != insecureConnection.HTTP_OK) {
                    if (responseCode == insecureConnection.HTTP_MOVED_TEMP || responseCode == insecureConnection.HTTP_MOVED_PERM
                            || responseCode == insecureConnection.HTTP_SEE_OTHER)
                        redirect = true;
                }
                if (redirect) {
                    newUrl = insecureConnection.getHeaderField("Location");
                    System.out.println("Redirect to URL : " + newUrl);
                    insecureConnection = (HttpURLConnection) new URL(newUrl).openConnection();
                    //httpsObject.openConnection(connection, "GET", "https://wso.williams.edu/facebook");
                    // protect against correct then incorrect login attempts
                    //redirect = false;
                }
                System.out.println("POST Response : ");
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(insecureConnection.getInputStream()));
                String inputLine;
                response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Response: " + response);
                insecureConnection.disconnect();

                JSONObject data = new JSONObject(response.toString());
                JSONArray panelArray = data.getJSONArray("panels");

                Document doc = Jsoup.parse(panelArray.getJSONObject(0).getString("html"));
                Elements table = doc.getElementsByClass("cbo_nn_itemGridTable");
                //System.out.println(table.toString());
                Elements rows = table.select("tr");
                //System.out.println(rows.toString());
                for( int kaishi = 2; kaishi < rows.size(); kaishi++ ) {
                    Element row = rows.get(kaishi);
                    //System.out.println(row.toString());
                    Elements td = row.select("td");
                    if( td.size() > 1 ) {
                        Element item = td.get(1);
                        // item.text() is the name of the food
                        //System.out.println(item.text());
                        Meal_Item mealItem = new Meal_Item();
                        mealItem.setName( item.text() );
                        mealItems.add( mealItem );
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Do something
            mealAdapter = new MyRecyclerMealAdapter(recyclerView,mealItems,DiningActivity.this);
            recyclerView.setAdapter(mealAdapter);
            recyclerView.offsetTopAndBottom(100);

        }
    } // End of WhitmansTask method

}
