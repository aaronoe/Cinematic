package de.aaronoe.cinematic.model;


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
import de.aaronoe.cinematic.database.Utilities;
import de.aaronoe.cinematic.movies.MovieItem;
import de.aaronoe.cinematic.R;

/**
 * Adapter to populate the recyclerview with movie-data
 * Created by aaron on 22.01.17.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    // member variable for the list of movie data
    private List<MovieItem> mMovieList;
    private Context mContext;

    private MovieAdapterOnClickHandler mClickHandler;

    /**
     * Creates a MovieAdapter with a click handler
     */
    public MovieAdapter(MovieAdapterOnClickHandler onClickHandler, Context context) {
        mClickHandler = onClickHandler;
        mContext = context;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieItem movieItem, ImageView backdropImageView);
    }


    class MovieAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        @BindView(R.id.tv_shows_backdrop_iv)
        ImageView backdropImageView;
        @BindView(R.id.tv_shows_h1_tv)
        TextView showTitleTextView;
        @BindView(R.id.tv_shows_genres_tv) TextView showGenreTextView;
        @BindView(R.id.tv_shows_rating_tv) TextView showRatingTextView;
        @BindView(R.id.tv_shows_year_tv) TextView showYearTextView;


        MovieAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieItem thisMovie = mMovieList.get(adapterPosition);
            mClickHandler.onClick(thisMovie, backdropImageView);
        }
    }


    /**
     * This doc was adopted from the Sunshine App
     *
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.shows_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }


    /**
     * This doc was adopted from the Sunshine App
     *
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder                    The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        // get the data of the current movie
        MovieItem thisMovieItem = mMovieList.get(position);
        // retrieve the poster picture path
        String picturePath = thisMovieItem.getBackdropPath();
        // put the picture URL together
        String pictureUrl = "http://image.tmdb.org/t/p/w500/" + picturePath;
        // get a reference to this item's ImageView
        ImageView currentImageView = holder.backdropImageView;
        // use picasso to load the image into the view
        Picasso.with(holder.itemView.getContext())
                .load(pictureUrl)
                .placeholder(R.drawable.poster_show_loading)
                .error(R.drawable.poster_show_not_available)
                .into(currentImageView);

        holder.showTitleTextView.setText(thisMovieItem.getTitle());
        holder.showRatingTextView.setText(String.valueOf(thisMovieItem.getVoteAverage()));
        holder.showYearTextView.setText(Utilities.convertDateToYear(thisMovieItem.getReleaseDate()));
        holder.showGenreTextView.setText(Utilities.extractMovieGenres(thisMovieItem.getGenreIds(), mContext));
    }


    @Override
    public int getItemCount() {
        if (mMovieList == null) return 0;
        return mMovieList.size();
    }

    /**
     * This doc was adopted from the Sunshine App
     *
     * This method is used to set the movie data on a MovieAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new MovieAdapter to display it.
     *
     * @param movieData The new movie data to be displayed.
     */
    public void setMovieData(List<MovieItem> movieData) {
        mMovieList = movieData;
        notifyDataSetChanged();
    }

}
