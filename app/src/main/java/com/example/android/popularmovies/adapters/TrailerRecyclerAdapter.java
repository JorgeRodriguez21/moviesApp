package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.MovieTrailer;

import java.util.List;

/**
 * Created by negri on 07/04/2016.
 */
public class TrailerRecyclerAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context context;
    private List<MovieTrailer> movieTrailerList;
    private View.OnClickListener listener;


    public TrailerRecyclerAdapter(Context context, List<MovieTrailer> moviesList) {
        this.context = context;
        this.movieTrailerList = moviesList;
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
        protected CardView cover;
        protected TextView trailerName;

        public ViewHolder(View view) {
            super(view);
            cover = (CardView) view.findViewById(R.id.trailerContainer);
            trailerName = (TextView) view.findViewById(R.id.trailerName);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder vh;
            v = LayoutInflater.from(context).inflate(R.layout.trailer_card, parent, false);
        v.findViewById(R.id.trailerContainer).setOnClickListener(this);
            vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).cover.setTag(movieTrailerList.get(position));
            ((ViewHolder) holder).trailerName.setText(movieTrailerList.get(position).getName());
        }
    }
    @Override
    public int getItemCount() {
        return movieTrailerList.size();
    }
}
