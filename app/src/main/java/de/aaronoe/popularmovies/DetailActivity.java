package de.aaronoe.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    TextView mTitleTextView;
    ImageView mPosterImageView;
    TextView mReleaseDateTextView;
    TextView mDetailDescriptionTextView;
    TextView mRatingTextView;
    MovieItem mMovieItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mTitleTextView = (TextView) findViewById(R.id.tv_detail_title);
        mPosterImageView = (ImageView) findViewById(R.id.iv_detail_thumbnail);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_detail);
        mDetailDescriptionTextView = (TextView) findViewById(R.id.tv_detail_description);
        mRatingTextView = (TextView) findViewById(R.id.tv_rating_detail);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("MovieItem")) {

                mMovieItem = intentThatStartedThisActivity.getParcelableExtra("MovieItem");

                populateViewsWithData(mMovieItem);

            }

        }

    }

    private void populateViewsWithData(MovieItem movieItem) {

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
