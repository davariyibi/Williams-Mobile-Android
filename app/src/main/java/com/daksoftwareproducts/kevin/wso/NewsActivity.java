package com.daksoftwareproducts.kevin.wso;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    Document doc;
    String dailyUrl = "https://web.williams.edu/messages/";
    Elements links;

    private MyRecyclerViewAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        new NewsActivity.News().execute();
    }

    private class News extends AsyncTask<Void, Void, Void> {
        List<News_Item> newsItems;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                doc = Jsoup.connect(dailyUrl).get();

                // div with class=box-item-div
                links = doc.select("tr");

                System.out.println("\nBox Items : " + links.size());

                Elements headings = new Elements();
                Elements information = new Elements();
                Element branch;
                int size = 0;
                for( Element item : links ) {
                    if( item.hasAttr("bgcolor")){
                        branch = item.child(1).child(1);
                        // Title
                        headings.add(branch.child(0));
                        //System.out.println(item.child(1).child(1).child(0).toString());
                        // Description (possibly with link)
                        information.add(branch.child(1));
                        //System.out.println(item.child(1).child(1).child(1).toString());
                        size++;
                    }
                }

                //System.out.println("Headings : " + headings.toString());
                //System.out.println("Information : " + information.toString());

                int i;

                newsItems = new ArrayList<>();
                for( i = 0; i < size; i++ ){
                    News_Item newsItem = new News_Item();
                    newsItem.setTitle(headings.get(i).text());
                    newsItem.setDescription(information.get(i).text());
                    newsItems.add(newsItem);
                }


            } catch (Exception e) {
                System.out.println("\nERROR\n");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
            newsAdapter = new MyRecyclerViewAdapter(recyclerView,newsItems,NewsActivity.this);
            recyclerView.setAdapter(newsAdapter);
            recyclerView.offsetTopAndBottom(100);

            /*
            //set load more listener for the RecyclerView adapter
            newsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (newsItems.size() <= 20) {
                        newsItems.add(null);
                        newsAdapter.notifyItemInserted(newsItems.size() - 1);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                newsItems.remove(newsItems.size() - 1);
                                newsAdapter.notifyItemRemoved(newsItems.size());

                                newsAdapter.notifyDataSetChanged();
                                newsAdapter.setLoaded();
                            }
                        }, 5000);
                    } else {
                        Toast.makeText(HomeActivity.this, "Loading data completed", Toast.LENGTH_SHORT).show();
                    }

                }
            });*/
        }
    } // End of News method
}


