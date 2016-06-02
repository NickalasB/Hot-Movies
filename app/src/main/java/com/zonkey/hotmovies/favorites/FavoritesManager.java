package com.zonkey.hotmovies.favorites;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nickbradshaw on 5/15/16.
 */
public class FavoritesManager {
    public static final String FAVORITE = "Favorite";
    private static FavoritesManager ourInstance = new FavoritesManager();
    private Set<String> mMovieIds = new HashSet<>();

    public static FavoritesManager getInstance() {
        return ourInstance;
    }

    private FavoritesManager() {
    }

    public void addFavorite(String movieId, Context context) {
        makeSureFavoritesAreLoaded(context);
        mMovieIds.add(movieId);
        saveToSharedPrefs(context);
    }

    private void makeSureFavoritesAreLoaded(Context context) {
        if (mMovieIds == null || mMovieIds.isEmpty()) {
            mMovieIds = getFavoritesFromSharedPrefs(context);
        }
    }

    private Set<String> getFavoritesFromSharedPrefs(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getStringSet(FAVORITE, new HashSet<String>());
    }

    public void removeFavorite(String movieId, Context context) {
        makeSureFavoritesAreLoaded(context);
        mMovieIds.remove(movieId);
        saveToSharedPrefs(context);
    }

    public boolean isFavorite(String movieId, Context context) {
        makeSureFavoritesAreLoaded(context);
        return mMovieIds.contains(movieId);
    }

    private void saveToSharedPrefs(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(FAVORITE, mMovieIds);
        editor.apply();
    }

    public Set<String> getFavoriteMovies(Context context){
        makeSureFavoritesAreLoaded(context);
        return new HashSet<>(mMovieIds);
    }
}
