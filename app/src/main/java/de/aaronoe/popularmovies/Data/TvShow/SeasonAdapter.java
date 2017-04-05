package de.aaronoe.popularmovies.Data.TvShow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.R;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

/**
 *
 * Created by aaron on 04.04.17.
 */

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>{

    private List<Season> seasonList;
    private Context mContext;
    private SeasonAdapterOnClickHandler seasonAdapterOnClickHandler;

    public SeasonAdapter(Context context, SeasonAdapterOnClickHandler clickHandler) {
        mContext = context;
        seasonAdapterOnClickHandler = clickHandler;
    }

    public interface SeasonAdapterOnClickHandler {
        void onClick(int seasonNumber);
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

        String episodeCount = mContext.getString(R.string.nr_episodes, mSeason.getEpisodeCount());
        holder.episodeCountTextView.setText(episodeCount);
        int seasonNumber = mSeason.getSeasonNumber();

        String metaData;
        if (seasonNumber == 0) {
            metaData = mContext.getString(R.string.extras_and_year);
        } else {
            metaData = mContext.getString(
                    R.string.episodes_and_year,
                    mSeason.getSeasonNumber(),
                    Utilities.convertDateToYear(mSeason.getAirDate()));
        }

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

    class SeasonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.poster_imageview)
        ImageView posterImageView;
        @BindView(R.id.season_number)
        TextView seasonNumberTextView;
        @BindView(R.id.episode_count_tv)
        TextView episodeCountTextView;

        SeasonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick() called with: v = [" + v + "]");
            int adapterPosition = getAdapterPosition();
            Season item = seasonList.get(adapterPosition);
            seasonAdapterOnClickHandler.onClick(item.getSeasonNumber());
        }
    }

}
