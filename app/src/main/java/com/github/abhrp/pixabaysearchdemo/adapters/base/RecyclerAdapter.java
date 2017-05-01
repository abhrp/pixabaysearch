package com.github.abhrp.pixabaysearchdemo.adapters.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.abhrp.pixabaysearchdemo.util.Util;

import java.util.ArrayList;

/**
 * Created by abhrp on 5/1/17.
 */

public abstract class RecyclerAdapter<T> extends BaseRecyclerAdapter<T, RecyclerView.ViewHolder> {

    private static final int HEADER_VIEW_TYPE = 1, FOOTER_VIEW_TYPE = 2;

    protected int headerViewCount, footerViewCount;

    private LinearLayout mHeaderView, mFooterView;

    public RecyclerAdapter(final Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == HEADER_VIEW_TYPE) {
            return new HeaderOrFooterViewHolder(mHeaderView);
        } else if (viewType == FOOTER_VIEW_TYPE) {
            return new HeaderOrFooterViewHolder(mFooterView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (isHeaderView(position)) {
            return;
        } else if (isFooterView(position)) {
            return;
        } else {
            onItemBindViewHolder(holder, position - headerViewCount);
        }
    }

    public abstract void onItemBindViewHolder(final RecyclerView.ViewHolder holder, final int position);

    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        if (isHeaderView(position)) {
            type = HEADER_VIEW_TYPE;
        } else if (isFooterView(position)) {
            type = FOOTER_VIEW_TYPE;
        }
        return type;
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        return mList.size() + headerViewCount + footerViewCount;
    }

    public int getChildCountWithoutHeaderFooter() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public void notifyChildItemChanged(
            int position) { //if count of header > 0, we need use this method to avoid crash.
        if (headerViewCount > 0) {
            notifyItemChanged(position + headerViewCount);
        } else {
            notifyItemChanged(position);
        }
    }

    public void notifyChildItemInserted(int position) {
        if (headerViewCount > 0) {
            notifyItemInserted(position + headerViewCount);
        } else {
            notifyItemInserted(position);
        }
    }

    public void notifyChildItemRemoved(int position) {
        if (headerViewCount > 0) {
            notifyItemRemoved(position + headerViewCount);
        } else {
            notifyItemRemoved(position);
        }
    }

    public void notifyChildItemRangeChanged(int position1, int position2) {
        if (headerViewCount > 0) {
            notifyItemRangeChanged(position1 + headerViewCount, position2 + headerViewCount);
        } else {
            notifyItemRangeChanged(position1, position2);
        }
    }

    public void notifyChildItemRangeInserted(int position1, int position2) {
        if (headerViewCount > 0) {
            notifyItemRangeInserted(position1 + headerViewCount, position2 + headerViewCount);
        } else {
            notifyItemRangeInserted(position1, position2);
        }
    }

    public void notifyChildItemRangeRemoved(int position1, int position2) {
        if (headerViewCount > 0) {
            notifyItemRangeRemoved(position1 + headerViewCount, position2 + headerViewCount);
        } else {
            notifyItemRangeRemoved(position1, position2);
        }
    }

    public boolean isHeaderView(int position) {
        return position == 0 && headerViewCount > 0;
    }

    public boolean isFooterView(int position) {
        return position >= (headerViewCount > 0 ? mList.size() + 1 : mList.size())
                && footerViewCount > 0;
    }

    public static class HeaderOrFooterViewHolder extends RecyclerView.ViewHolder {

        protected View headerOrFooterView;

        public HeaderOrFooterViewHolder(View itemView) {
            super(itemView);
            headerOrFooterView = itemView;
        }
    }

    public void addHeader(View headerView) {
        if (mHeaderView == null) {
            mHeaderView = new LinearLayout(mContext);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            mHeaderView.setLayoutParams(params);
            mHeaderView.setOrientation(LinearLayout.VERTICAL);
            headerViewCount++;
        }
        if (headerView != null) {
            mHeaderView.addView(headerView);
            notifyDataSetChanged();
        }
    }

    public void addHeader(View headerView, boolean wrapContentWidth) {
        if (mHeaderView == null) {
            mHeaderView = new LinearLayout(mContext);
            RecyclerView.LayoutParams params;
            if (wrapContentWidth) {
                params = new RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            } else {
                params = new RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            }
            params.width = Util.convertDpToPixel(mContext, 240);
            mHeaderView.setLayoutParams(params);
            mHeaderView.setOrientation(LinearLayout.VERTICAL);
            headerViewCount++;
        } else {

        }
        if (headerView != null) {
            mHeaderView.addView(headerView);
            notifyDataSetChanged();
        }
    }

    public int getHeaderHeight() {
        if (this.mHeaderView != null) {
            return mHeaderView.getMeasuredHeight();
        }

        return 0;
    }


    public void addFooter(View footerView) {
        if (mFooterView == null) {
            mFooterView = new LinearLayout(mContext);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            mFooterView.setLayoutParams(params);
            mFooterView.setOrientation(LinearLayout.VERTICAL);
            footerViewCount++;
        }
        if (footerView != null) {
            mFooterView.addView(footerView);
            notifyDataSetChanged();
        }
    }

    public void addFooter(View footerView, boolean wrapContentWidth) {
        if (mFooterView == null) {
            mFooterView = new LinearLayout(mContext);
            RecyclerView.LayoutParams params;
            if (wrapContentWidth) {
                params = new RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            } else {
                params = new RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            }
            params.width = Util.convertDpToPixel(mContext, 240);
            mFooterView.setLayoutParams(params);
            mFooterView.setOrientation(LinearLayout.VERTICAL);
            mFooterView.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
            footerViewCount++;
        }
        if (footerView != null) {
            mFooterView.addView(footerView);
            notifyDataSetChanged();
        }
    }

    public void removeFooters() {
        if (mFooterView != null && mFooterView.getChildCount() > 0) {
            mFooterView.removeAllViews();
            notifyDataSetChanged();
        }
    }

    public void removeHeaders() {
        if (mHeaderView != null && mHeaderView.getChildCount() > 0) {
            mHeaderView.removeAllViews();
            notifyDataSetChanged();
        }
    }

    public int getHeaderViewCount() {
        return headerViewCount;
    }

    public int getFooterViewCount() {
        return footerViewCount;
    }
}
