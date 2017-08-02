package de.aaronoe.cinematic.ui.showdetail;

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
import de.aaronoe.cinematic.model.TvShow.TvShow;
import de.aaronoe.cinematic.ui.redesign.showdetail.ShowDetailActivity;
import de.aaronoe.cinematic.util.Constants;

/**
 * Created by private on 7/23/17.
 *
 */

public class SimilarShowsAdapter extends RecyclerView.Adapter<SimilarShowsAdapter.SimilarMoviesViewHolder> {

    private List<TvShow> tvShowList;
    private Context mContext;

    public SimilarShowsAdapter(Context context) {
        mContext = context;
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

        final TvShow tvShow = tvShowList.get(position);

        String picPath = tvShow.getPosterPath();
        String pictureUrl = "http://image.tmdb.org/t/p/w185/" + picPath;


        Picasso.with(mContext)
                .load(pictureUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.posterImageView);

        final ImageView posterIv = holder.posterImageView;

        holder.posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToStartDetailActivity = new Intent(mContext, ShowDetailActivity.class);
                intentToStartDetailActivity.putExtra(mContext.getString(R.string.INTENT_KEY_TV_SHOW_ITEM), tvShow);
                intentToStartDetailActivity.putExtra(mContext.getString(R.string.intent_transition_enter_mode), Constants.NONE);
                mContext.startActivity(intentToStartDetailActivity);

/*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((TvShowDetailActivity) mContext, posterIv, mContext.getString(R.string.transition_key_show_poster));
                    mContext.startActivity(intent, options.toBundle());
                } else {
                    mContext.startActivity(intent);
                } */
            }
        });

    }

    public void setShowData(List<TvShow> data) {
        tvShowList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (tvShowList == null) {
            return 0;
        }
        return tvShowList.size();
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
