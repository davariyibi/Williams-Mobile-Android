package com.daksoftwareproducts.kevin.wso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ProfileActivity extends AppCompatActivity {

    TextView nameView, unixView, addressView;
    HttpsObject httpsObject;
    String referer;
    String url;
    HttpsURLConnection connection;
    String html;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView imageView = (ImageView) findViewById(R.id.profileImage);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
//        TextView textView = (TextView) findViewById(R.id.profileName);

        /*
        String name = getIntent().getStringExtra("name");
        nameView = (TextView) findViewById(R.id.txt_name);
        nameView.setText(name);
        String unix = getIntent().getStringExtra("unix");
        unixView = (TextView) findViewById(R.id.txt_unix);
        unixView.setText(unix);
        addressView = (TextView) findViewById(R.id.txt_address);
        */


        url = getIntent().getStringExtra("link");
        System.out.println("HTML: "+url);
        httpsObject = (HttpsObject) getIntent().getSerializableExtra("httpsObject");
        referer = getIntent().getStringExtra("url");
        System.out.println("Referer: "+referer);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //new HTMLParser().execute();
        try {
            URL obj = new URL(url);
            connection = (HttpsURLConnection) obj.openConnection();
            //httpsObject.openConnection(connection,"GET",referer);
            //System.out.println(httpsObject.getPageContent(connection,url));
            httpsObject.openConnection(connection,"GET",referer);
            System.out.println("POST Response : ");

            // This is what we want to parse
            //System.out.println( httpsObject.printResponse(connection) );
            html = httpsObject.printResponse(connection);

            //connection.disconnect();

            Document doc = Jsoup.parse(html);

            Elements picInfo = doc.getElementsByClass("picture");
            String pic = picInfo.first().child(0).toString();
            String picLink = pic.substring(pic.indexOf("\"") + 1, pic.indexOf("\"", pic.indexOf("\"") + 1));

            URL picUrl = new URL("https://wso.williams.edu" + picLink);
            HttpURLConnection picConnection = (HttpURLConnection) picUrl.openConnection();
            picConnection.setDoInput(true);
            picConnection.connect();
            InputStream input = picConnection.getInputStream();
            Bitmap picBitmap = BitmapFactory.decodeStream(input);
//            imageView.setImageBitmap(BitmapFactory.decodeByteArray(currentWeather.iconData,0,currentWeather.iconData.length));
            imageView.setImageBitmap(picBitmap);

                Elements information = doc.getElementsByClass("info");

                int i;
                int j = 0; // For when Kevin wants to add "pronouns"
                Element item = information.first();
                int size = item.childNodeSize() - 1;
                String text;
                for( i = 0; i < size; i++ ){
                    TextView label = new TextView(this);
                    String line = item.child(i).text();
                    label.setText(line);
                    System.out.println("Beep: "+ line);
                    if (i == 0){
                        label.setTextColor(Color.parseColor("#512698"));
                        label.setTextSize(18);
//                        textView.setText(line);
                    } else {
                        label.setAllCaps(true);
//                        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/DroidSansFallback.ttf");
//                        label.setTypeface(tf,1);
                        label.setTextColor(Color.BLACK);
                        if (i == 1 || i % 3 == j) {
                            label.setTextColor(Color.parseColor("#757575"));
                            label.setTextSize(10);
                        } else if (i == 2 && line != "") {
                            System.out.println("Beeeeeep: "+ line + ".");
                            j++;
                            label.setTextSize(10);
                        } else if (i % 3 == 2 + j) {
                            label.setTextSize(8);
                        } else {
                            label.setTextSize(12);
                        }
                    }

                    label.setId(i);
                    linearLayout.addView(label);
                    //System.out.println(item.child(i).toString());
                }

                /*
                Element name = information.first().child(0);
                System.out.println("Name: "+name.text());

                Element status = information.first().child(1);
                System.out.println("Status: "+status.text());

                Element pronouns = information.first().child(2);
                System.out.println(pronouns.text());

                System.out.println("Test case: "+information.first().child(7).text());
                */
            /*
            Element unix = information.first().child(5);
            System.out.println("Unix: "+unix.text());

            Element SUBox = information.first().child(8);
            System.out.println("SUBox: "+SUBox.text());

            Element room = information.first().child(11);
            System.out.println("Room: "+room.text());
            */
        } catch (Exception e) {
            e.printStackTrace();
        }


    } // end of onCreate method

    // Title AsyncTask
    private class HTMLParser extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL obj = new URL(url);
                connection = (HttpsURLConnection) obj.openConnection();
                //httpsObject.openConnection(connection,"GET",referer);
                //System.out.println(httpsObject.getPageContent(connection,url));
                httpsObject.openConnection(connection,"GET",referer);
                System.out.println("POST Response : ");

                // This is what we want to parse
                //System.out.println( httpsObject.printResponse(connection) );
                html = httpsObject.printResponse(connection);

                //connection.disconnect();
                /*
                Document doc = Jsoup.parse(html);

                Elements information = doc.getElementsByClass("info");

                int i;
                Element item = information.first();
                int size = item.childNodeSize() - 1;
                for( i = 0; i < size; i++ ){
                    TextView label = new TextView();
                    label.setText(item.child(i).text());
                    //label.setId(i + 5);
                    linearLayout.addView(label);
                    //System.out.println(item.child(i).toString());
                }
                */
                /*
                Element name = information.first().child(0);
                System.out.println("Name: "+name.text());

                Element status = information.first().child(1);
                System.out.println("Status: "+status.text());

                Element pronouns = information.first().child(2);
                System.out.println(pronouns.text());

                System.out.println("Test case: "+information.first().child(7).text());
                */
            /*
            Element unix = information.first().child(5);
            System.out.println("Unix: "+unix.text());

            Element SUBox = information.first().child(8);
            System.out.println("SUBox: "+SUBox.text());

            Element room = information.first().child(11);
            System.out.println("Room: "+room.text());
            */
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Do something

        }
    } // End of FacebookSearchTask method

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