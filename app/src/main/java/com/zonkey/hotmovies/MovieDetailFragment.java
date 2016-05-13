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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailFragment extends Fragment implements MovieRetriever.MovieRetrieverCallback {

    @BindView(R.id.detail_poster_imageView)
    ImageView posterDetailImageView;
    @BindView(R.id.detail_backdrop_imageView)
    ImageView backdropDetailImageView;
    @BindView(R.id.detail_movie_title)
    TextView movieTitleTextView;
    @BindView(R.id.detail_movie_overview)
    TextView movieSummaryTextView;
    @BindView(R.id.detail_user_rating_text)
    TextView movieRatingTextView;
    @BindView(R.id.detail_vote_count_text)
    TextView movieTotalRatingsTextView;
    @BindView(R.id.detail_release_date)
    TextView movieReleaseDateTextView;
    @BindView(R.id.detail_click_for_trailer_textView)
    TextView movieClickForTrailerTextView;
    @BindView(R.id.reviews_recyclerView)
    RecyclerView reviewRecyclerView;
    @BindView(R.id.trailer_recycler_view)
    RecyclerView trailerRecyclerView;

    private Movie movie;

    public MovieDetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, rootView);

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
