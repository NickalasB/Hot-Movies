package com.zonkey.hotmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.zonkey.hotmovies.adapters.MovieReviewsAdapter;
import com.zonkey.hotmovies.adapters.MovieTrailersAdapter;
import com.zonkey.hotmovies.favorites.FavoritesManager;
import com.zonkey.hotmovies.json.ReviewTags;
import com.zonkey.hotmovies.json.TrailerTags;
import com.zonkey.hotmovies.models.Movie;
import com.zonkey.hotmovies.models.Reviews;
import com.zonkey.hotmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailFragment extends Fragment {

    ImageView posterDetailImageView;
    ImageView backdropDetailImageView;
    TextView movieTitleTextView;
    TextView movieSummaryTextView;
    TextView movieRatingTextView;
    TextView movieTotalRatingsTextView;
    TextView movieReleaseDateTextView;
    ToggleButton favoriteButton;
    TextView movieScrollForTrailerTextView;
    TextView movieReviewAuthorTitleTextView;
    //    TextView movieReviewAuthorTextView;
//    TextView movieReviewsTextView;
    private Movie movie;
    private Trailer trailer;
    TextView movieReviewTitleTextView;


    private RecyclerView reviewRecyclerView;
    private RecyclerView trailerRecyclerView;

    //    private GridView trailerGridview;
    private ImageView trailerImageView;


    public MovieDetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        posterDetailImageView = (ImageView) rootView.findViewById(R.id.detail_poster_imageView);
        movieScrollForTrailerTextView = (TextView) rootView.findViewById(R.id.detail_scroll_for_trailer_textView);
        backdropDetailImageView = (ImageView) rootView.findViewById(R.id.detail_backdrop_imageView);
        movieTitleTextView = (TextView) rootView.findViewById(R.id.detail_movie_title);
        movieSummaryTextView = (TextView) rootView.findViewById(R.id.detail_movie_overview);
        movieRatingTextView = (TextView) rootView.findViewById(R.id.detail_user_rating_text);
        movieTotalRatingsTextView = (TextView) rootView.findViewById(R.id.detail_vote_count_text);
        movieReleaseDateTextView = (TextView) rootView.findViewById(R.id.detail_release_date);
        favoriteButton = (ToggleButton) rootView.findViewById(R.id.detail_favorite_button);
        movieReviewAuthorTitleTextView = (TextView) rootView.findViewById(R.id.detail_reviews_title_textView);
        movieReviewTitleTextView = (TextView) rootView.findViewById(R.id.details_review_title);
        trailerImageView = (ImageView) rootView.findViewById(R.id.trailers_imageView);


        //where we handle the reviews RecyclerView
        reviewRecyclerView = (RecyclerView) rootView.findViewById(R.id.reviews_recyclerView);
        trailerRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailer_recycler_view);


        LinearLayoutManager reviewsLm = new LinearLayoutManager(getContext());

        LinearLayoutManager trailersLm
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        trailerRecyclerView.setHasFixedSize(true);
        trailerRecyclerView.setLayoutManager(trailersLm);


        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setLayoutManager(reviewsLm);



        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(MainActivity.MOVIE)){
            movie = bundle.getParcelable(MainActivity.MOVIE);
            if (movie != null){
                setUpFavoriteButtonAndLoadMovie(movie);
            }
        }
        return rootView;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        setUpFavoriteButtonAndLoadMovie(movie);
    }

    private void setUpFavoriteButtonAndLoadMovie(Movie movie) {
        setUpFavoriteButton(movie);
        new FetchTrailerTask().execute(movie);
        new FetchReviewsTask().execute(movie);
    }


    private void setUpFavoriteButton(final Movie movie) {
        favoriteButton.setChecked(FavoritesManager.getInstance().isFavorite(movie.id, getContext()));
        favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FavoritesManager.getInstance().addFavorite(movie.id, getContext());
                } else {
                    FavoritesManager.getInstance().removeFavorite(movie.id, getContext());
                }
            }
        });
    }

    private void initializeTrailerAdapter() {
        List<Trailer> trailers = movie.trailers != null ? movie.trailers : new ArrayList<Trailer>();
        MovieTrailersAdapter trailerAdapter = new MovieTrailersAdapter(getActivity(), trailers);
        trailerRecyclerView.setAdapter(trailerAdapter);


    }

    private void initializeReviewAdapter() {
        List<Reviews> reviews = movie.reviews != null ? movie.reviews : new ArrayList<Reviews>();
        MovieReviewsAdapter adapter = new MovieReviewsAdapter(getActivity(), reviews);
        reviewRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (movie != null){
            displayMovie();
        }
    }


    private void displayMovie() {
        if (movie != null) {
            Picasso.with(getContext())
                    .load(movie.getPosterURL())
                    .into(posterDetailImageView);
            Picasso.with(getContext())
                    .load(movie.getBackdropURL())
                    .into(backdropDetailImageView);
            movieScrollForTrailerTextView.setText(R.string.details_scroll_for_trailer);
            movieTitleTextView.setText(movie.title);
            movieReleaseDateTextView.setText("Release date: " + movie.release_date);
            movieRatingTextView.setText("Avg. Rating: " + movie.vote_average + "/10");
            movieTotalRatingsTextView.setText("Total Ratings: " + movie.vote_count);
            movieSummaryTextView.setText(movie.overview);
        }
        initializeReviewAdapter();
        initializeTrailerAdapter();
    }




    public class FetchTrailerTask extends AsyncTask<Movie, Void, List<Trailer>> {

        private final String LOG_TAG = FetchTrailerTask.class.getSimpleName();
        private Movie mMovie;

        private List<Trailer> getTrailerDataFromJson(String trailerInfoJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TRAILER_KEY = "key";

            JSONObject jsonTrailerInfo = new JSONObject(trailerInfoJsonStr);
            JSONArray trailerArray = jsonTrailerInfo.getJSONArray(TrailerTags.RESULTS);

            List<Trailer> trailerList = new ArrayList<>();
            for (int i = 0; i < trailerArray.length(); i++) {
                // for every object in this array we need to create a movie object
                JSONObject trailerObject = trailerArray.getJSONObject(i);
                Trailer trailer = new Trailer(
                        trailerObject.getString(TRAILER_KEY));
                //this adds it all up into the big ol object we just created
                trailerList.add(trailer);
            }

            return trailerList;


        }

        @Override
        protected List<Trailer> doInBackground(Movie... params) {

            if (params.length == 0) {
                return null;
            }

            mMovie = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String trailerJsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/";
                //"http://api.themoviedb.org/3/movie/[movieID]/videos";
                final String VIDEOS_PARAM = "videos";
                final String API_KEY_PARAM = "api_key";


                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(mMovie.id)
                        .appendPath(VIDEOS_PARAM)
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                        .build();


                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built Trailer URL:" + builtUri);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                trailerJsonStr = buffer.toString();


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getTrailerDataFromJson(trailerJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            mMovie.setTrailers(trailers);
            displayMovie();


        }

    }


    public class FetchReviewsTask extends AsyncTask<Movie, Void, List<Reviews>> {

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();
        private Movie mMovie;

        private List<Reviews> getReviewsDataFromJson(String reviewsInfoJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String REVIEWS_AUTHOR = "author";
            final String REVIEWS_CONTENT = "content";


            JSONObject jsonReviewsInfo = new JSONObject(reviewsInfoJsonStr);
            JSONArray reviewsArray = jsonReviewsInfo.getJSONArray(ReviewTags.RESULTS);

            List<Reviews> reviewsList = new ArrayList<>();
            for (int i = 0; i < reviewsArray.length(); i++) {
                // for every object in this array we need to create a movie object
                JSONObject reviewsObject = reviewsArray.getJSONObject(i);
                Reviews reviews = new Reviews(
                        reviewsObject.getString(REVIEWS_AUTHOR),
                        reviewsObject.getString(REVIEWS_CONTENT));
                //this adds it all up into the big ol object we just created
                reviewsList.add(reviews);
            }

            return reviewsList;


        }

        @Override
        protected List<Reviews> doInBackground(Movie... params) {

            if (params.length == 0) {
                return null;
            }

            mMovie = params[0];


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String reviewsJsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/";
                //"http://api.themoviedb.org/3/movie/[movieID]/videos";
                final String REVIEWS_PARAM = "reviews";
                final String API_KEY_PARAM = "api_key";


                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(mMovie.id)
                        .appendPath(REVIEWS_PARAM)
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                        .build();


                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built Reviews URL:" + builtUri);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                reviewsJsonStr = buffer.toString();


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getReviewsDataFromJson(reviewsJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(List<Reviews> reviews) {

            mMovie.setReviews(reviews);
            if (movie.reviews.size() == 0) {
                movieReviewTitleTextView.setText(R.string.details_no_reviews_title);
            }
            if (movie.trailers.size() == 0) {
                movieScrollForTrailerTextView.setText(R.string.details_no_trailers_textview);
            }
            displayMovie();

        }

    }
}
