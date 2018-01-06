package com.daksoftwareproducts.kevin.wso;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kevin on 6/18/2017.
 */

public class MyRecyclerProfileAdapter extends RecyclerView.Adapter {

    private HttpsObject httpsObject;
    private String url;
    private Activity activity;
    private List<Profile_Item> profile_items;

    public MyRecyclerProfileAdapter(RecyclerView recyclerView, List<Profile_Item> profile_items, Activity activity, HttpsObject httpsObject, String url){
        this.profile_items = profile_items;
        this.activity = activity;
        this.httpsObject = httpsObject;
        this.url = url;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.profile, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Profile_Item profile_item = profile_items.get(position);
        final UserViewHolder userViewHolder = (UserViewHolder) holder;
        userViewHolder.name.setText("" + profile_item.getName());
        userViewHolder.unix.setText("" + profile_item.getUnix());
        userViewHolder.address.setText("" + profile_item.getAddress());
//        userViewHolder.image.setImageBitmap();
        userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //implement onClick
                    Intent intent = new Intent(userViewHolder.itemView.getContext(),ProfileActivity.class);
                    intent.putExtra("name",userViewHolder.name.getText());
                    intent.putExtra("unix",userViewHolder.unix.getText());
                    intent.putExtra("link",profile_item.getUrl());
                    intent.putExtra("httpsObject",httpsObject);
                    intent.putExtra("url",url);
                    userViewHolder.itemView.getContext().startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return profile_items == null ? 0 : profile_items.size();
    }

    // "Normal item" ViewHolder
    private class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView unix;
        public TextView address;
//        public ImageView image;

        public UserViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txt_name);
            unix = (TextView) view.findViewById(R.id.txt_unix);
            address = (TextView) view.findViewById(R.id.txt_address);
//            image = (ImageView) view.findViewById(R.id.profileImage);

        }
    }
}
