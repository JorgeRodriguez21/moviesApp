package com.example.android.popularmovies.handlers;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.MovieTrailer;
import com.example.android.popularmovies.services.VolleyService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by negri on 08/04/2016.
 */
public class MovieHandler {

    private Context context;
    private VolleyService volley;
    protected RequestQueue requestQueue;
    private ArrayList<Movie> moviesList;
    private ArrayList<MovieTrailer> movieTrailers;
    private ArrayList<String> movieReviews;

    public MovieHandler(Context context){
        this.context = context;
        volley = VolleyService.getInstance(this.context);
        requestQueue = volley.getRequestQueue();
        moviesList = new ArrayList<>();
        movieTrailers = new ArrayList<>();
        movieReviews = new ArrayList<>();
    }

    public void addToQueue(Request request) {
        if (request != null) {
            request.setTag(this);
            if (requestQueue == null)
                requestQueue = volley.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            requestQueue.add(request);
        }
    }

    public void searchMovies(String url, final HandlerCallback.listener listener){
        moviesList = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                JSONObject object;
                JSONArray result = null;
                try {
                    result = jsonObject.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Movie movie;
                for (int i =0; i<result.length();i++){
                    try {
                        object = result.getJSONObject(i);
                        movie = gson.fromJson(object.toString(), Movie.class);
                        moviesList.add(movie);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onResponse(HandlerCallback.OK);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.onResponse(HandlerCallback.FAIL);
                try {
                    System.out.println(new String(volleyError.networkResponse.data,"UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addToQueue(request);
    }

    public void searchMovieTrailers(Long id, final HandlerCallback.listener listener){
        moviesList = new ArrayList<>();
        String url = context.getString(R.string.first_url_part)+id+context.getString(R.string.last_video_url_part).
                concat(context.getString(R.string.api_key));
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                JSONObject object;
                JSONArray result = null;
                try {
                    result = jsonObject.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MovieTrailer movie;
                for (int i =0; i<result.length();i++){
                    try {
                        object = result.getJSONObject(i);
                        movie = gson.fromJson(object.toString(), MovieTrailer.class);
                        movieTrailers.add(movie);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onResponse(HandlerCallback.OK);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.onResponse(HandlerCallback.FAIL);
                try {
                    System.out.println(new String(volleyError.networkResponse.data,"UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addToQueue(request);
    }

    public void searchMovieReviews(Long id, final HandlerCallback.listener listener){
        moviesList = new ArrayList<>();
        String url = context.getString(R.string.first_url_part)+id+context.getString(R.string.last_review_url_part).
                concat(context.getString(R.string.api_key));
        JsonObjectRequest request = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                JSONObject object;
                JSONArray result = null;
                try {
                    result = jsonObject.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String reviewUrl;
                for (int i =0; i<result.length();i++){
                    try {
                        object = result.getJSONObject(i);
                        reviewUrl = object.get("url").toString();
                        movieReviews.add(reviewUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onResponse(HandlerCallback.OK);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.onResponse(HandlerCallback.FAIL);
                try {
                    System.out.println(new String(volleyError.networkResponse.data,"UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addToQueue(request);
    }


    public ArrayList<Movie> getMoviesList() {
        return moviesList;
    }

    public ArrayList<MovieTrailer> getMovieTrailers() {
        return movieTrailers;
    }

    public ArrayList<String> getMovieReviews() {
        return movieReviews;
    }
}
