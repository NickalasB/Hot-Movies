package com.zonkey.hotmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zonkey.hotmovies.adapters.MovieReviewsAdapter;
import com.zonkey.hotmovies.adapters.MovieTrailersAdapter;
import com.zonkey.hotmovies.models.Movie;
import com.zonkey.hotmovies.models.Review;
import com.zonkey.hotmovies.models.Trailer;
import com.zonkey.hotmovies.retrievers.MovieRetriever;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailFragment extends Fragment implements MovieRetriever.MovieRetrieverCallback {

    ImageView posterDetailImageView;
    ImageView backdropDetailImageView;
    TextView movieTitleTextView;
    TextView movieSummaryTextView;
    TextView movieRatingTextView;
    TextView movieTotalRatingsTextView;
    TextView movieReleaseDateTextView;
    TextView movieClickForTrailerTextView;
    TextView movieReviewAuthorTitleTextView;
    TextView movieReviewAuthorTextView;
    TextView movieReviewsTextView;
    private Movie movie;

    private RecyclerView reviewRecyclerView;
    private RecyclerView trailerRecyclerView;

    public MovieDetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        posterDetailImageView = (ImageView) rootView.findViewById(R.id.detail_poster_imageView);
        movieClickForTrailerTextView = (TextView) rootView.findViewById(R.id.detail_click_for_trailer_textView);
        backdropDetailImageView = (ImageView) rootView.findViewById(R.id.detail_backdrop_imageView);
        movieTitleTextView = (TextView) rootView.findViewById(R.id.detail_movie_title);
        movieSummaryTextView = (TextView) rootView.findViewById(R.id.detail_movie_overview);
        movieRatingTextView = (TextView) rootView.findViewById(R.id.detail_user_rating_text);
        movieTotalRatingsTextView = (TextView) rootView.findViewById(R.id.detail_vote_count_text);
        movieReleaseDateTextView = (TextView) rootView.findViewById(R.id.detail_release_date);
        movieReviewAuthorTitleTextView = (TextView) rootView.findViewById(R.id.detail_reviews_title_textView);
        movieReviewAuthorTextView = (TextView) rootView.findViewById(R.id.detail_reviews_author_textView);
        movieReviewsTextView = (TextView) rootView.findViewById(R.id.detail_reviews_textView);


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

        return rootView;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        new MovieRetriever(this).getMovie(movie.id);
    }

    @Override
    public void onMovieRetrieved(final Movie movie) {
        this.movie = movie;
        displayMovie();
    }

    @Override
    public void onError(final String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void initializeTrailerAdapter() {
        List<Trailer> trailers = movie.trailers != null ? movie.trailers : new ArrayList<Trailer>();
        MovieTrailersAdapter trailerAdapter = new MovieTrailersAdapter(getActivity(), trailers);
        trailerRecyclerView.setAdapter(trailerAdapter);
    }

    private void initializeReviewAdapter() {
        List<Review> reviews = movie.mReviews != null ? movie.mReviews : new ArrayList<Review>();
        MovieReviewsAdapter adapter = new MovieReviewsAdapter(getActivity(), reviews);
        reviewRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        displayMovie();
    }

    private void displayMovie() {
        if (movie != null) {
            Picasso.with(getContext())
                    .load(movie.getPosterURL())
                    .into(posterDetailImageView);
            movieClickForTrailerTextView.setText(R.string.details_click_for_trailer);
            movieTitleTextView.setText(movie.title);
            movieReleaseDateTextView.setText("Release date: " + movie.release_date);
            movieRatingTextView.setText("Avg. Rating: " + movie.vote_average + "/10");
            movieTotalRatingsTextView.setText("Total Ratings: " + movie.vote_count);
            movieSummaryTextView.setText(movie.overview);

            initializeReviewAdapter();
            initializeTrailerAdapter();
        }
    }
}
