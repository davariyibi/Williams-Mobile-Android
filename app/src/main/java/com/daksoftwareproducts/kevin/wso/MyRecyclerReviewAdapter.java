package com.daksoftwareproducts.kevin.wso;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kevin on 6/18/2017.
 */

public class MyRecyclerReviewAdapter extends RecyclerView.Adapter {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener onLoadMoreListener;

    private boolean isLoading;
    private Activity activity;
    private List<Review_Item> review_items;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public MyRecyclerReviewAdapter(RecyclerView recyclerView, List<Review_Item> review_items, Activity activity){
        this.review_items = review_items;
        this.activity = activity;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        // if new_item is null, itemViewType is view_type_loading, itemViewType is view_type_item otherwise
        return review_items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.review_item, parent, false);
            return new UserViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            final Review_Item review_item = review_items.get(position);
            final UserViewHolder userViewHolder = (UserViewHolder) holder;
            userViewHolder.courseTitle.setText(review_item.getCourseTitle());
            userViewHolder.comment.setText(review_item.getComment());
            userViewHolder.rating.setText(review_item.getRating());
            userViewHolder.datePosted.setText(review_item.getDatePosted());
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return review_items == null ? 0 : review_items.size();
    }

    public void setLoaded() {
        isLoading = false;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    // "Loading item" ViewHolder
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    // "Normal item" ViewHolder
    private class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView courseTitle;
        public TextView comment;
        public TextView rating;
        public TextView datePosted;

        public UserViewHolder(View view) {
            super(view);
            courseTitle = (TextView) view.findViewById(R.id.course_title);
            comment = (TextView) view.findViewById(R.id.comment);
            rating = (TextView) view.findViewById(R.id.rating);
            datePosted = (TextView) view.findViewById(R.id.date_posted);

        }
    }
}
