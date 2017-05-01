package com.github.abhrp.pixabaysearchdemo.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.github.abhrp.pixabaysearchdemo.R;

import static android.R.attr.width;

/**
 * Created by abhrp on 5/1/17.
 */

public class NetworkImageView extends ImageView {
    private String url;

    public NetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public NetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NetworkImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setImageUrl(String url, int width, int height) {
        if (url != null && url.contains("http")) {
            this.url = url;
            setupGlide(-1, true, width, height);
        }
    }

    public void setImageUrl(String url, RequestListener listener) {
        if (url != null && url.contains("http")) {
            this.url = url;
            setupGlide(-1, true, listener);
        }
    }

    private void setupGlide(int drawable, boolean animated, int width, int height) {
        if (getContext() == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                ((Activity) getContext()).isDestroyed()) {
            return;
        }
        DrawableRequestBuilder builder;
        builder = Glide.with(getContext()).load(url)
                .override(width, height)
                .fitCenter().skipMemoryCache(false);

        if(drawable != -1) {
            builder.error(drawable).placeholder(drawable).fitCenter();
        }
        builder.dontTransform().diskCacheStrategy(DiskCacheStrategy.SOURCE);
        if(!animated || !animated) {
            builder.dontAnimate();
        }
        builder.into(this);
    }

    private void setupGlide(int drawable, boolean animated, RequestListener listener) {
        if (getContext() == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                ((Activity) getContext()).isDestroyed()) {
            return;
        }
        DrawableRequestBuilder builder;
        builder = Glide.with(getContext()).load(url).listener(listener)
                .fitCenter().skipMemoryCache(false);

        if(drawable != -1) {
            builder.error(drawable).placeholder(drawable).fitCenter();
        }
        builder.dontTransform().diskCacheStrategy(DiskCacheStrategy.SOURCE);
        if(!animated || !animated) {
            builder.dontAnimate();
        }
        builder.into(this);
    }
}
