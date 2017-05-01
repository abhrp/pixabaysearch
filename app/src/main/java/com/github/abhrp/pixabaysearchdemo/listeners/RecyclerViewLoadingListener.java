package com.github.abhrp.pixabaysearchdemo.listeners;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by abhrp on 5/1/17.
 */

public class RecyclerViewLoadingListener extends RecyclerView.OnScrollListener {

    protected static final int PAGE_LOAD_THRESHOLD = 8;

    public interface OnLoadItems {

        void onLoadMore();
        boolean hasMoreItems();
    }

    private OnLoadItems mCallback;
    private boolean mIsLoading;

    public RecyclerViewLoadingListener(final OnLoadItems callback) {
        mCallback = callback;
        mIsLoading = false;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        int visibleItemCount = recyclerView.getChildCount(),
                totalItemCount = layoutManager.getItemCount(),
                firstVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            firstVisibleItemPosition = ((GridLayoutManager) layoutManager)
                    .findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager){
            int[] firstVisibleItemPositions = new int[2];
            firstVisibleItemPosition = ((StaggeredGridLayoutManager) layoutManager)
                    .findFirstVisibleItemPositions(firstVisibleItemPositions)[0];
        } else {
            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                    .findFirstVisibleItemPosition();
        }
        boolean loadMore = true;

        if (totalItemCount > 0 && !mIsLoading &&
                (firstVisibleItemPosition + visibleItemCount > totalItemCount - PAGE_LOAD_THRESHOLD)
                && mCallback.hasMoreItems() && loadMore) {
            mIsLoading = true;
            mCallback.onLoadMore();
        }
    }

    public void setDoneLoading() {
        mIsLoading = false;
    }
}
