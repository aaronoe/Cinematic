package de.aaronoe.cinematic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.cinematic.BuildConfig;
import de.aaronoe.cinematic.database.Utilities;
import de.aaronoe.cinematic.CinematicApp;
import de.aaronoe.cinematic.R;
import de.aaronoe.cinematic.model.remote.ApiInterface;
import de.aaronoe.cinematic.model.TvShow.EpisodeAdapter;
import de.aaronoe.cinematic.model.TvShow.FullSeason.FullSeason;
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
    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    EpisodeAdapter episodeAdapter;

    @Inject ApiInterface apiInterface;

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
    @BindView(R.id.season_overview_tv)
    TextView seasonOverviewTv;
    @BindView(R.id.single_season_recycler_view)
    DiscreteScrollView singleSeasonRv;
    @BindView(R.id.episode_overview_textview)
    TextView overViewTV;
    @BindView(R.id.episode_overview_container)
    LinearLayout episodeOverviewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_season_detail);
        ButterKnife.bind(this);

        ((CinematicApp) getApplication()).getNetComponent().inject(this);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(getString(R.string.intent_key_tvshow))) {
                showName = intentThatStartedThisActivity.getStringExtra(getString(R.string.intent_key_tvshow));
                seasonDetailTitle.setText(showName);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(showName);
                }
            }
            if (intentThatStartedThisActivity.hasExtra(getString(R.string.intent_key_selected_season))) {
                selectedSeason = intentThatStartedThisActivity.getIntExtra(getString(R.string.intent_key_selected_season), -1);
                if (selectedSeason == 0) {
                    tvDetailTitle.setText(getString(R.string.extras_and_year));
                } else {
                    tvDetailTitle.setText(getString(R.string.season_x, selectedSeason));
                }
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


        downloadSeasonDetails();
    }


    private void downloadSeasonDetails() {

        Call<FullSeason> call = apiInterface.getTvSeasonDetails(showId, selectedSeason, API_KEY);


        call.enqueue(new Callback<FullSeason>() {
            @Override
            public void onResponse(Call<FullSeason> call, Response<FullSeason> response) {

                if (response == null || response.body() == null) {
                    return;
                }

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

        if (mSeason.getOverview() == null || mSeason.getOverview().equals("")) {
            episodeOverviewContainer.setVisibility(View.GONE);
        }

        seasonOverviewTv.setText(mSeason.getOverview());


        episodeAdapter = new EpisodeAdapter(this);
        singleSeasonRv.setAdapter(episodeAdapter);

        singleSeasonRv.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.75f)
                .build());

        singleSeasonRv.setOrientation(Orientation.HORIZONTAL);


        episodeAdapter.setEpisodeList(mSeason.getEpisodes());
        Log.e(TAG, "populateViewsWithData: " + mSeason.getEpisodes().size() );
    }

}
