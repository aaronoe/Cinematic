package de.aaronoe.popularmovies.ui.Favorites;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.Database.MoviesContract.ShowEntry;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.R;

/**
 *
 * Created by aaron on 11.04.17.
 */

public class FavoriteTvShowsAdaper extends RecyclerView.Adapter<FavoriteTvShowsAdaper.TvShowViewHolder> {

    private Context mContext;
    private Map<Integer,String> map;
    private TvShowAdapterOnClickHandler mClickHandler;
    private Cursor showCursor;


    public FavoriteTvShowsAdaper(Context context, TvShowAdapterOnClickHandler clickHandler, Cursor data) {
        mContext = context;
        mClickHandler = clickHandler;
        showCursor = data;

        // get genre mappings
        map = new HashMap<Integer,String>();
        map.put(10759, "Action & Adventure");
        map.put(16, "Animation");
        map.put(35, "Comedy");
        map.put(80, "Crime");
        map.put(99, "Documentary");
        map.put(18, "Drama");
        map.put(10751, "Family");
        map.put(10752, "Kids");
        map.put(9648, "Mystery");
        map.put(10763, "News");
        map.put(10764, "Reality");
        map.put(10765, "Sci-Fi & Fantasy");
        map.put(10766, "Soap");
        map.put(10767, "Talk");
        map.put(10768, "War & Politics");
        map.put(37, "Western");

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
