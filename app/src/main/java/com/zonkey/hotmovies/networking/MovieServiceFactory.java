package com.zonkey.hotmovies.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieServiceFactory {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public MovieService create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MovieService.class);
    }
}
