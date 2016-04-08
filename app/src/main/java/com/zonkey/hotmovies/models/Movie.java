package com.zonkey.hotmovies.models;

/**
 * Created by nickbradshaw on 4/5/16.
 */
public class Movie {

    // these are just objects
    public String poster_path;
    public String title;
    public String overview;

    public Movie(String poster_path, String title, String overview) {
        this.poster_path = poster_path;
        this.title = title;
        this.overview = overview;
    }

    //this method constructs the base URL plus the "poster_path" defined in the API
    public String getPosterURL() {

        return String.format("http://image.tmdb.org/t/p/w500%s", poster_path);
    }



}
