package com.github.abhrp.pixabaysearchdemo.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.abhrp.pixabaysearchdemo.R;
import com.github.abhrp.pixabaysearchdemo.adapters.base.RecyclerAdapter;
import com.github.abhrp.pixabaysearchdemo.model.PixabayPhoto;
import com.github.abhrp.pixabaysearchdemo.util.Util;
import com.github.abhrp.pixabaysearchdemo.widget.NetworkImageView;

import java.lang.ref.WeakReference;

/**
 * Created by abhrp on 5/1/17.
 */

public class ImageListAdapter extends RecyclerAdapter<PixabayPhoto> {

    private WeakReference<Context> context;
    private final View.OnClickListener onClickListener;

    public ImageListAdapter(Context context, View.OnClickListener onClickListener) {
        super(context);
        this.context = new WeakReference<>(context);
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        if (viewHolder == null) {
            View view = mInflater.inflate(R.layout.item_image, parent, false);
            viewHolder = new ImageViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onItemBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isFooterView(position) || isHeaderView(position)) {
            return;
        }
        if (position < mList.size() && mList.get(position) != null && holder instanceof ImageViewHolder) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            int displayWidth = Util.displayWidth/2 - Util.convertDpToPixel(context.get(), 24);
            double aspectRatio = (getItem(position).webformatHeight)/(getItem(position).webformatWidth*1.0);
            int displayHeight = (int) (displayWidth * aspectRatio);
            imageViewHolder.imageView.getLayoutParams().height = displayHeight;
            imageViewHolder.imageView.getLayoutParams().width = displayWidth;
            imageViewHolder.imageView.setImageUrl(getItem(position).webformatURL, displayWidth, displayHeight);
            imageViewHolder.imageCardView.setTag(position);
            imageViewHolder.imageCardView.setOnClickListener(onClickListener);
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public CardView imageCardView;
        public NetworkImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageCardView = (CardView) itemView.findViewById(R.id.image_card);
            imageView = (NetworkImageView) itemView.findViewById(R.id.feed_image);
        }
    }
}
