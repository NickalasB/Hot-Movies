package com.zonkey.hotmovies.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zonkey.hotmovies.models.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoritesRepository {

    private static final String FAVORITES = "Favorites";
    private static FavoritesRepository ourInstance = new FavoritesRepository();

    private List<Movie> mFavoriteMovies = new ArrayList<>();

    public static FavoritesRepository getInstance() {
        return ourInstance;
    }

    private FavoritesRepository() {
    }

    public List<Movie> getFavoriteMovies(Context context){
        if(mFavoriteMovies == null || mFavoriteMovies.isEmpty()){
            mFavoriteMovies = getFavoriteMoviesFromSharedPreferences(context);
        }
        return new ArrayList<>(mFavoriteMovies);
    }

    private List<Movie> getFavoriteMoviesFromSharedPreferences(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonFavorites = sharedPreferences.getString(FAVORITES, "");
        if(!jsonFavorites.isEmpty()){
            Type type = new TypeToken<List<Movie>>(){}.getType();
            return new Gson().fromJson(jsonFavorites, type);
        }
        return new ArrayList<>();
    }

    public void add(Movie movie, Context context){
        getFavoriteMovies(context);
        mFavoriteMovies.add(0, movie);
        saveFavoritesToSharedPreferences(context);
    }

    private void saveFavoritesToSharedPreferences(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FAVORITES, new Gson().toJson(mFavoriteMovies));
        editor.apply();
    }

    public void remove(Movie movie, Context context){
        getFavoriteMovies(context);
        mFavoriteMovies.remove(movie);
        saveFavoritesToSharedPreferences(context);
    }
}
