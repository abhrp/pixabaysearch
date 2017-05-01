package com.github.abhrp.pixabaysearchdemo.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.abhrp.pixabaysearchdemo.R;
import com.github.abhrp.pixabaysearchdemo.adapters.base.RecyclerAdapter;
import com.github.abhrp.pixabaysearchdemo.listeners.ImageClickListener;
import com.github.abhrp.pixabaysearchdemo.model.PixabayPhoto;
import com.github.abhrp.pixabaysearchdemo.util.Constants;
import com.github.abhrp.pixabaysearchdemo.util.Util;
import com.github.abhrp.pixabaysearchdemo.widget.NetworkImageView;

import java.lang.ref.WeakReference;

/**
 * Created by abhrp on 5/1/17.
 */

public class ImageListAdapter extends RecyclerAdapter<PixabayPhoto> {

    private WeakReference<Context> context;
    private final ImageClickListener imageClickListener;

    public ImageListAdapter(Context context, ImageClickListener imageClickListener) {
        super(context);
        this.context = new WeakReference<>(context);
        this.imageClickListener = imageClickListener;
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
    public void onItemBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position < mList.size() && mList.get(position) != null && holder instanceof ImageViewHolder) {
            final ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            int displayWidth = Util.displayWidth/2 - Util.convertDpToPixel(context.get(), Constants.PADDING);
            double aspectRatio = (getItem(position).previewHeight)/(getItem(position).previewWidth*1.0);
            int displayHeight = (int) (displayWidth * aspectRatio);
            imageViewHolder.imageView.getLayoutParams().height = displayHeight;
            imageViewHolder.imageView.getLayoutParams().width = displayWidth;
            imageViewHolder.imageView.setImageUrl(getItem(position).previewURL, displayWidth, displayHeight);
            imageViewHolder.imageCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageClickListener != null) {
                        imageClickListener.imageClicked(getItem(position), imageViewHolder.imageView);
                    }
                }
            });
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
