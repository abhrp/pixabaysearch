package com.github.abhrp.pixabaysearchdemo.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.abhrp.pixabaysearchdemo.R;
import com.github.abhrp.pixabaysearchdemo.model.PixabayPhoto;
import com.github.abhrp.pixabaysearchdemo.util.Constants;
import com.github.abhrp.pixabaysearchdemo.widget.NetworkImageView;

public class PhotoActivity extends AppCompatActivity {

    private NetworkImageView mNetworkImageView;
    private TextView userTextView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        setContentView(R.layout.activity_photo);
        final PixabayPhoto pixabayPhoto = (PixabayPhoto) getIntent().getSerializableExtra(Constants.PHOTO);
        mNetworkImageView = (NetworkImageView) findViewById(R.id.full_image);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        userTextView = (TextView) findViewById(R.id.user_text);
        userTextView.setVisibility(View.GONE);

        mNetworkImageView.setImageUrl(pixabayPhoto.webformatURL, new RequestListener() {
            @Override
            public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                mProgressBar.setVisibility(View.GONE);
                userTextView.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                mProgressBar.setVisibility(View.GONE);
                userTextView.setText(getString(R.string.photo_by_user, pixabayPhoto.user));
                userTextView.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }
}
