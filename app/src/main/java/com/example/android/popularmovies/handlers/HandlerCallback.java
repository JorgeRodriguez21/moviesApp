package com.example.android.popularmovies.handlers;

/**
 * Created by negri on 08/04/2016.
 */
public class HandlerCallback {

    private int status;
    public static final int OK = 1;
    public static final int FAIL = 2;

    public interface listener {
        void onResponse(int status);
    }
}
