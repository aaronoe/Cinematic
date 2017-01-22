package de.aaronoe.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

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
        mReleaseDateTextView.setText(movieItem.getmReleaseDate());

        mDetailDescriptionTextView.setText(movieItem.getmMovieDescription());

        String ratingText = "Rating: " + movieItem.getmVoteAverage() + "/10";
        mRatingTextView.setText(ratingText);

    }


}
