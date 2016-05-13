package com.zonkey.hotmovies.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zonkey.hotmovies.gson.MovieListDeserializer;
import com.zonkey.hotmovies.gson.ReviewListDeserializer;
import com.zonkey.hotmovies.gson.TrailerListDeserializer;
import com.zonkey.hotmovies.models.Movie;
import com.zonkey.hotmovies.models.Review;
import com.zonkey.hotmovies.models.Trailer;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieServiceFactory {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    public MovieService create() {
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(new TypeToken<List<Trailer>>() {}.getType(), new TrailerListDeserializer())
                        .registerTypeAdapter(new TypeToken<List<Movie>>() {}.getType(), new MovieListDeserializer())
                        .registerTypeAdapter(new TypeToken<List<Review>>() {}.getType(), new ReviewListDeserializer())
                        .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(MovieService.class);
    }
}
