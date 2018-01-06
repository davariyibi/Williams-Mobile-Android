package com.daksoftwareproducts.kevin.wso;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class FactrakActivity extends AppCompatActivity {

    String url;
    private HttpsURLConnection connection;
    HttpsObject httpsObject;
    Button factSearch;
    EditText searchFT;
    private MyRecyclerProfAdapter professorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factrak);

        factSearch = (Button) findViewById(R.id.factButton);
        searchFT = (EditText) findViewById(R.id.searchft);

        url = getIntent().getStringExtra("url");
        System.out.println(url);
        httpsObject = (HttpsObject) getIntent().getSerializableExtra("httpsObject");

        factSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FactrakActivity.FactrakSearchTask().execute();
            }
        });

    } // end of onCreate method

    private Void professorParser(String data) throws JSONException {
        JSONArray jArr = new JSONArray(data);
        List<Professor_Item> professorItems = new ArrayList<>();
        for( int i = 0; i < jArr.length(); i++ ){
            JSONObject element = jArr.getJSONObject(i);
            Professor_Item professorItem = new Professor_Item();
            professorItem.setName(element.getString("name"));
            professorItem.setId(element.getString("id"));
            professorItems.add(professorItem);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(FactrakActivity.this));
        professorAdapter = new MyRecyclerProfAdapter(recyclerView,professorItems,FactrakActivity.this,httpsObject,url);
        recyclerView.setAdapter(professorAdapter);
        recyclerView.offsetTopAndBottom(100);
        return null;
    }

    // Title AsyncTask
    private class FactrakSearchTask extends AsyncTask<Void, Void, Void> {

        String results;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                results = getPageContent();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Do something
            try {
                professorParser(results);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    } // End of FactrakSearchTask method


    private String getPageContent() throws Exception {
        url = "https://wso.williams.edu/factrak/autocomplete.json?q=";
        String request = searchFT.getText().toString();
        request = request.replaceAll(" ","%20");
        url = url + request;
        URL obj = new URL(url);
        connection = (HttpsURLConnection) obj.openConnection();
        String content = httpsObject.getPageContent( connection, url );
        return content;
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