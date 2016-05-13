package com.zonkey.hotmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zonkey.hotmovies.R;
import com.zonkey.hotmovies.models.Movie;
import com.zonkey.hotmovies.models.Trailer;

import java.util.List;

/**
 * Created by nickbradshaw on 5/9/16.
 */
public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.ViewHolder> {

    private Context mContext;
    private List<Trailer> mTrailerList;
    private Movie movie;
    private Trailer trailer;


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
        Picasso.with(mContext)
                .load(mTrailerList.get(position).getTrailerImagerURL())
                .resize(160, 120)
                .into((viewHolder.trailerImageView));

    }



    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView trailerImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            trailerImageView = (ImageView) itemView.findViewById(R.id.trailers_imageView);

        }
        


        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "Look at that bitch- a toast", Toast.LENGTH_SHORT).show();
//                Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
//                trailerIntent.setData(Uri.parse(trailer.getTrailerURL()));
//                mContext.startActivity(trailerIntent);
        }
    }

}

