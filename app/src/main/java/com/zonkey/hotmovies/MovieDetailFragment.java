package com.zonkey.hotmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zonkey.hotmovies.adapters.MovieReviewsAdapter;
import com.zonkey.hotmovies.adapters.MovieTrailersAdapter;
import com.zonkey.hotmovies.models.Movie;
import com.zonkey.hotmovies.models.Reviews;
import com.zonkey.hotmovies.models.Trailer;

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
    TextView movieClickForTrailerTextView;
    TextView movieReviewAuthorTitleTextView;
    TextView movieReviewAuthorTextView;
    TextView movieReviewsTextView;
    private Movie movie;
    private Trailer trailer;

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
        trailerImageView = (ImageView) rootView.findViewById(R.id.trailers_imageView);


        //where we handle the reviews RecyclerView
        reviewRecyclerView = (RecyclerView) rootView.findViewById(R.id.reviews_recyclerView);
        trailerRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailer_recycler_view);


        LinearLayoutManager reviewsLm = new LinearLayoutManager(getContext());
//        LinearLayoutManager trailersLm = new LinearLayoutManager(getContext());

        LinearLayoutManager trailersLm
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        trailerRecyclerView.setHasFixedSize(true);
        trailerRecyclerView.setLayoutManager(trailersLm);


        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setLayoutManager(reviewsLm);


//        trailerGridview = (GridView) rootView.findViewById(R.id.trailers_gridView);


        posterDetailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posterDetailImageView != null) {
//                    Intent trailerIntent = new Intent(getActivity(), MovieTrailerActivity.class);
                    Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                    trailerIntent.setData(Uri.parse(movie.getTrailerUrl()));
                    startActivity(trailerIntent);
                }
            }
        });

        return rootView;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        initializeReviewAdapter();
        initializeTrailerAdapter();
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
            if (trailer != null) {
                Picasso.with(getContext())
                        .load(trailer.getTrailerImagerURL())
                        .into(trailerImageView);
            }


        }
    }
}
