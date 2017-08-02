package de.aaronoe.cinematic.ui.redesign.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.cinematic.R;
import de.aaronoe.cinematic.movies.MovieItem;
import de.aaronoe.cinematic.util.Constants;

/**
 * Created by private on 8/2/17.
 *
 */


public class SimilarMoviesAdapter extends RecyclerView.Adapter<SimilarMoviesAdapter.SimilarMoviesViewHolder> {

    private List<MovieItem> movieList;
    private Context mContext;

    public SimilarMoviesAdapter(Context context) {
        mContext = context;
    }

    public void setMovieList(List<MovieItem> movieList) {
        this.movieList = movieList;
    }

    @Override
    public SimilarMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.similar_show_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new SimilarMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimilarMoviesViewHolder holder, int position) {

        final MovieItem movieItem = movieList.get(position);

        String picPath = movieItem.getPosterPath();
        String pictureUrl = "http://image.tmdb.org/t/p/w185/" + picPath;


        Picasso.with(mContext)
                .load(pictureUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.posterImageView);


        holder.posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToStartDetailActivity = new Intent(mContext, MovieDetailActivity.class);
                intentToStartDetailActivity.putExtra(mContext.getString(R.string.INTENT_KEY_MOVIE), movieItem);
                intentToStartDetailActivity.putExtra(mContext.getString(R.string.intent_transition_enter_mode), Constants.NONE);
                mContext.startActivity(intentToStartDetailActivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        }
        return movieList.size();
    }

    class SimilarMoviesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.poster_imageview)
        ImageView posterImageView;

        SimilarMoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
