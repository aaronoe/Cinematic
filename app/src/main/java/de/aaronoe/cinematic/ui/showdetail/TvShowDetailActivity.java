package de.aaronoe.cinematic.ui.showdetail;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.cinematic.CinematicApp;
import de.aaronoe.cinematic.R;
import de.aaronoe.cinematic.database.MovieUpdateService;
import de.aaronoe.cinematic.database.Utilities;
import de.aaronoe.cinematic.model.Crew.Credits;
import de.aaronoe.cinematic.model.Crew.CrewAdapter;
import de.aaronoe.cinematic.model.TvShow.FullShow.CreatedBy;
import de.aaronoe.cinematic.model.TvShow.FullShow.Genre;
import de.aaronoe.cinematic.model.TvShow.FullShow.Season;
import de.aaronoe.cinematic.model.TvShow.FullShow.TvShowFull;
import de.aaronoe.cinematic.model.TvShow.SeasonAdapter;
import de.aaronoe.cinematic.model.TvShow.TvShow;
import de.aaronoe.cinematic.model.remote.ApiInterface;
import de.aaronoe.cinematic.ui.TvSeasonDetailActivity;
import de.aaronoe.cinematic.util.Constants;
import de.hdodenhof.circleimageview.CircleImageView;

public class TvShowDetailActivity extends AppCompatActivity implements
        SeasonAdapter.SeasonAdapterOnClickHandler,
        ShowDetailContract.View,
        LoaderManager.LoaderCallbacks<Cursor> {

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
    @BindView(R.id.crew_meta_data_container)
    ConstraintLayout crewMetaContainer;
    @BindView(R.id.season_detail_pb)
    ProgressBar seasonDetailPb;

    @BindView(R.id.meta_show_container)
    LinearLayout showMetaContainer;
    @BindView(R.id.show_seasons_pane)
    LinearLayout showSeasonsPane;

    @BindView(R.id.cast_section_container) LinearLayout castSectionContainer;
    @BindView(R.id.cast_recycler_view) RecyclerView castRecyclerView;

    @BindView(R.id.similar_shows_section_container) LinearLayout similarSectionContainer;
    @BindView(R.id.similar_shows_recycler_view) RecyclerView similarRecyclerView;


    int showId;
    int enterMode;
    String showName;
    TvShowFull thisShow;
    TvShow enterShow;
    Context mContext;
    SeasonAdapter seasonAdapter;
    ShowDetailPresenterImpl presenter;

    @Inject ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_show_details);
        ButterKnife.bind(this);
        mContext = this;
        presenter = new ShowDetailPresenterImpl(this);

        ((CinematicApp) getApplication()).getNetComponent().inject(this);

        Intent startIntent = getIntent();
        if (startIntent != null) {
            if (startIntent.hasExtra(getString(R.string.intent_key_tv_show))) {
                showId = startIntent.getIntExtra(getString(R.string.intent_key_tv_show), -1);
                downloadShowDetails();
            }
            if (startIntent.hasExtra(getString(R.string.intent_key_tv_show_update))) {
                showName = startIntent.getStringExtra(getString(R.string.intent_key_tv_show_update));
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(showName);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }
            if (startIntent.hasExtra(getString(R.string.INTENT_KEY_TV_SHOW_ITEM))) {
                enterShow = startIntent.getParcelableExtra(getString(R.string.INTENT_KEY_TV_SHOW_ITEM));
                enterMode = startIntent.getIntExtra(getString(R.string.intent_transition_enter_mode), Constants.NONE);
                showId = enterShow.getId();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(enterShow.getName());
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
                initWithShow();
            }
        }

    }

    private void initWithShow() {
        // Backdrop
        String pictureUrl = "http://image.tmdb.org/t/p/w500/" + enterShow.getBackdropPath();
        String posterUrl = "http://image.tmdb.org/t/p/w185/" + enterShow.getPosterPath();

        supportPostponeEnterTransition();

        switch (enterMode) {
            case Constants.BACKDROP_ENTER :
                Picasso.with(this)
                        .load(pictureUrl)
                        .placeholder(R.drawable.poster_show_loading)
                        .error(R.drawable.poster_show_not_available)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                                tvDetailBackdrop.setImageBitmap(bitmap);
                                supportStartPostponedEnterTransition();
                            }

                            @Override
                            public void onBitmapFailed(Drawable drawable) {
                                supportStartPostponedEnterTransition();
                            }

                            @Override
                            public void onPrepareLoad(Drawable drawable) {

                            }
                        });

                Picasso.with(this)
                        .load(posterUrl)
                        .into(tvDetailProfile);

                break;
            case Constants.POSTER_ENTER:
                Picasso.with(this)
                        .load(posterUrl)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                                tvDetailProfile.setImageBitmap(bitmap);
                                supportStartPostponedEnterTransition();
                            }

                            @Override
                            public void onBitmapFailed(Drawable drawable) {
                                supportStartPostponedEnterTransition();
                            }

                            @Override
                            public void onPrepareLoad(Drawable drawable) {

                            }
                        });
                Picasso.with(this)
                        .load(pictureUrl)
                        .placeholder(R.drawable.poster_show_loading)
                        .error(R.drawable.poster_show_not_available)
                        .into(tvDetailBackdrop);
                break;
        }



        tvDetailTitle.setText(enterShow.getName());
        tvDetailYear.setText(Utilities.convertDateToYear(enterShow.getFirstAirDate()));
        tvDetailOverview.setText(enterShow.getOverview());
        tvDetailAgeRating.setText(enterShow.getVoteAverage().toString());

        downloadShowDetails();

    }


    public void downloadShowDetails() {
        presenter.downloadCast(showId);
        presenter.downloadInfo(showId);
        presenter.downloadSimilar(showId);
    }


    @Override
    public void onClick(Season season, ImageView posterImageView) {
        Log.d(TAG, "onClick() called with: seasonNumber = [" + season + "]");

        Intent intent = new Intent(mContext, TvSeasonDetailActivity.class);
        intent.putExtra(getString(R.string.intent_key_tvshow), thisShow);
        intent.putExtra(getString(R.string.intent_key_selected_season), season);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, posterImageView, getString(R.string.TRANSITION_KEY_TV_SEASON));
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    @Override
    public void showInfo(@NonNull TvShowFull showData) {

        thisShow = showData;

        if (enterShow == null) {

            // Backdrop
            String pictureUrl = "http://image.tmdb.org/t/p/w500/" + showData.getBackdropPath();

            Picasso.with(this)
                    .load(pictureUrl)
                    .placeholder(R.drawable.poster_show_loading)
                    .error(R.drawable.poster_show_not_available)
                    .into(tvDetailBackdrop);

            String posterUrl = "http://image.tmdb.org/t/p/w185/" + showData.getPosterPath();
            Picasso.with(this)
                    .load(posterUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(tvDetailProfile);

            tvDetailTitle.setText(showData.getName());
            tvDetailYear.setText(Utilities.convertDateToYear(showData.getFirstAirDate()));
            tvDetailOverview.setText(showData.getOverview());
            tvDetailAgeRating.setText(showData.getVoteAverage().toString());

        }

        seasonDetailPb.setVisibility(View.INVISIBLE);
        crewMetaContainer.setVisibility(View.VISIBLE);
        showMetaContainer.setVisibility(View.VISIBLE);
        showSeasonsPane.setVisibility(View.VISIBLE);
        Log.e(TAG, "populateViewsWithData() called");

        // Creator
        List<CreatedBy> createdByList = showData.getCreatedBy();
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

        showRuntimeStatus.setText(showData.getStatus());
        showRuntimeFirstAirDate.setText(Utilities.convertDate(showData.getFirstAirDate()));
        showRuntimeNrEpisodes.setText(String.valueOf(showData.getNumberOfEpisodes()));
        showRuntimeNrSeasons.setText(String.valueOf(showData.getNumberOfSeasons()));

        int diff = (int) Utilities.computeDifferenceInDays(showData.getLastAirDate());
        String lastRuntime = Utilities.convertDate(showData.getLastAirDate());

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

        List<Integer> runtime = showData.getEpisodeRunTime();
        String runtimeString = "";
        for (int i = 0; i < runtime.size(); i++) {
            if (i != 0) {
                runtimeString += ", ";
            }
            runtimeString += runtime.get(i);
        }
        runtimeString += " " + getString(R.string.runtime_minutes);

        showRuntimeMinutes.setText(runtimeString);

        List<Genre> genreList = showData.getGenres();
        String genreString = "";
        for (int i = 0; i < genreList.size(); i++) {
            if (i != 0) {
                genreString += ", ";
            }
            genreString += genreList.get(i).getName();
        }
        showRuntimeGenres.setText(genreString);


        List<Season> seasonList = showData.getSeasons();

        // new season pane
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        seasonRecylcerView.setLayoutManager(linearLayoutManager);
        seasonRecylcerView.setNestedScrollingEnabled(false);
        seasonAdapter = new SeasonAdapter(this, this);
        seasonRecylcerView.setAdapter(seasonAdapter);
        seasonAdapter.setSeasonList(seasonList);

        Log.d(TAG, "populateViewsWithData: " + Utilities.buildShowUri(showData.getId()));

        // Todo: Here as well
        getSupportLoaderManager().initLoader(CHECK_FAVE_LOADER_ID, null, this);

    }

    @Override
    public void showCast(@NonNull Credits castData) {
        castSectionContainer.setVisibility(View.VISIBLE);
        CrewAdapter creditsAdapter = new CrewAdapter(this);
        creditsAdapter.setCastData(castData.getCast());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        castRecyclerView.setLayoutManager(layoutManager);
        castRecyclerView.setAdapter(creditsAdapter);
    }

    @Override
    public void showSimilar(@NonNull List<TvShow> similarList) {
        similarSectionContainer.setVisibility(View.VISIBLE);
        SimilarShowsAdapter similarShowsAdapter = new SimilarShowsAdapter(this);
        similarShowsAdapter.setShowData(similarList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        similarRecyclerView.setLayoutManager(layoutManager);
        similarRecyclerView.setAdapter(similarShowsAdapter);
    }

    @Override
    public void showErrorInfo() {

    }

    @Override
    public void showErrorCast() {
        castSectionContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showErrorSimilar() {

    }




    // Todo: Get rid of this at some point

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

                MovieUpdateService.deleteItem(mContext, Utilities.buildShowUri(thisShow.getId()));
            }

}
