package de.aaronoe.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.BuildConfig;
import de.aaronoe.popularmovies.Data.ApiClient;
import de.aaronoe.popularmovies.Data.ApiInterface;
import de.aaronoe.popularmovies.Data.TvShow.FullSeason.FullSeason;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvSeasonDetailActivity extends AppCompatActivity {

    private static final String TAG = "TvSeasonDetailActivity";

    String showName;
    String showBackdropPath;
    FullSeason mSeason;
    int selectedSeason;
    int showId;
    ApiInterface apiInterface;
    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;


    @BindView(R.id.tv_detail_backdrop)
    ImageView tvDetailBackdrop;
    @BindView(R.id.tv_detail_profile)
    ImageView tvDetailProfile;
    @BindView(R.id.season_detail_title)
    TextView seasonDetailTitle;
    @BindView(R.id.tv_detail_title)
    TextView tvDetailTitle;
    @BindView(R.id.tv_detail_year)
    TextView tvDetailYear;
    @BindView(R.id.tv_detail_dividerdot)
    TextView tvDetailDividerdot;
    @BindView(R.id.tv_detail_age_rating)
    TextView tvDetailAgeRating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_season_detail);
        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(getString(R.string.intent_key_tvshow))) {
                showName = intentThatStartedThisActivity.getStringExtra(getString(R.string.intent_key_tvshow));
                seasonDetailTitle.setText(showName);
            }
            if (intentThatStartedThisActivity.hasExtra(getString(R.string.intent_key_selected_season))) {
                selectedSeason = intentThatStartedThisActivity.getIntExtra(getString(R.string.intent_key_selected_season), -1);
                tvDetailTitle.setText(getString(R.string.season_x, selectedSeason));
            }
            if (intentThatStartedThisActivity.hasExtra(getString(R.string.intent_key_backdrop))) {
                showBackdropPath = intentThatStartedThisActivity.getStringExtra(getString(R.string.intent_key_backdrop));
            }
            if (intentThatStartedThisActivity.hasExtra(getString(R.string.intent_key_season_id))) {
                showId = intentThatStartedThisActivity.getIntExtra(getString(R.string.intent_key_season_id), -1);
            }
        }

        // Backdrop
        String pictureUrl = "http://image.tmdb.org/t/p/w500/" + showBackdropPath;
        Picasso.with(this)
                .load(pictureUrl)
                .placeholder(R.drawable.poster_show_loading)
                .error(R.drawable.poster_show_not_available)
                .into(tvDetailBackdrop);

        Log.d(TAG, "onCreate: " + selectedSeason);

        Log.d(TAG, "onCreate: "+ showId + " " + selectedSeason);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        downloadSeasonDetails();
    }


    private void downloadSeasonDetails() {

        Call<FullSeason> call = apiInterface.getTvSeasonDetails(showId, selectedSeason, API_KEY);

        call.enqueue(new Callback<FullSeason>() {
            @Override
            public void onResponse(Call<FullSeason> call, Response<FullSeason> response) {
                mSeason = response.body();
                populateViewsWithData();
            }

            @Override
            public void onFailure(Call<FullSeason> call, Throwable t) {
                Toast.makeText(TvSeasonDetailActivity.this, "Failed downloading data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void populateViewsWithData() {

        String creatorProfileUrl = "http://image.tmdb.org/t/p/w185/" + mSeason.getPosterPath();
        Picasso.with(this)
                .load(creatorProfileUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(tvDetailProfile);

        tvDetailYear.setText(Utilities.convertDateToYear(mSeason.getAirDate()));

        tvDetailAgeRating.setText(getString(R.string.nr_episodes, mSeason.getEpisodes().size()));

    }

}
