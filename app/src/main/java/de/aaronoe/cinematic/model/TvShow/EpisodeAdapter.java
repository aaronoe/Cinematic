package de.aaronoe.cinematic.model.TvShow;

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
import de.aaronoe.cinematic.model.TvShow.FullSeason.Episode;
import de.aaronoe.cinematic.database.Utilities;
import de.aaronoe.cinematic.R;

/**
 *
 * Created by aaron on 04.04.17.
 */

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {

    List<Episode> episodeList;
    private Context mContext;

    public EpisodeAdapter(Context context) {mContext = context;}

    @Override
    public EpisodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.tv_episodes_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EpisodeViewHolder holder, int position) {

        Episode mEpisode = episodeList.get(position);

        Integer episodeNumber = mEpisode.getEpisodeNumber();
        if (episodeNumber != null) {
            holder.episodeNumberTextView.setText(String.valueOf(episodeNumber));
        }
        holder.episodeTitleTextView.setText(mEpisode.getName());
        holder.episodeReleaseTextView.setText(Utilities.convertDate(mEpisode.getAirDate()));
        holder.episodeOverviewTextView.setText(mEpisode.getOverview());

        String creatorProfileUrl = "http://image.tmdb.org/t/p/w342/" + mEpisode.getStillPath();
        Picasso.with(mContext)
                .load(creatorProfileUrl)
                .placeholder(R.drawable.poster_show_loading)
                .error(R.drawable.poster_show_not_available)
                .into(holder.posterImageView);

    }

    @Override
    public int getItemCount() {
        if (episodeList == null) return 0;
        return episodeList.size();
    }

    public void setEpisodeList(List<Episode> episodes) {
        episodeList = episodes;
        notifyDataSetChanged();
    }

    class EpisodeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.episode_poster_imageview)
        ImageView posterImageView;
        @BindView(R.id.episode_number_tv)
        TextView episodeNumberTextView;
        @BindView(R.id.episode_title_tv)
        TextView episodeTitleTextView;
        @BindView(R.id.episode_release_date)
        TextView episodeReleaseTextView;
        @BindView(R.id.episode_overview)
        TextView episodeOverviewTextView;

        public EpisodeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
