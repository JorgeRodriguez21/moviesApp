package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {

    private Movie movie;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        if(movie.getReleaseDate()!=null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(movie.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.releaseDate)).setText(""+calendar.get(Calendar.YEAR));
        }
        ((TextView) rootView.findViewById(R.id.average)).setText(""+movie.getVoteAverage());
        ((TextView) rootView.findViewById(R.id.sinopsis)).setText(movie.getOverview());
        ((TextView) rootView.findViewById(R.id.title)).setText(movie.getName());
       ImageView cover = ((ImageView) rootView.findViewById(R.id.coverImage));
        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/w300"+movie.getPosterPath()).into(cover);
        return rootView;
    }
}
