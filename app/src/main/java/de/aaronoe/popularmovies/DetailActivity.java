package de.aaronoe.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.Database.MoviesContract;
import de.aaronoe.popularmovies.Database.MoviesContract.MovieEntry;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.Movies.MovieItem;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @BindView(R.id.tv_detail_title) TextView mTitleTextView;
    @BindView(R.id.iv_detail_thumbnail) ImageView mPosterImageView;
    @BindView(R.id.tv_release_detail) TextView mReleaseDateTextView;
    @BindView(R.id.tv_detail_description) TextView mDetailDescriptionTextView;
    @BindView(R.id.tv_rating_detail) TextView mRatingTextView;
    @BindView(R.id.toggleButton) ToggleButton mToggleButton;
    MovieItem mMovieItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("MovieItem")) {

                mMovieItem = intentThatStartedThisActivity.getParcelableExtra("MovieItem");

                populateViewsWithData(mMovieItem);

            }

        }

    }

    /**
     * This method populates the views with data
     * @param movieItem data to be used to populate the views
     */
    private void populateViewsWithData(final MovieItem movieItem) {

        // Check if movie already is a favorite
        if (isMovieFavorite(movieItem)) {
            Log.e("function called", "test");
            mToggleButton.setTextOn("Favorited");
            mToggleButton.setChecked(true);
        } else {
            mToggleButton.setTextOff("Favorite");
            mToggleButton.setChecked(false);
        }


        // Set Title
        mTitleTextView.setText(movieItem.getmTitle());

        // Set Movie Poster
        String picturePath = movieItem.getmPosterPath();
        // put the picture URL together
        String pictureUrl = "http://image.tmdb.org/t/p/w185/" + picturePath;
        Picasso.with(DetailActivity.this).load(pictureUrl).into(mPosterImageView);

        // Release Date
        mReleaseDateTextView.setText(convertDate(movieItem.getmReleaseDate()));

        mDetailDescriptionTextView.setText(movieItem.getmMovieDescription());

        String ratingText = movieItem.getmVoteAverage() + "/10";
        mRatingTextView.setText(ratingText);

        int movieId = movieItem.getmMovieId();

        // Toggle button for favorite db
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // Toggle is enabled

                    Uri uri = getContentResolver().insert(
                            MovieEntry.CONTENT_URI,
                            Utilities.getContentValuesForMovie(movieItem));

                    Toast.makeText(DetailActivity.this, "Item inserted for: " + uri, Toast.LENGTH_SHORT).show();


                } else {

                    String[] selection = new String[]{Integer.toString(movieItem.getmMovieId())};

                    int numberOfItemsDeleted;
                    int movieId = movieItem.getmMovieId();
                    Uri deleteUri = MovieEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(movieId)).build();


                    numberOfItemsDeleted = getContentResolver().delete(
                            deleteUri,
                            null,
                            null
                    );

                    if (numberOfItemsDeleted > 0) Log.e("DetailActivity: ", "Items deleted: " + numberOfItemsDeleted);

                }
            }
        });

    }


    /**
     * Checks if this movie is already a user's favorite
     * @param movieItem
     * @return true if it is, false if it's not a favorite
     */
    private boolean isMovieFavorite(MovieItem movieItem) {

        int id = movieItem.getmMovieId();
        String[] selection = new String[]{Integer.toString(movieItem.getmMovieId())};

        Cursor result =
                getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                MovieEntry.COLUMN_MOVIE_ID + "=?",
                selection,
                null);

        Log.e("Detail: ", "" + result.getCount());
        Log.e("Detail", "" + (result != null && result.getCount() > 0));
        return (result != null && result.getCount() > 0);

    }


    /**
     * Converts a date returned by the API into a different format.
     *
     * @param sourceDate a string representing a date in this format: 2015-12-15
     * @return a string representing a date in this format: December 15, 2015
     */
    private String convertDate(String sourceDate) {

        DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

        Date date = null;
        try {
            date = sourceFormat.parse(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Error formatting the date");
        }
        return targetFormat.format(date);

    }


}
