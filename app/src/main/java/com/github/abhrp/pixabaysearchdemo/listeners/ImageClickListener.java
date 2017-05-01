package com.github.abhrp.pixabaysearchdemo.listeners;

import android.view.View;

import com.github.abhrp.pixabaysearchdemo.model.PixabayPhoto;

/**
 * Created by abhrp on 5/1/17.
 */

public interface ImageClickListener {
    void imageClicked(PixabayPhoto photo, View sharedView);
}
