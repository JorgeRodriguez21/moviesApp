package com.example.android.popularmovies.fragments;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.activities.MovieDetailActivity;
import com.example.android.popularmovies.activities.MovieListActivity;
import com.example.android.popularmovies.adapters.ReviewRecyclerAdapter;
import com.example.android.popularmovies.adapters.TrailerRecyclerAdapter;
import com.example.android.popularmovies.handlers.HandlerCallback;
import com.example.android.popularmovies.handlers.MovieHandler;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.MovieTrailer;
import com.example.android.popularmovies.util.SharedPreferencesUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {

    private Movie movie;
    private MovieHandler movieHandler;
    private TrailerRecyclerAdapter trailerRecyclerAdapter;
    private ReviewRecyclerAdapter reviewRecyclerAdapter;
    private RecyclerView trailersRV;
    private RecyclerView reviewsRV;
    private boolean isFavorite;
    ArrayList <Movie> favorites;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(getString(R.string.movie_tag))) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            movie = getArguments().getParcelable(getString(R.string.movie_tag));
            movieHandler = new MovieHandler(getContext());
            getFavoriteMovies();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private HandlerCallback.listener searchMoviesTrailer() {

        return new HandlerCallback.listener() {
            @Override
            public void onResponse(int status) {

                if (status == HandlerCallback.OK) {
                    setTrailerAdapter();
                }
            }
        };
    }

    private HandlerCallback.listener searchMovieReviews() {

        return new HandlerCallback.listener() {
            @Override
            public void onResponse(int status) {

                if (status == HandlerCallback.OK) {
                    setReviewsAdapter();
                }
            }
        };
    }

    private void setTrailerAdapter() {
        trailerRecyclerAdapter = new TrailerRecyclerAdapter(getContext(), movieHandler.getMovieTrailers());
        trailerRecyclerAdapter.setClickListener(movieTrailerClickListener());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        trailersRV.setLayoutManager(linearLayoutManager);
        trailersRV.setAdapter(trailerRecyclerAdapter);
    }

    private void setReviewsAdapter() {
        reviewRecyclerAdapter = new ReviewRecyclerAdapter(getContext(), movieHandler.getMovieReviews());
        reviewRecyclerAdapter.setClickListener(reviewClickListener());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        reviewsRV.setLayoutManager(linearLayoutManager);
        reviewsRV.setAdapter(reviewRecyclerAdapter);
    }

    private View.OnClickListener reviewClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle(getContext().getString(R.string.review));
                WebView wv = new WebView(getContext());
                wv.loadUrl((String) v.getTag());
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                alert.setView(wv);
                alert.setNegativeButton(getContext().getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        };
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }



    private View.OnClickListener movieTrailerClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieTrailer mt = (MovieTrailer) v.getTag();
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(getContext().getString(R.string.application_youtube_link) + mt.getKey()));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(getContext().getString(R.string.web_youtube_link) + mt.getKey()));
                    startActivity(intent);
                }
            }
        };
    }

    private void getFavoriteMovies(){

        favorites = SharedPreferencesUtils.obtainFavorites(getContext());
        if(favorites == null){
            favorites = new ArrayList<>();
        }
        isFavorite = favorites.contains(movie);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        trailersRV = (RecyclerView) rootView.findViewById(R.id.trailersRV);
        reviewsRV = (RecyclerView) rootView.findViewById(R.id.reviewsRV);
        movieHandler.searchMovieTrailers(movie.getId(), searchMoviesTrailer());
        movieHandler.searchMovieReviews(movie.getId(), searchMovieReviews());
        if (movie.getReleaseDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(movie.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.releaseDate)).setText("" + calendar.get(Calendar.YEAR));
        }
        ((TextView) rootView.findViewById(R.id.average)).setText("" + movie.getVoteAverage());
        ((TextView) rootView.findViewById(R.id.overview)).setText(movie.getOverview());
        ((TextView) rootView.findViewById(R.id.title)).setText(movie.getName());
        final ImageView favoriteBtn = (ImageView) rootView.findViewById(R.id.favoriteBtn);
        if(isFavorite){
            favoriteBtn.setImageBitmap(
                    decodeSampledBitmapFromResource(getResources(), R.drawable.favorite, 100, 100));
        }else{
            favoriteBtn.setImageBitmap(
                    decodeSampledBitmapFromResource(getResources(), R.drawable.no_favorite, 100, 100));
        }
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFavorite){
                    favorites.remove(movie);
                    SharedPreferencesUtils.saveFavorites(getContext(),favorites);
                    favoriteBtn.setImageBitmap(
                            decodeSampledBitmapFromResource(getResources(), R.drawable.no_favorite, 100, 100));
                    isFavorite= false;
                }else {
                    favorites.add(movie);
                    SharedPreferencesUtils.saveFavorites(getContext(),favorites);
                    favoriteBtn.setImageBitmap(
                            decodeSampledBitmapFromResource(getResources(), R.drawable.favorite, 100, 100));
                    isFavorite = true;
                }
            }
        });
        ImageView cover = ((ImageView) rootView.findViewById(R.id.coverImage));
        Picasso.with(getContext())
                .load(getContext().getString(R.string.images_url) + movie.getPosterPath()).into(cover);
        return rootView;
    }
}
