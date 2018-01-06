package com.daksoftwareproducts.kevin.wso;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class FacebookActivity extends AppCompatActivity {

    String url, html, newUrl, results;
    boolean redirect = false;
    EditText searchFB;
    Button faceSearch;
    HttpsObject httpsObject;
    private HttpsURLConnection connection;
    private MyRecyclerProfileAdapter profileAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        url = getIntent().getStringExtra("url");
        System.out.println(url);
        httpsObject = (HttpsObject) getIntent().getSerializableExtra("httpsObject");
        System.out.println(results);
        html = getIntent().getStringExtra("html");

        searchFB = (EditText) findViewById(R.id.searchfb);
        faceSearch = (Button) findViewById(R.id.faceButton);

        faceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FacebookActivity.FacebookSearchTask().execute();
            }
        });

    } // end of onCreate method

    private Void profileParser(String data) throws Exception {

        List<Profile_Item> profileItems = new ArrayList<>();
        Document doc = Jsoup.parse(data);
        Elements table = doc.select("table");

        //System.out.println("Size of the table: " + table.size() );

        // We know the html result includes a table
        if( table.size() == 1 ){
            Elements body = table.select("tbody");
            //System.out.println("Number of bodies: " + body.size());
            Elements rows = body.select("tr");
            //System.out.println("Number of rows: " + rows.size());
            //System.out.println("Rows: " + rows.toString());
            //Element item = rows.first();

            /*
            // Ending URL
            System.out.println(item.getElementsByTag("td").get(0).getElementsByTag("a").attr("href"));
            // Name
            System.out.println(item.getElementsByTag("td").get(0).text());
            // Email address
            System.out.println(item.getElementsByTag("td").get(1).getElementsByTag("a").attr("href"));
            // Unix
            System.out.println(item.getElementsByTag("td").get(1).text());
            // Room/Office
            System.out.println(item.getElementsByTag("td").get(2).text());
            */

            for( Element item : rows ) {
                Profile_Item profileItem = new Profile_Item();
                profileItem.setName(item.getElementsByTag("td").get(0).text());
                profileItem.setUrl("https://wso.williams.edu" + item.getElementsByTag("td").get(0).getElementsByTag("a").attr("href") );
                profileItem.setUnix(item.getElementsByTag("td").get(1).text());
                profileItem.setEmail(item.getElementsByTag("td").get(1).getElementsByTag("a").attr("href"));
                profileItem.setAddress(item.getElementsByTag("td").get(2).text());
                profileItems.add(profileItem);
            }
            /*
        for( int j = 0; j < profileItems.size(); j++ ){
            System.out.println(profileItems.get(j).getToString());
        }
        */

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(FacebookActivity.this));
            profileAdapter = new MyRecyclerProfileAdapter(recyclerView,profileItems,FacebookActivity.this,httpsObject,url);
            recyclerView.setAdapter(profileAdapter);
            recyclerView.offsetTopAndBottom(100);

        }

        // implement the situation for when there is no table
        // The source of the WSO picture
        //Elements picSrc = doc.getElementsByClass("picture");
        Elements information = doc.getElementsByClass("info");

        if( information.size() == 1 ){
            //System.out.println(information.size());
            //System.out.println(information.toString());

            Element name = information.first().child(0);
            System.out.println("Name: "+name.text());

            Element status = information.first().child(1);
            System.out.println("Status: "+status.text());

            Element pronouns = information.first().child(2);
            System.out.println(pronouns.text());

            System.out.println("Test case: "+information.first().child(7).text());
            /*
            Element unix = information.first().child(5);
            System.out.println("Unix: "+unix.text());

            Element SUBox = information.first().child(8);
            System.out.println("SUBox: "+SUBox.text());

            Element room = information.first().child(11);
            System.out.println("Room: "+room.text());
            */


//            Intent intent = new Intent(this,ProfileActivity.class);
//            intent.putExtra("name",name.text());
//            intent.putExtra("unix",userViewHolder.unix.getText());
//            intent.putExtra("link",profile_item.getUrl());
//            intent.putExtra("httpsObject",httpsObject);
//            intent.putExtra("url",url);
//            this.startActivity(intent);

        }

        Elements thirds = doc.getElementsByClass("two-third");
        System.out.println("Thirds size: " + thirds.size());

        if( thirds.size() > 0 ){
            for( Element item : thirds ){
                Profile_Item profileItem = new Profile_Item();
                profileItem.setName( item.child(0).text() );
                profileItem.setUrl( "https://wso.williams.edu" + item.child(0).getElementsByTag("a").attr("href") );
                profileItem.setUnix( item.child(1).getElementsByClass("list-contents").first().text() );
                profileItem.setEmail( item.child(1).getElementsByClass("list-contents").first().getElementsByTag("a").attr("href") );
//                profileItem.setPic( profileItem.getUrl() );
                profileItems.add( profileItem );
            }


        for( int j = 0; j < profileItems.size(); j++ ){
            System.out.println(profileItems.get(j).getToString());
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(FacebookActivity.this));
        profileAdapter = new MyRecyclerProfileAdapter(recyclerView,profileItems,FacebookActivity.this,httpsObject,url);
        recyclerView.setAdapter(profileAdapter);
        recyclerView.offsetTopAndBottom(100);
        }

        return null;
    }


    private String sendFacebookPost(String postParams) throws Exception {
        url = "https://wso.williams.edu/facebook";
        URL obj = new URL(url);
        connection = (HttpsURLConnection) obj.openConnection();

        // Param: HttpsURLConnection, RequestMethod String, Referer String, Post Parameters String
        httpsObject.sendPost(connection,"POST","https://wso.williams.edu/",postParams);

        int responseCode = httpsObject.getResponseCode();
        // responseCode = connection.getResponseCode();

        if (responseCode != connection.HTTP_OK) {
            if (responseCode == connection.HTTP_MOVED_TEMP || responseCode == connection.HTTP_MOVED_PERM
                    || responseCode == connection.HTTP_SEE_OTHER)
                redirect = true;
        }

        if (redirect) {
            newUrl = connection.getHeaderField("Location");
            System.out.println("Redirect to URL : " + newUrl);
            connection = (HttpsURLConnection) new URL(newUrl).openConnection();
            httpsObject.openConnection(connection, "GET", "https://wso.williams.edu/facebook");

            // protect against correct then incorrect login attempts
            //redirect = false;
        }
        System.out.println("Printing Response Header : ");

        Map<String, List<String>> header = connection.getHeaderFields();
        for ( Map.Entry<String, List<String>> entry : header.entrySet()) {
            System.out.println("Key : " + entry + " ,Value : " + entry.getValue());
        }

        System.out.println("POST Response : ");
        String response = httpsObject.printResponse(connection);
        System.out.println( response );
        connection.disconnect();
        return response;
    }

    // Williams Facebook search form parameters
    public String getFormParams(String html) throws UnsupportedEncodingException {

        System.out.println("Extracting form's data...");

        Document doc = Jsoup.parse(html);

        // Google form id
        Elements inputElements = doc.getElementsByTag("input");
        List<String> paramList = new ArrayList<String>();
        for (Element inputElement : inputElements) {
            String key = inputElement.attr("name");
            String value = inputElement.attr("value");

            if (key.equals("search"))
                value = searchFB.getText().toString();
            else if (key.equals("commit"))
                value = "Search";
            paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
        }

        // build parameters list
        StringBuilder result = new StringBuilder();
        for (String param : paramList) {
            if (result.length() == 0) {
                result.append(param);
            } else {
                result.append("&" + param);
            }
        }
        return result.toString();
    }


    // Title AsyncTask
    private class FacebookSearchTask extends AsyncTask<Void, Void, Void> {
        String results;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String postParams = getFormParams(html);
                results = sendFacebookPost(postParams);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Do something
            if( redirect ){
                redirect = false;
                launchProfileActivity(results);
            } else {
                try {
                    profileParser(results);
                } catch (Exception e){
                    e.printStackTrace();
                }
                //launchFBActivity(results);
            }
        }
    } // End of FacebookSearchTask method

    private void launchProfileActivity(String result){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("name","Kevin Chang");
        intent.putExtra("unix","kc13");
        intent.putExtra("link",newUrl);
        intent.putExtra("httpsObject",httpsObject);
        intent.putExtra("url",url);
        startActivity(intent);
    }

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
                return true;
            case R.id.facebook:
                return true;
            case R.id.factrak:
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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