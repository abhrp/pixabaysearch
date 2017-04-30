package com.github.abhrp.pixabaysearchdemo;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.github.abhrp.pixabaysearchdemo.network.NetworkConfig;

/**
 * Created by abhrp on 4/30/17.
 */

public class PixabayApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        NetworkConfig.getNetworkConfig().configNetworkClient();
    }
}
