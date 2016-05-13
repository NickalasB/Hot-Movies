package com.zonkey.hotmovies.retrievers;

import android.util.Log;

import com.zonkey.hotmovies.models.Movie;
import com.zonkey.hotmovies.networking.MovieService;
import com.zonkey.hotmovies.networking.MovieServiceFactory;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListRetriever implements Callback<List<Movie>> {
    private static final String TAG = "MovieListRetriever";
    private MovieService mMovieService;
    private WeakReference<MovieListRetrieverCallback> mMovieRetrieverCallbackWeakReference;

    public MovieListRetriever(MovieListRetrieverCallback callback) {
        mMovieRetrieverCallbackWeakReference = new WeakReference<>(callback);
        mMovieService = new MovieServiceFactory().create();
    }

    public void getHighestRatedMovies() {
        mMovieService.getHighestRatedMovies().enqueue(this);
    }

    public void getPopularMovies() {
        mMovieService.getPopularMovies().enqueue(this);
    }

    @Override
    public void onResponse(final Call<List<Movie>> call, final Response<List<Movie>> response) {
        if (response.isSuccessful()) {
            notifyMoviesRetrieved(response.body());
        } else {
            notifyError(response.message());
            Log.e(TAG, response.message());
        }
    }

    @Override
    public void onFailure(final Call<List<Movie>> call, final Throwable t) {
        notifyError(t.getMessage());
        Log.e(TAG, t.getMessage());
    }

    private void notifyMoviesRetrieved(final List<Movie> movies) {
        MovieListRetrieverCallback callback = mMovieRetrieverCallbackWeakReference.get();
        if (callback != null) {
            callback.onMoviesRetrieved(movies);
        }
    }

    private void notifyError(final String message) {
        MovieListRetrieverCallback callback = mMovieRetrieverCallbackWeakReference.get();
        if (callback != null) {
            callback.onError(message);
        }
    }

    public interface MovieListRetrieverCallback {
        void onMoviesRetrieved(List<Movie> movies);

        void onError(String errorMessage);
    }
}
