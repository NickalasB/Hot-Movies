package com.zonkey.hotmovies.networking;

import com.zonkey.hotmovies.models.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieService {

    @GET("discover/movie?api_key=cc19772c03a449027eaa0cb6559f304a&sort_by=popularity.desc")
    Call<List<Movie>> getPopularMovies();

    @GET("discover/movie?api_key=cc19772c03a449027eaa0cb6559f304a&sort_by=popularity.desc")
    Call<List<Movie>> getHighestRatedMovies();
}
