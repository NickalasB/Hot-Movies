package com.zonkey.hotmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nickbradshaw on 4/5/16.
 */
public class Movie implements Parcelable {


    public String poster_path;
    public String title;
    public String overview;
    public int id;
    public String vote_count;
    public String vote_average;
    public String release_date;
    public String backdrop_path;


    public Movie(String poster_path,
                 String title,
                 String overview,
                 int id,
                 String vote_count,
                 String vote_average,
                 String release_date,
                 String backdrop_path) {
        this.poster_path = poster_path;
        this.title = title;
        this.overview = overview;
        this.id = id;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.backdrop_path = backdrop_path;

    }

    //this  constructs the base URL plus the "poster_path" defined in the API
    public String getPosterURL() {

        return String.format("http://image.tmdb.org/t/p/w185%s", poster_path);
    }

    //this constructs the base URL plus the "poster_path" defined in the API- it is not yet implemented
    public String getBackdropURL() {
        return String.format("http://image.tmdb.org/t/p/w185%s", backdrop_path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.poster_path);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeInt(this.id);
        dest.writeString(this.vote_count);
        dest.writeString(this.vote_average);
        dest.writeString(this.release_date);
        dest.writeString(this.backdrop_path);
    }

    protected Movie(Parcel in) {
        this.poster_path = in.readString();
        this.title = in.readString();
        this.overview = in.readString();
        this.id = in.readInt();
        this.vote_count = in.readString();
        this.vote_average = in.readString();
        this.release_date = in.readString();
        this.backdrop_path = in.readString();

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
