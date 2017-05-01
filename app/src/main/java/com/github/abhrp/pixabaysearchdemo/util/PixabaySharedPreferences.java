package com.github.abhrp.pixabaysearchdemo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by abhrp on 5/1/17.
 */

public class PixabaySharedPreferences {
    private static Context context;
    private static final String APP_PREFERENCE = "com.github.abhrp.pixabaysearchdemo.preference";

    private static final String SEARCH_SUGGESTIONS = "search_suggestions";

    public static void setContext(Context context) {
        PixabaySharedPreferences.context = context;
    }

    public static void setSearchSuggestions(String searchSuggestions) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_PREFERENCE, 0).edit();
        List<String> searchSuggestion = getSearchSuggestions();
        boolean isDuplicate = false;
        for (String suggestion: searchSuggestion) {
            if (suggestion.trim().equalsIgnoreCase(searchSuggestions.trim())) {
                isDuplicate = true;
            }
        }
        if (!isDuplicate) {
            searchSuggestion.add(searchSuggestions);
            String json = new Gson().toJson(searchSuggestion);
            editor.putString(SEARCH_SUGGESTIONS, json);
            editor.apply();
        }
    }

    public static List<String> getSearchSuggestions() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCE, 0);
        String searchSuggestions = sharedPreferences.getString(SEARCH_SUGGESTIONS, null);
        if (!TextUtils.isEmpty(searchSuggestions)) {
            Type listType = new TypeToken<List<String>>() { }.getType();
            List<String> list = (List<String>) new Gson()
                    .fromJson(searchSuggestions, listType);
            Collections.reverse(list);
            return list;
        }
        return new ArrayList<>();
    }
}
