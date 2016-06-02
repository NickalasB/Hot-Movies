package com.zonkey.hotmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zonkey.hotmovies.R;
import com.zonkey.hotmovies.models.Trailer;

import java.util.List;

/**
 * Created by nickbradshaw on 5/9/16.
 */
public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.ViewHolder> {

    private Context mContext;
    private List<Trailer> mTrailerList;


    public MovieTrailersAdapter(Context movieContext, List<Trailer> trailerList) {
        this.mContext = movieContext;
        this.mTrailerList = trailerList;

    }

    @Override
    public MovieTrailersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_trailers_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.display(mTrailerList.get(position));

    }


    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView trailerImageView;

        public void display(final Trailer trailer) {
            Picasso.with(mContext)
                    .load(trailer.getTrailerImagerURL())
                    .into(trailerImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                    trailerIntent.setData(Uri.parse(trailer.getTrailerURL()));
                    mContext.startActivity(trailerIntent);
                }
            });

        }


        public ViewHolder(View itemView) {
            super(itemView);
            trailerImageView = (ImageView) itemView.findViewById(R.id.trailers_imageView);
        }
    }
}

