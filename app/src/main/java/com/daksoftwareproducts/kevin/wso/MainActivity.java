package com.daksoftwareproducts.kevin.wso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    String url;
    ProgressDialog mProgressDialog;
    TabHost tabHost;
    Document doc;
    Elements links;

    TextView d1,d2,d3,d4,d5,dmore,a1,a2,a3,a4,a5,amore,e1,e2,e3,e4,e5,emore,l1,l2,l3,l4,l5,lmore,j1,j2,j3,j4,j5,jmore,rmore;

    private List<String> cookies;
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = "https://wso.williams.edu/";
        //url = getIntent().getStringExtra("newUrl");
        //setCookies(getIntent().getStringArrayListExtra("cookies"));

        new Home().execute();
        new Test().execute();

        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        // Discussions tab
        TabHost.TabSpec spec = host.newTabSpec("Discussions");
        spec.setContent(R.id.discussions);
        spec.setIndicator("Discussions");
        host.addTab(spec);

        //Announcements tab
        spec = host.newTabSpec("Announcements");
        spec.setContent(R.id.announcements);
        spec.setIndicator("Announcements");
        host.addTab(spec);

        //Exchanges tab
        spec = host.newTabSpec("Exchanges");
        spec.setContent(R.id.exchanges);
        spec.setIndicator("Exchanges");
        host.addTab(spec);

        //Lost & Found tab
        spec = host.newTabSpec("Lost & Found");
        spec.setContent(R.id.lostfound);
        spec.setIndicator("Lost & Found");
        host.addTab(spec);

        //Jobs tab
        spec = host.newTabSpec("Jobs");
        spec.setContent(R.id.jobs);
        spec.setIndicator("Jobs");
        host.addTab(spec);

        //Rides tab
        spec = host.newTabSpec("Rides");
        spec.setContent(R.id.rides);
        spec.setIndicator("Rides");
        host.addTab(spec);


    } // end of onCreate method


    // Title AsyncTask
    private class Test extends AsyncTask<Void, Void, Void> {
        Elements discussions = new Elements();
        Elements announcements = new Elements();
        Elements exchanges = new Elements();
        Elements lostFound = new Elements();
        Elements jobs = new Elements();
        Elements rides = new Elements();

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

                System.out.println("\nBox Items : " + links.size());
                for( Element item : links ) {
                    if(item.attr("abs:href").toLowerCase().contains("discussions")) {
                        discussions.add(item);
                    } else if (item.attr("abs:href").toLowerCase().contains("announcements")) {
                        announcements.add(item);
                    } else if (item.attr("abs:href").toLowerCase().contains("exchanges")) {
                        exchanges.add(item);
                    } else if (item.attr("abs:href").toLowerCase().contains("lost_and_found")) {
                        lostFound.add(item);
                    } else if (item.attr("abs:href").toLowerCase().contains("jobs")) {
                        jobs.add(item);
                    } else if (item.attr("abs:href").toLowerCase().contains("rides")) {
                        rides.add(item);
                    } else {
                        // Do something (or nothing) with the other links
                    }
                }

                /*
                System.out.println("Discussions : " + discussions.toString());
                System.out.println("Announcements : " + announcements.toString() );
                System.out.println("Exchanges : " + exchanges.toString() );
                System.out.println("Lost and Found : " + lostFound.toString() );
                System.out.println("Jobs : " + jobs.toString() );
                System.out.println("Rides : " + rides.toString() );
                */

            } catch (Exception e) {
                System.out.println("\nERROR\n");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            d1 = (TextView) findViewById(R.id.d1);
            d2 = (TextView) findViewById(R.id.d2);
            d3 = (TextView) findViewById(R.id.d3);
            d4 = (TextView) findViewById(R.id.d4);
            d5 = (TextView) findViewById(R.id.d5);
            dmore = (TextView) findViewById(R.id.dmore);
            a1 = (TextView) findViewById(R.id.a1);
            a2 = (TextView) findViewById(R.id.a2);
            a3 = (TextView) findViewById(R.id.a3);
            a4 = (TextView) findViewById(R.id.a4);
            a5 = (TextView) findViewById(R.id.a5);
            amore = (TextView) findViewById(R.id.amore);
            e1 = (TextView) findViewById(R.id.e1);
            e2 = (TextView) findViewById(R.id.e2);
            e3 = (TextView) findViewById(R.id.e3);
            e4 = (TextView) findViewById(R.id.e4);
            e5 = (TextView) findViewById(R.id.e5);
            emore = (TextView) findViewById(R.id.emore);
            l1 = (TextView) findViewById(R.id.l1);
            l2 = (TextView) findViewById(R.id.l2);
            l3 = (TextView) findViewById(R.id.l3);
            l4 = (TextView) findViewById(R.id.l4);
            l5 = (TextView) findViewById(R.id.l5);
            lmore = (TextView) findViewById(R.id.lmore);
            j1 = (TextView) findViewById(R.id.j1);
            j2 = (TextView) findViewById(R.id.j2);
            j3 = (TextView) findViewById(R.id.j3);
            j4 = (TextView) findViewById(R.id.j4);
            j5 = (TextView) findViewById(R.id.j5);
            jmore = (TextView) findViewById(R.id.jmore);
            rmore = (TextView) findViewById(R.id.rmore);

            d1.setText(discussions.get(1).text());
            d2.setText(discussions.get(2).text());
            d3.setText(discussions.get(3).text());
            d4.setText(discussions.get(4).text());
            d5.setText(discussions.get(5).text());
            dmore.setText("More " + discussions.get(0).text());
            a1.setText(announcements.get(1).text());
            a2.setText(announcements.get(2).text());
            a3.setText(announcements.get(3).text());
            a4.setText(announcements.get(4).text());
            a5.setText(announcements.get(5).text());
            amore.setText("More " + announcements.get(0).text());
            e1.setText(exchanges.get(1).text());
            e2.setText(exchanges.get(2).text());
            e3.setText(exchanges.get(3).text());
            e4.setText(exchanges.get(4).text());
            e5.setText(exchanges.get(5).text());
            emore.setText("More " + exchanges.get(0).text());
            l1.setText(lostFound.get(1).text());
            l2.setText(lostFound.get(2).text());
            l3.setText(lostFound.get(3).text());
            l4.setText(lostFound.get(4).text());
            l5.setText(lostFound.get(5).text());
            lmore.setText("More " + lostFound.get(0).text());
            j1.setText(jobs.get(1).text());
            j2.setText(jobs.get(2).text());
            j3.setText(jobs.get(3).text());
            j4.setText(jobs.get(4).text());
            j5.setText(jobs.get(5).text());
            jmore.setText("More " + jobs.get(0).text());
            rmore.setText("More " + rides.get(0).text());

        }
    } // End of login method


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
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("WSO Home");
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            TextView txttitle = (TextView) findViewById(R.id.titletxt);
            txttitle.setText(title);
            setTitle(title);
            mProgressDialog.dismiss();
        }
    } // End of Home method



    // Title AsyncTask
    private class Facebook extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
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
                    TextView txttitle = (TextView) findViewById(R.id.titletxt);
                    txttitle.setText(link.attr("abs:href"));
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
            mProgressDialog = new ProgressDialog(MainActivity.this);
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
                    TextView txttitle = (TextView) findViewById(R.id.titletxt);
                    txttitle.setText(link.attr("abs:href"));
                }
            }
            mProgressDialog.dismiss();
        }
    } // End of Facttrak method

    public void setCookies(List<String> cookies) {
        this.cookies = cookies;
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