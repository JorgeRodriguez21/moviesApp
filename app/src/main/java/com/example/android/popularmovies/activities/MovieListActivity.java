package com.example.android.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MovieRecyclerAdapter;
import com.example.android.popularmovies.fragments.MovieDetailFragment;
import com.example.android.popularmovies.handlers.HandlerCallback;
import com.example.android.popularmovies.handlers.MovieHandler;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.util.SharedPreferencesUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private MovieHandler movieHandler;
    @InjectView(R.id.movie_list)
    protected RecyclerView recyclerView;
    MovieRecyclerAdapter adapter;
    private ArrayList<Movie> favorites;
    private boolean isInFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.inject(this);
        movieHandler = new MovieHandler(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(toolbar!=null)
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        getMoviesData(R.string.popular_movie_url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.top_rated) {
            getMoviesData(R.string.top_rated_movie_url);
            isInFavorites = false;
            return true;
        }else if(id == R.id.most_popular){
            getMoviesData(R.string.popular_movie_url);
            isInFavorites = false;
            return true;
        }else  if(id == R.id.favorites) {
            getFavoriteMovies();
            isInFavorites = true;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(int columnsNumber) {
        adapter = new MovieRecyclerAdapter(this,movieHandler.getMoviesList());
        adapter.setClickListener(showMoviesInformation());
        GridLayoutManager manager = new GridLayoutManager(this, columnsNumber);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public HandlerCallback.listener getMovieData(){

        return new HandlerCallback.listener() {
            @Override
            public void onResponse(int status) {
                if(status==HandlerCallback.OK) {
                    if (mTwoPane) {
                        setupRecyclerView(3);
                    } else {
                        setupRecyclerView(2);
                    }
                }
            }
        };
    }

    private void getMoviesData(int url){
        movieHandler.searchMovies(getString(url)+getString(R.string.api_key),getMovieData());
    }

    private View.OnClickListener showMoviesInformation(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = (Movie) v.getTag();

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(getString(R.string.movie_tag), movie);
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                }else {
                    Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                    intent.putExtra(getString(R.string.movie_tag), movie);
                    startActivity(intent);
                }
            }
        };
    }

    private void getFavoriteMovies(){

        favorites = SharedPreferencesUtils.obtainFavorites(this);
        if(favorites == null){
            favorites = new ArrayList<>();
        }
         int columnsNumber = mTwoPane?3:2;
            adapter = new MovieRecyclerAdapter(this,favorites);
            adapter.setClickListener(showMoviesInformation());
            GridLayoutManager manager = new GridLayoutManager(this, columnsNumber);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isInFavorites)
            getFavoriteMovies();
    }
}
