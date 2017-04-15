package de.aaronoe.cinematic.ui.Favorites;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.cinematic.Database.MoviesContract;
import de.aaronoe.cinematic.R;

/**
 *
 * Created by aaron on 12.04.17.
 */

class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMovieAdapterViewHolder> {

    private Context mContext;
    private MovieAdapterOnClickHandler mClickHandler;
    private Cursor movieCursor;

    public FavoriteMoviesAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public FavoriteMovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new FavoriteMovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteMovieAdapterViewHolder holder, int position) {


        if (!movieCursor.moveToPosition(position)) return;

        String picturePath = movieCursor.getString
                (movieCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH));
        // put the picture URL together
        String pictureUrl = "http://image.tmdb.org/t/p/w342/" + picturePath;
        // get a reference to this item's ImageView
        ImageView currentImageView = holder.mImageView;
        // use picasso to load the image into the view
        Picasso.with(holder.itemView.getContext())
                .load(pictureUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(currentImageView);

    }

    @Override
    public int getItemCount() {
        if (movieCursor == null || movieCursor.getCount() == 0) return 0;
        return movieCursor.getCount();
    }

    interface MovieAdapterOnClickHandler {
        void onClick(int movieId, String movieName);
    }

    void changeCursor(Cursor data) {
        if (data == null) return;
        movieCursor = data;
    }

    class FavoriteMovieAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{


        @BindView(R.id.iv_movie_thumbnail)
        ImageView mImageView;


        FavoriteMovieAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            if (movieCursor.moveToPosition(adapterPosition)) {
                int movieId = movieCursor.getInt(movieCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID));
                String movieName = movieCursor.getString(movieCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE));
                mClickHandler.onClick(movieId, movieName);
            }
        }
    }

}
