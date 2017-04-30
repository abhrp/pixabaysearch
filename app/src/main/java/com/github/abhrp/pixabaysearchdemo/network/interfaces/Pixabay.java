package com.github.abhrp.pixabaysearchdemo.network.interfaces;

import com.github.abhrp.pixabaysearchdemo.network.response.PixabayPhotoResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by abhrp on 4/30/17.
 */

public interface Pixabay {
    @GET("api/?key=5241194-8a3a35f267b5afa498d2faab4")
    Call<PixabayPhotoResponse> getPhotos(@QueryMap Map<String, Object> params);
}
