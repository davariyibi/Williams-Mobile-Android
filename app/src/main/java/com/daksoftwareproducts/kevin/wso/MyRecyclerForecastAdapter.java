package com.daksoftwareproducts.kevin.wso;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kevin on 6/18/2017.
 */

public class MyRecyclerForecastAdapter extends RecyclerView.Adapter {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener onLoadMoreListener;

    private Activity activity;
    private List<DayForecast> weather_items;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public MyRecyclerForecastAdapter(RecyclerView recyclerView, List<DayForecast> weather_items, Activity activity){
        this.weather_items = weather_items;
        this.activity = activity;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

//    public MyRecyclerForecastAdapter(View view, List<DayForecast> weather_items, Activity activity){
//        this.weather_items = weather_items;
//        this.activity = activity;
//
//        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) view.getLayoutManager();
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                totalItemCount = linearLayoutManager.getItemCount();
//                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//            }
//        });
//    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.dayforecast,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        System.out.println("Position: "+position+"\nList size: "+weather_items.size());
        DayForecast weatherItem = weather_items.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.description.setText(weatherItem.descr() + " ");
        myViewHolder.weekday.setText(weatherItem.dow() + " " + weatherItem.time());
        myViewHolder.weatherIcon.setImageBitmap(BitmapFactory.decodeByteArray(weatherItem.weather.iconData,0,weatherItem.weather.iconData.length));
        myViewHolder.highTemp.setText("High: "+ weatherItem.forecastTemp.getHigh() + "º");
        myViewHolder.lowTemp.setText("Low: " + weatherItem.forecastTemp.getLow() + "º");
    }

    @Override
    public int getItemCount() {
        return weather_items.size();
    }



    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    // ViewHolder
    private class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView weatherIcon;
        public TextView description,weekday,highTemp,lowTemp;

        public MyViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.description);
            weekday = (TextView) view.findViewById(R.id.weekday);
            weatherIcon = (ImageView) view.findViewById(R.id.weatherIcon);
            highTemp = (TextView) view.findViewById(R.id.highTemp);
            lowTemp = (TextView) view.findViewById(R.id.lowTemp);
        }
    }
}
