package com.example.android.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by negri on 24/04/2016.
 */
public class SharedPreferencesUtils {

    private static void saveStringInSharedPreferences(Context context, String key, String value){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static   String obtainStringFromSharedPreferences(final Context context, String key){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static void saveFavorites(final Context context, ArrayList<Movie> favoriteMovies){

        Gson gson = createGsonObject();
        String json = gson.toJson(favoriteMovies);
        saveStringInSharedPreferences (context,context.getString(R.string.favorite),json);
    }

    public static ArrayList<Movie> obtainFavorites(final Context context){

        Gson gson = createGsonObject();
        String json =obtainStringFromSharedPreferences(context,context.getString(R.string.favorite));
        Type type = new TypeToken<ArrayList<Movie>>() {}.getType();
        return gson.fromJson(json,type);
    }

    @NonNull
    private static Gson createGsonObject() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
    }




}
