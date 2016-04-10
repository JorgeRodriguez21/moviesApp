package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by negri on 07/04/2016.
 */
public class Movie implements Parcelable {

    private Long id;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("original_title")
    private String name;

    private String overview;

    @SerializedName("release_date")
    private Date releaseDate;

    @SerializedName("vote_average")
    private Double voteAverage;


    protected Movie(Parcel in) {
        posterPath = in.readString();
        name = in.readString();
        overview = in.readString();
        releaseDate = (Date) in.readSerializable();
        voteAverage = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Long getId() {
        return id;
    }
    public String getPosterPath() {
        return posterPath;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(name);
        dest.writeString(overview);
        dest.writeSerializable(releaseDate);
        dest.writeDouble(voteAverage);
    }
}
