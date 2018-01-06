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

public class MyRecyclerProfAdapter extends RecyclerView.Adapter {


    private HttpsObject httpsObject;
    private String url;
    private Activity activity;
    private List<Professor_Item> professor_items;

    public MyRecyclerProfAdapter(RecyclerView recyclerView, List<Professor_Item> professor_items, Activity activity, HttpsObject httpsObject, String url){
        this.professor_items = professor_items;
        this.activity = activity;
        this.httpsObject = httpsObject;
        this.url = url;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.professor, parent, false);
        return new UserViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            final Professor_Item professor_item = professor_items.get(position);
            final UserViewHolder userViewHolder = (UserViewHolder) holder;
            userViewHolder.name.setText(professor_item.getName());
            userViewHolder.id.setText(professor_item.getId());
            userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //implement onClick
                    Intent intent = new Intent(userViewHolder.itemView.getContext(),ProfessorActivity.class);
                    intent.putExtra("name",professor_item.getName());
                    intent.putExtra("id",professor_item.getId());
                    intent.putExtra("httpsObject",httpsObject);
                    intent.putExtra("url",url);
                    userViewHolder.itemView.getContext().startActivity(intent);
                    System.out.println(professor_item.getString());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return professor_items == null ? 0 : professor_items.size();
    }

    // "Normal item" ViewHolder
    private class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView id;

        public UserViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txt_name);
            id = (TextView) view.findViewById(R.id.txt_id);

        }
    }
}
