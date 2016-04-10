package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by negri on 07/04/2016.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context context;
    private List<Movie> moviesList;
    private View.OnClickListener listener;


    public MovieRecyclerAdapter(Context context, List<Movie> moviesList) {
        this.context = context;
        this.moviesList = moviesList;
    }
    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onClick(v);
    }


    public void setClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView cover;

        public ViewHolder(View view) {
            super(view);
            cover = (ImageView) view.findViewById(R.id.coverImage);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder vh;
            v = LayoutInflater.from(context).inflate(R.layout.movie_list_content, parent, false);
        v.findViewById(R.id.coverImage).setOnClickListener(this);
            vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w500"+moviesList.get(position).getPosterPath())
                    .into(((ViewHolder) holder).cover);
            ((ViewHolder) holder).cover.setTag(moviesList.get(position));
        }
    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
