package com.github.abhrp.pixabaysearchdemo.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.github.abhrp.pixabaysearchdemo.R;
import com.github.abhrp.pixabaysearchdemo.network.NetworkConfig;
import com.github.abhrp.pixabaysearchdemo.network.response.PixabayPhotoResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PixabaySearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixabay_search);
        handleIntent(getIntent());

        getPhotos();
    }

    private void getPhotos() {
        Map<String, Object> params = new HashMap<>();
        params.put("q", "flowers");
        params.put("photo_type", "photo");

        NetworkConfig.getNetworkConfig().getPhotos(params, new Callback<PixabayPhotoResponse>() {
            @Override
            public void onResponse(Call<PixabayPhotoResponse> call, Response<PixabayPhotoResponse> response) {

            }

            @Override
            public void onFailure(Call<PixabayPhotoResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pixabay_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }
}
