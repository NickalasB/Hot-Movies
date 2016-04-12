package com.zonkey.hotmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nickbradshaw on 4/5/16.
 */
public class Movie implements Parcelable {

    // these are just objects
    public String poster_path;
    public String title;
    public String overview;
    public int id;
    public String vote_count;
    public String release_date;



    public Movie(String poster_path, String title, String overview, int id, String vote_count, String release_date) {
        this.poster_path = poster_path;
        this.title = title;
        this.overview = overview;
        this.id = id;
        this.vote_count = vote_count;
        this.release_date = release_date;

    }

    //this method constructs the base URL plus the "poster_path" defined in the API
    public String getPosterURL() {

        return String.format("http://image.tmdb.org/t/p/w185%s", poster_path);
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
        dest.writeString(this.release_date);
    }

    protected Movie(Parcel in) {
        this.poster_path = in.readString();
        this.title = in.readString();
        this.overview = in.readString();
        this.id = in.readInt();
        this.vote_count = in.readString();
        this.release_date = in.readString();
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
