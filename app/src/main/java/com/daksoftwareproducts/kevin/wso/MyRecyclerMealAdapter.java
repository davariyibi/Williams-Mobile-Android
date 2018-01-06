package com.daksoftwareproducts.kevin.wso;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kevin on 10/10/2017.
 */

public class MyRecyclerMealAdapter extends RecyclerView.Adapter {

    private Activity activity;
    private List<Meal_Item> meal_items;

    public MyRecyclerMealAdapter(RecyclerView recyclerView, List<Meal_Item> meal_items, Activity activity){
        this.meal_items = meal_items;
        this.activity = activity;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.meal_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Meal_Item mealItem = meal_items.get(position);
        UserViewHolder userViewHolder = (UserViewHolder) holder;
        userViewHolder.name.setText(mealItem.getName());
    }

    @Override
    public int getItemCount() {
        return meal_items == null ? 0 : meal_items.size();
    }

    // "Normal item" ViewHolder
    private class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public UserViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.meal_name);
        }
    }
}
