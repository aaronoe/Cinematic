package de.aaronoe.cinematic.ui.Favorites;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.cinematic.Database.MoviesContract.ShowEntry;
import de.aaronoe.cinematic.Database.Utilities;
import de.aaronoe.cinematic.R;

/**
 *
 * Created by aaron on 11.04.17.
 */

public class FavoriteTvShowsAdaper extends RecyclerView.Adapter<FavoriteTvShowsAdaper.TvShowViewHolder> {

    private Context mContext;
    private TvShowAdapterOnClickHandler mClickHandler;
    private Cursor showCursor;


    public FavoriteTvShowsAdaper(Context context, TvShowAdapterOnClickHandler clickHandler, Cursor data) {
        mContext = context;
        mClickHandler = clickHandler;
        showCursor = data;


    }

    @Override
    public TvShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.shows_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new TvShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TvShowViewHolder holder, int position) {

        if (showCursor == null || showCursor.getCount() == 0) return;

        if (!showCursor.moveToPosition(position)) return;

        String showName = showCursor.getString(showCursor.getColumnIndex(ShowEntry.COLUMN_TITLE));
        double showRating = showCursor.getDouble(showCursor.getColumnIndex(ShowEntry.COLUMN_VOTE_AVERAGE));
        String firstAirDate = showCursor.getString(showCursor.getColumnIndex(ShowEntry.COLUMN_FIRST_AIR_DATE));
        String backdropPath = showCursor.getString(showCursor.getColumnIndex(ShowEntry.COLUMN_BACKDROP_PATH));
        String genreText = showCursor.getString(showCursor.getColumnIndex(ShowEntry.COLUMN_GENRES));

        holder.showTitleTextView.setText(showName);
        holder.showRatingTextView.setText(String.valueOf(showRating));
        holder.showYearTextView.setText(Utilities.convertDateToYear(firstAirDate));
        holder.showGenreTextView.setText(genreText);

        String pictureUrl = "http://image.tmdb.org/t/p/w500/" + backdropPath;

        Picasso.with(mContext)
                .load(pictureUrl)
                .placeholder(R.drawable.poster_show_loading)
                .error(R.drawable.poster_show_not_available)
                .into(holder.backdropImageView);

    }

    @Override
    public int getItemCount() {
        if (showCursor == null) return 0;
        return showCursor.getCount();
    }

    interface TvShowAdapterOnClickHandler {
        void onClick(int movieId);
    }

    public void setVideoData(Cursor data) {
        showCursor = data;
        notifyDataSetChanged();
    }


    public class TvShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_shows_backdrop_iv)
        ImageView backdropImageView;
        @BindView(R.id.tv_shows_h1_tv)
        TextView showTitleTextView;
        @BindView(R.id.tv_shows_genres_tv) TextView showGenreTextView;
        @BindView(R.id.tv_shows_rating_tv) TextView showRatingTextView;
        @BindView(R.id.tv_shows_year_tv) TextView showYearTextView;

        TvShowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            if (showCursor.moveToPosition(adapterPosition)) {
                int showID = showCursor.getInt(showCursor.getColumnIndex(ShowEntry.COLUMN_ID));
                mClickHandler.onClick(showID);
            }
        }
    }


}
