package de.aaronoe.cinematic.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.cinematic.BuildConfig;
import de.aaronoe.cinematic.Database.MovieUpdateService;
import de.aaronoe.cinematic.Database.Utilities;
import de.aaronoe.cinematic.PopularMoviesApplication;
import de.aaronoe.cinematic.R;
import de.aaronoe.cinematic.model.ApiInterface;
import de.aaronoe.cinematic.model.TvShow.FullShow.CreatedBy;
import de.aaronoe.cinematic.model.TvShow.FullShow.Genre;
import de.aaronoe.cinematic.model.TvShow.FullShow.Season;
import de.aaronoe.cinematic.model.TvShow.FullShow.TvShowFull;
import de.aaronoe.cinematic.model.TvShow.SeasonAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowDetailActivity extends AppCompatActivity implements
        SeasonAdapter.SeasonAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "TvShowDetailActivity";
    private static final int CHECK_FAVE_LOADER_ID = 609;

    @BindView(R.id.tv_detail_backdrop)
    ImageView tvDetailBackdrop;
    @BindView(R.id.tv_detail_profile)
    ImageView tvDetailProfile;
    @BindView(R.id.tv_detail_title)
    TextView tvDetailTitle;
    @BindView(R.id.tv_detail_year)
    TextView tvDetailYear;
    @BindView(R.id.tv_detail_dividerdot)
    TextView tvDetailDividerdot;
    @BindView(R.id.tv_detail_age_rating)
    TextView tvDetailAgeRating;
    @BindView(R.id.show_runtime_status)
    TextView showRuntimeStatus;
    @BindView(R.id.show_runtime_last_air_date)
    TextView showRuntimeLastAirDate;
    @BindView(R.id.show_runtime_first_air_date)
    TextView showRuntimeFirstAirDate;
    @BindView(R.id.show_runtime_minutes)
    TextView showRuntimeMinutes;
    @BindView(R.id.show_runtime_genres)
    TextView showRuntimeGenres;
    @BindView(R.id.show_runtime_nr_seasons)
    TextView showRuntimeNrSeasons;
    @BindView(R.id.show_runtime_nr_episodes)
    TextView showRuntimeNrEpisodes;
    @BindView(R.id.tv_detail_overview)
    TextView tvDetailOverview;
    @BindView(R.id.detail_show_crew_image)
    CircleImageView detailShowCrewImage;
    @BindView(R.id.detail_show_crew_name)
    TextView detailShowCrewName;
    @BindView(R.id.detail_show_crew_title)
    TextView detailShowCrewTitle;
    @BindView(R.id.seasons_recycler_view)
    RecyclerView seasonRecylcerView;
    @BindView(R.id.toggleFavoriteShowButton)
    ToggleButton toggleFavoriteShowButton;
    @BindView(R.id.show_detail_container)
    ScrollView showDetailContainer;

    int movieId;
    String showName;
    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    TvShowFull thisShow;
    Context mContext;
    SeasonAdapter seasonAdapter;

    @Inject ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_show_details);
        ButterKnife.bind(this);
        mContext = this;

        ((PopularMoviesApplication) getApplication()).getNetComponent().inject(this);

        Intent startIntent = getIntent();
        if (startIntent != null) {
            if (startIntent.hasExtra(getString(R.string.intent_key_tv_show))) {
                movieId = startIntent.getIntExtra(getString(R.string.intent_key_tv_show), -1);
            }
            if (startIntent.hasExtra(getString(R.string.intent_key_tv_show_update))) {
                showName = startIntent.getStringExtra(getString(R.string.intent_key_tv_show_update));
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(showName);
                }
            }
        }
        downloadShowDetails();

    }


    public void downloadShowDetails() {

        Call<TvShowFull> call = apiInterface.getTvShowDetails(movieId, API_KEY);

        call.enqueue(new Callback<TvShowFull>() {
            @Override
            public void onResponse(Call<TvShowFull> call, Response<TvShowFull> response) {
                thisShow = response.body();
                if (thisShow != null) {
                    Log.d(TAG, "onResponse: " + thisShow.getOverview());
                    populateViewsWithData();
                }
            }

            @Override
            public void onFailure(Call<TvShowFull> call, Throwable t) {
                Toast.makeText(TvShowDetailActivity.this, "Downloading Data has failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void populateViewsWithData() {

        // Backdrop
        String pictureUrl = "http://image.tmdb.org/t/p/w500/" + thisShow.getBackdropPath();

        Picasso.with(this)
                .load(pictureUrl)
                .placeholder(R.drawable.poster_show_loading)
                .error(R.drawable.poster_show_not_available)
                .into(tvDetailBackdrop);

        String posterUrl = "http://image.tmdb.org/t/p/w185/" + thisShow.getPosterPath();
        Picasso.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(tvDetailProfile);

        tvDetailTitle.setText(thisShow.getName());
        tvDetailYear.setText(Utilities.convertDateToYear(thisShow.getFirstAirDate()));
        int numberOfSeasons = thisShow.getNumberOfSeasons();
        tvDetailAgeRating.setText(getResources().
                getQuantityString(R.plurals.no_of_seasons, numberOfSeasons, numberOfSeasons));


        // Creator
        List<CreatedBy> createdByList = thisShow.getCreatedBy();
        if (createdByList == null || createdByList.size() == 0) {
            // remove views
            detailShowCrewImage.setVisibility(View.INVISIBLE);
            detailShowCrewName.setVisibility(View.INVISIBLE);
            detailShowCrewTitle.setVisibility(View.INVISIBLE);
        } else {
            CreatedBy firstEntry = createdByList.get(0);
            detailShowCrewName.setText(firstEntry.getName());

            String creatorProfileUrl = "http://image.tmdb.org/t/p/w185/" + firstEntry.getProfilePath();
            Picasso.with(this)
                    .load(creatorProfileUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(detailShowCrewImage);
        }

        tvDetailOverview.setText(thisShow.getOverview());
        showRuntimeStatus.setText(thisShow.getStatus());
        showRuntimeFirstAirDate.setText(Utilities.convertDate(thisShow.getFirstAirDate()));
        showRuntimeNrEpisodes.setText(String.valueOf(thisShow.getNumberOfEpisodes()));
        showRuntimeNrSeasons.setText(String.valueOf(thisShow.getNumberOfSeasons()));

        int diff = (int) Utilities.computeDifferenceInDays(thisShow.getLastAirDate());
        String lastRuntime = Utilities.convertDate(thisShow.getLastAirDate());


        if (diff <= 0) {
            String daysDifference;
            if (diff < 0) {
                daysDifference = getResources()
                        .getQuantityString(R.plurals.x_days_ago_plurals, Math.abs(diff), Math.abs(diff));
            } else {
                daysDifference = getString(R.string.today_show);
            }
            lastRuntime += " " + daysDifference;
        }

        showRuntimeLastAirDate.setText(lastRuntime);
        Log.d(TAG, "populateViewsWithData: " + diff);

        List<Integer> runtime = thisShow.getEpisodeRunTime();
        String runtimeString = "";
        for (int i = 0; i < runtime.size(); i++) {
            if (i != 0) {
                runtimeString += ", ";
            }
            runtimeString += runtime.get(i);
        }
        runtimeString += " " + getString(R.string.runtime_minutes);

        showRuntimeMinutes.setText(runtimeString);

        List<Genre> genreList = thisShow.getGenres();
        String genreString = "";
        for (int i = 0; i < genreList.size(); i++) {
            if (i != 0) {
                genreString += ", ";
            }
            genreString += genreList.get(i).getName();
        }
        showRuntimeGenres.setText(genreString);


        List<Season> seasonList = thisShow.getSeasons();

        // new season pane
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        seasonRecylcerView.setLayoutManager(linearLayoutManager);
        seasonRecylcerView.setNestedScrollingEnabled(false);
        seasonAdapter = new SeasonAdapter(this, this);
        seasonRecylcerView.setAdapter(seasonAdapter);
        seasonAdapter.setSeasonList(seasonList);

        Log.d(TAG, "populateViewsWithData: " + Utilities.buildShowUri(thisShow.getId()));
        getSupportLoaderManager().initLoader(CHECK_FAVE_LOADER_ID, null, this);

    }


    @Override
    public void onClick(int seasonNumber) {
        Log.d(TAG, "onClick() called with: seasonNumber = [" + seasonNumber + "]");
        Intent intent = new Intent(mContext, TvSeasonDetailActivity.class);
        intent.putExtra(getString(R.string.intent_key_tvshow), thisShow.getName());
        intent.putExtra(getString(R.string.intent_key_season_id), thisShow.getId());
        intent.putExtra(getString(R.string.intent_key_selected_season), seasonNumber);
        intent.putExtra(getString(R.string.intent_key_backdrop), thisShow.getBackdropPath());
        mContext.startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Utilities.buildShowUri(thisShow.getId()), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null) {
            Log.d(TAG, "onLoadFinished: " + data.getCount());
            if (data.getCount() == 0) {
                toggleFavoriteShowButton.setTextOn(getString(R.string.button_off));
                toggleFavoriteShowButton.setChecked(false);
            } else {
                toggleFavoriteShowButton.setTextOff(getString(R.string.button_on));
                toggleFavoriteShowButton.setChecked(true);
            }
        } else {
            Log.d(TAG, "onLoadFinished: data is null");
        }

        toggleFavoriteShowButton.setOnCheckedChangeListener(favoriteShowsChangeListener);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private CompoundButton.OnCheckedChangeListener favoriteShowsChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    Log.d(TAG, "onCheckedChanged() called with: compoundButton = [" + compoundButton + "], isChecked = [" + isChecked + "]");
                    if (isChecked) {
                        // Toggle is enabled
                        addToFavorites();
                        Snackbar snackbar = Snackbar
                                .make(showDetailContainer, getString(R.string.added_to_favorites), Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toggleFavoriteShowButton.setChecked(false);
                                        Snackbar undo = Snackbar.make(showDetailContainer, getString(R.string.removed_from_favorites), Snackbar.LENGTH_SHORT);
                                        undo.show();
                                    }
                                });
                        snackbar.show();

                    } else {
                        removeFromFavorites();
                        Snackbar snackbar = Snackbar
                                .make(showDetailContainer, getString(R.string.removed_from_favorites), Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toggleFavoriteShowButton.setChecked(true);
                                        Snackbar undo = Snackbar.make(showDetailContainer, getString(R.string.added_to_favorites), Snackbar.LENGTH_SHORT);
                                        undo.show();
                                    }
                                });
                        snackbar.show();
                    }
                }
            };

            private void addToFavorites() {
                toggleFavoriteShowButton.setText(getString(R.string.button_on));
                toggleFavoriteShowButton.setTextOn(getString(R.string.button_on));
                toggleFavoriteShowButton.setChecked(true);
                //Toast.makeText(mContext, getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show();

                MovieUpdateService.insertNewShow
                        (mContext, Utilities.getContentValuesForShow(thisShow, mContext));
            }

            private void removeFromFavorites() {
                toggleFavoriteShowButton.setText(getString(R.string.button_off));
                toggleFavoriteShowButton.setTextOff(getString(R.string.button_off));
                toggleFavoriteShowButton.setChecked(false);
                //Toast.makeText(mContext, R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();

                MovieUpdateService.deleteTask(mContext, Utilities.buildShowUri(thisShow.getId()));
            }

}
