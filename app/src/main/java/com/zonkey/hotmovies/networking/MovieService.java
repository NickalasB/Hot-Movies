package com.zonkey.hotmovies.networking;

import com.zonkey.hotmovies.BuildConfig;
import com.zonkey.hotmovies.models.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieService {

    @GET("movie/popular?api_key=" + BuildConfig.MOVIE_DB_API_KEY)
    Call<List<Movie>> getPopularMovies();

    @GET("movie/top_rated?api_key=" + BuildConfig.MOVIE_DB_API_KEY)
    Call<List<Movie>> getHighestRatedMovies();

    @GET("movie/{id}?append_to_response=reviews,videos&api_key=" + BuildConfig.MOVIE_DB_API_KEY)
    Call<Movie> getMovie(@Path("id") String movieId);
}
