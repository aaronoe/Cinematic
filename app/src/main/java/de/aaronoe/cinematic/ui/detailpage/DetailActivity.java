package de.aaronoe.cinematic.ui.detailpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.cinematic.BuildConfig;
import de.aaronoe.cinematic.movies.MovieItem;
import de.aaronoe.cinematic.CinematicApp;
import de.aaronoe.cinematic.R;
import de.aaronoe.cinematic.model.remote.ApiInterface;
import de.aaronoe.cinematic.model.FullMovie.FullMovie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by aaron on 21.02.17.
 */

public class DetailActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener{

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    private int mMaxScrollSize;

    @BindView(R.id.detailpage_profile_backdrop) ImageView mBackDropImageView;
    @BindView(R.id.detailpage_profile_image) ImageView mProfileImageView;
    @BindView(R.id.detailpage_movie_title) TextView mMovieTitleTextView;
    @BindView(R.id.detailpage_tabs) TabLayout mTabLayout;
    @BindView(R.id.detailpage_viewpager) ViewPager mViewPager;
    @BindView(R.id.detailpage_appbar) AppBarLayout mAppBarLayout;

    String movieName;
    MovieItem mMovieItem;
    FullMovie mFullMovie;
    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    int id;
    @Inject ApiInterface apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_coord);

        ButterKnife.bind(this);

        ((CinematicApp) getApplication()).getNetComponent().inject(this);

        mAppBarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = mAppBarLayout.getTotalScrollRange();

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("MovieItem")) {
                mMovieItem = intentThatStartedThisActivity.getParcelableExtra("MovieItem");
                id = mMovieItem.getId();
                Log.e(DetailActivity.class.getSimpleName(), ""+id);
                populateViewsWithData();
                setUpViewPager();
            }
            if (intentThatStartedThisActivity.hasExtra("MovieId")) {
                id = intentThatStartedThisActivity.getIntExtra("MovieId", -1);
            }
            if (intentThatStartedThisActivity.hasExtra(getString(R.string.intent_key_movie_name))) {
                movieName = intentThatStartedThisActivity.getStringExtra(getString(R.string.intent_key_movie_name));
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(movieName);
                }
            }
        }
        Log.e(DetailActivity.class.getSimpleName(), "ID: " + id);
        if (id != -1) {
            downloadExtraInfo(id);
        }

    }

    private void setUpViewPager(){
        mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), mMovieItem));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void downloadExtraInfo(int id){

        Log.e(DetailActivity.class.getSimpleName(), "Download started");

        Call<FullMovie> call = apiService.getMovieDetails(id, API_KEY);

        call.enqueue(new Callback<FullMovie>() {
            @Override
            public void onResponse(Call<FullMovie> call, Response<FullMovie> response) {

                if (response == null || response.body() == null) {
                    return;
                }

                mFullMovie = response.body();

                if (mMovieItem == null) {
                    mMovieItem = new MovieItem();
                    mMovieItem.setPosterPath(mFullMovie.getPosterPath());
                    mMovieItem.setOverview(mFullMovie.getOverview());
                    mMovieItem.setTitle(mFullMovie.getTitle());
                    mMovieItem.setId(mFullMovie.getId());
                    mMovieItem.setReleaseDate(mFullMovie.getReleaseDate());
                    mMovieItem.setVoteAverage(mFullMovie.getVoteAverage());
                    mMovieItem.setBackdropPath(mFullMovie.getBackdropPath());

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(mFullMovie.getTitle());
                    }

                    populateViewsWithData();
                    setUpViewPager();
                }
            }

            @Override
            public void onFailure(Call<FullMovie> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Download failed", Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateViewsWithData() {

        // Set Title
        mMovieTitleTextView.setText(mMovieItem.getTitle());

        // Set Movie Poster
        String picturePath = mMovieItem.getPosterPath();
        // put the picture URL together
        String pictureUrl = "http://image.tmdb.org/t/p/w185/" + picturePath;
        Picasso.with(this)
                .load(pictureUrl)
                .into(mProfileImageView);

        // Set Movie Backdrop
        String backdropPath = mMovieItem.getBackdropPath();
        // put the picture URL together
        String backdropUrl = "http://image.tmdb.org/t/p/w500/" + backdropPath;
        Picasso.with(this)
                .load(backdropUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(mBackDropImageView);

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
