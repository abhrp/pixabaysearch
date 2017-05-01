package com.github.abhrp.pixabaysearchdemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.github.abhrp.pixabaysearchdemo.R;
import com.github.abhrp.pixabaysearchdemo.adapters.ImageListAdapter;
import com.github.abhrp.pixabaysearchdemo.model.PixabayPhoto;
import com.github.abhrp.pixabaysearchdemo.network.NetworkConfig;
import com.github.abhrp.pixabaysearchdemo.network.response.PixabayPhotoResponse;
import com.github.abhrp.pixabaysearchdemo.util.PixabaySharedPreferences;
import com.github.abhrp.pixabaysearchdemo.util.Util;
import com.github.abhrp.pixabaysearchdemo.widget.SpacingDecorator;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PixabaySearchActivity extends AppCompatActivity implements MaterialSearchView.OnQueryTextListener, MaterialSearchView.SearchViewListener {

    private FrameLayout mToolbarContainer;
    private Toolbar mToolbar;
    private MaterialSearchView mSearchView;
    private final String KEYWORD_PARAM = "q";
    private RecyclerView mRecyclerView;
    private ImageListAdapter imageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.getDefaultDisplay(this);
        setContentView(R.layout.activity_pixabay_search);
        mToolbarContainer = (FrameLayout) findViewById(R.id.toolbar_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.image_list);
        mRecyclerView.setNestedScrollingEnabled(false);
        int spacing = Util.convertDpToPixel(this, 6);
        mRecyclerView.addItemDecoration(new SpacingDecorator(spacing, spacing, true));
        imageListAdapter = new ImageListAdapter(this, null);
        imageListAdapter.setList(new ArrayList<PixabayPhoto>());
        mRecyclerView.setAdapter(imageListAdapter);
        mRecyclerView.setVisibility(View.GONE);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(layoutManager);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        mSearchView.setVoiceSearch(false);
        mSearchView.setCursorDrawable(R.drawable.custom_cursor);
        mSearchView.setEllipsize(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSearchViewListener(this);
        addSuggestions();
    }

    private void addSuggestions() {
        if (PixabaySharedPreferences.getSearchSuggestions() != null && PixabaySharedPreferences.getSearchSuggestions().size() > 0) {
            List<String> suggestionsList = PixabaySharedPreferences.getSearchSuggestions();
            Collections.reverse(suggestionsList);
            String[] suggestions = new String[suggestionsList.size()];
            for (int i=0; i < suggestions.length; i++) {
                suggestions[i] = suggestionsList.get(i);
            }
            mSearchView.setSuggestions(suggestions);
        }
    }

    private void getPhotos(String searchQuery) {
        Map<String, Object> params = new HashMap<>();
        if (!TextUtils.isEmpty(searchQuery)) {
            params.put(KEYWORD_PARAM, searchQuery);
        }

        NetworkConfig.getNetworkConfig().getPhotos(params, new Callback<PixabayPhotoResponse>() {
            @Override
            public void onResponse(Call<PixabayPhotoResponse> call, Response<PixabayPhotoResponse> response) {
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        imageListAdapter.addSubList(response.body().hits);
                        imageListAdapter.notifyDataSetChanged();
                        mRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                    }
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PixabayPhotoResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pixabay_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mSearchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }

    }

    /**
     * Called when the user submits the query. This could be due to a key press on the
     * keyboard or due to pressing a submit button.
     * The listener can override the standard behavior by returning true
     * to indicate that it has handled the submit request. Otherwise return false to
     * let the SearchView handle the submission by launching any associated intent.
     *
     * @param query the query text that is to be submitted
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }
        getPhotos(query);
        PixabaySharedPreferences.setSearchSuggestions(query.trim());
        return false;
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onSearchViewShown() {
        addSuggestions();
    }

    @Override
    public void onSearchViewClosed() {

    }
}
