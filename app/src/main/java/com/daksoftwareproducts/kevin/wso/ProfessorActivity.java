package com.daksoftwareproducts.kevin.wso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ProfessorActivity extends AppCompatActivity {

    TextView professorName;
    HttpsObject httpsObject;
    HttpsURLConnection connection;
    String id;
    String referer;
    String url = "https://wso.williams.edu/factrak/professors/";
    private MyRecyclerReviewAdapter reviewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        String name = getIntent().getStringExtra("name");
        professorName = (TextView) findViewById(R.id.professorName);
        professorName.setText(name);
        id = getIntent().getStringExtra("id");
        httpsObject = (HttpsObject) getIntent().getSerializableExtra("httpsObject");
        referer = getIntent().getStringExtra("url");

        new ReviewParser().execute();

    } // end of onCreate method


    // Title AsyncTask
    private class ReviewParser extends AsyncTask<Void, Void, Void> {
        List<Review_Item> reviewItems;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // 1. Send a "GET" request, so that you can extract the form's data.
                url = url + id;

                URL obj = new URL(url);
                connection = (HttpsURLConnection) obj.openConnection();
                //httpsObject.openConnection(connection,"GET",referer);
                //System.out.println(httpsObject.getPageContent(connection,url));
                httpsObject.openConnection(connection,"GET",referer);
                System.out.println("POST Response : ");

                // This is what we want to parse
                //System.out.println( httpsObject.printResponse(connection) );
                String html = httpsObject.printResponse(connection);

                //connection.disconnect();

                Document doc = Jsoup.parse(html);

                // div with <div id="..." class="comment">
                Elements divisions = doc.getElementsByClass("comment");
                System.out.println("\nDivisions : " + divisions.size());

                reviewItems = new ArrayList<>();
                if( divisions.get(0).getElementsByClass("blurred").isEmpty() ) {
                    System.out.println("There is no blur");
                    for (Element item : divisions) {
                        Review_Item reviewItem = new Review_Item();
                        reviewItem.setCourseTitle(item.getElementsByTag("h1").first().text());
                        //reviewItem.setComment(item.getElementsByTag("p").first().text());
                        reviewItem.setComment(item.getElementsByClass("comment-text").first().text());
                        reviewItem.setRating(item.getElementsByTag("h1").last().text());
                        reviewItem.setDatePosted(item.getElementsByClass("comment-text").next().text());
                        reviewItems.add(reviewItem);
                    }
                } else {
                    Review_Item reviewItem = new Review_Item();
                    reviewItem.setCourseTitle("Write reviews to see reviews!");
                    reviewItem.setComment("To write a review, just go on the official WSO website, search a professor's name, and then click on the link on the professor's page to write a review!");
                    reviewItem.setRating("");
                    reviewItem.setDatePosted("");
                    reviewItems.add(reviewItem);
                }

            } catch (Exception e) {
                System.out.println("\nERROR\n");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Do something
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(ProfessorActivity.this));
            reviewAdapter = new MyRecyclerReviewAdapter(recyclerView,reviewItems,ProfessorActivity.this);
            recyclerView.setAdapter(reviewAdapter);
            recyclerView.offsetTopAndBottom(100);
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