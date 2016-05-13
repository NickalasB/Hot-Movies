package com.zonkey.hotmovies.retrievers;

import android.util.Log;

import com.zonkey.hotmovies.models.Movie;
import com.zonkey.hotmovies.networking.MovieService;
import com.zonkey.hotmovies.networking.MovieServiceFactory;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRetriever implements Callback<Movie> {

    private static final String TAG = "MovieListRetriever";
    private MovieService mMovieService;
    private WeakReference<MovieRetrieverCallback> mMovieRetrieverCallbackWeakReference;

    public MovieRetriever(MovieRetrieverCallback callback) {
        mMovieRetrieverCallbackWeakReference = new WeakReference<>(callback);
        mMovieService = new MovieServiceFactory().create();
    }

    public void getMovie(String id) {
        mMovieService.getMovie(id).enqueue(this);
    }

    @Override
    public void onResponse(final Call<Movie> call, final Response<Movie> response) {
        if (response.isSuccessful()) {
            notifyMovieRetrieved(response.body());
        } else {
            notifyError(response.message());
            Log.e(TAG, response.message());
        }
    }

    @Override
    public void onFailure(final Call<Movie> call, final Throwable t) {
        notifyError(t.getMessage());
        Log.e(TAG, t.getMessage());
    }

    private void notifyMovieRetrieved(Movie movie) {
        MovieRetrieverCallback callback = mMovieRetrieverCallbackWeakReference.get();
        if (callback != null) {
            callback.onMovieRetrieved(movie);
        }
    }

    private void notifyError(final String message) {
        MovieRetrieverCallback callback = mMovieRetrieverCallbackWeakReference.get();
        if (callback != null) {
            callback.onError(message);
        }
    }

    public interface MovieRetrieverCallback {
        void onMovieRetrieved(Movie movie);

        void onError(String errorMessage);
    }
}
