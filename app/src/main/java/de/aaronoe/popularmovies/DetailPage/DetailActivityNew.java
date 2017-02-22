package de.aaronoe.popularmovies.DetailPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.Movies.MovieItem;
import de.aaronoe.popularmovies.R;

/**
 *
 * Created by aaron on 21.02.17.
 */

public class DetailActivityNew extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener{

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    private int mMaxScrollSize;
    private boolean activityClosed = false;
    private Context mContext = this;


    @BindView(R.id.detailpage_profile_backdrop) ImageView mBackDropImageView;
    @BindView(R.id.detailpage_profile_image) ImageView mProfileImageView;
    @BindView(R.id.detailpage_movie_title) TextView mMovieTitleTextView;
    @BindView(R.id.detailpage_tabs) TabLayout mTabLayout;
    @BindView(R.id.detailpage_viewpager) ViewPager mViewPager;
    @BindView(R.id.detailpage_appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.detailpage_toolbar) Toolbar mToolBar;

    MovieItem mMovieItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_coord);

        ButterKnife.bind(this);

        mAppBarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = mAppBarLayout.getTotalScrollRange();
        Log.e(DetailActivityNew.class.getSimpleName(), "Opened detailacitivity");

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("MovieItem")) {
                mMovieItem = intentThatStartedThisActivity.getParcelableExtra("MovieItem");
                Log.e(DetailActivityNew.class.getSimpleName(), mMovieItem.getmBackdropPath());

                populateViewsWithData();
            }
        }

        mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), mMovieItem));
        mTabLayout.setupWithViewPager(mViewPager);

    }

    public void populateViewsWithData() {

        // Set Title
        mMovieTitleTextView.setText(mMovieItem.getmTitle());

        // Set Movie Poster
        String picturePath = mMovieItem.getmPosterPath();
        // put the picture URL together
        String pictureUrl = "http://image.tmdb.org/t/p/w185/" + picturePath;
        Picasso.with(this).load(pictureUrl).into(mProfileImageView);

        // Set Movie Backdrop
        String backdropPath = mMovieItem.getmBackdropPath();
        // put the picture URL together
        String backdropUrl = "http://image.tmdb.org/t/p/w500/" + backdropPath;
        Log.e("backdrop: ", backdropPath);
        Picasso.with(this).load(backdropUrl).into(mBackDropImageView);

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            mProfileImageView.animate().scaleY(0).scaleX(0).setDuration(200).start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImageView.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }
}
