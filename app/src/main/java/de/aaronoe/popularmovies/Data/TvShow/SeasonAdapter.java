package de.aaronoe.popularmovies.Data.TvShow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.Data.TvShow.FullShow.Season;
import de.aaronoe.popularmovies.R;

/**
 *
 * Created by aaron on 04.04.17.
 */

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>{

    private List<Season> seasonList;
    private Context mContext;

    public SeasonAdapter(Context context) {
        mContext = context;
    }


    @Override
    public SeasonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.tv_season_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new SeasonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SeasonViewHolder holder, int position) {
        Season mSeason = seasonList.get(position);

        String creatorProfileUrl = "http://image.tmdb.org/t/p/w185/" + mSeason.getPosterPath();
        Picasso.with(mContext)
                .load(creatorProfileUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.posterImageView);

        holder.episodeCountTextView.setText(mContext.getString(R.string.nr_episodes, mSeason.getEpisodeCount()));
        String metaData = mContext.getString(
                R.string.season_and_year,
                mSeason.getSeasonNumber());
        holder.seasonNumberTextView.setText(metaData);
    }

    public void setSeasonList(List<Season> seasons) {
        seasonList = seasons;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (seasonList == null) return 0;
        return seasonList.size();
    }

    class SeasonViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.poster_imageview)
        ImageView posterImageView;
        @BindView(R.id.season_number)
        TextView seasonNumberTextView;
        @BindView(R.id.episode_count_tv)
        TextView episodeCountTextView;

        SeasonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
