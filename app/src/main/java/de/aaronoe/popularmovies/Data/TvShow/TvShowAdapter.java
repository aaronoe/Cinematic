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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.R;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

/**
 *
 * Created by aaron on 26.03.17.
 */

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {

    private List<TvShow> videoItemList;
    private Context mContext;
    private Map<Integer,String> map;
    private TvShowAdapterOnClickHandler mClickHandler;


    public TvShowAdapter(Context context, TvShowAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;

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

    public interface TvShowAdapterOnClickHandler {
        void onClick(int movieId);
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

        TvShow thisItem = videoItemList.get(position);

        holder.showRatingTextView.setText(String.valueOf(thisItem.getVoteAverage()));
        holder.showTitleTextView.setText(thisItem.getName());
        holder.showYearTextView.setText(Utilities.convertDateToYear(thisItem.getFirstAirDate()));
        holder.showGenreTextView.setText(extractGenres(thisItem.getGenreIds()));

        String picturePath = thisItem.getBackdropPath();

        String pictureUrl = "http://image.tmdb.org/t/p/w500/" + picturePath;


        Picasso.with(mContext)
                .load(pictureUrl)
                .placeholder(R.drawable.poster_show_loading)
                .error(R.drawable.poster_show_not_available)
                .into(holder.backdropImageView);

    }

    @Override
    public int getItemCount() {
        if (videoItemList == null) return 0;
        return videoItemList.size();
    }

    public void setVideoData(List<TvShow> videoData) {
        videoItemList = videoData;
        notifyDataSetChanged();
    }


    class TvShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_shows_backdrop_iv) ImageView backdropImageView;
        @BindView(R.id.tv_shows_h1_tv) TextView showTitleTextView;
        @BindView(R.id.tv_shows_genres_tv) TextView showGenreTextView;
        @BindView(R.id.tv_shows_rating_tv) TextView showRatingTextView;
        @BindView(R.id.tv_shows_year_tv) TextView showYearTextView;

        public TvShowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            TvShow thisShow = videoItemList.get(adapterPosition);
            mClickHandler.onClick(thisShow.getId());
        }
    }

    private String extractGenres(List<Integer> genres) {

        final String SEPARATOR = ", ";
        if (genres == null || genres.size() == 0) return null;

        List<String> result = new ArrayList<>();

        for (int id : genres) {
            if (map.containsKey(id)) {
                result.add(map.get(id));
            } else {
                Log.d(TAG, "extractGenres: " + id);
            }
        }

        StringBuilder resBuilder = new StringBuilder();

        for (String item : result) {
            resBuilder.append(item);
            resBuilder.append(SEPARATOR);
        }

        String list = resBuilder.toString();
        if (list.length() == 0) return mContext.getString(R.string.genre_not_available);
        return list.substring(0, list.length() - SEPARATOR.length());
    }

}
