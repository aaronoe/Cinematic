package de.aaronoe.popularmovies.ui.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.BuildConfig;
import de.aaronoe.popularmovies.Data.ApiClient;
import de.aaronoe.popularmovies.Data.ApiInterface;
import de.aaronoe.popularmovies.Data.TvShow.ShowsResponse;
import de.aaronoe.popularmovies.Data.TvShow.TvShow;
import de.aaronoe.popularmovies.Data.TvShow.TvShowAdapter;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.R;
import de.aaronoe.popularmovies.ui.TvShowDetailActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by aaron on 14.04.17.
 */

public class SearchShowsActivity extends AppCompatActivity implements TvShowAdapter.TvShowAdapterOnClickHandler{

    TvShowAdapter tvShowAdapter;
    List<TvShow> tvShows;

    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    ApiInterface apiService;
    StaggeredGridLayoutManager gridLayout;

    private static final String BUNDLE_RECYCLER_LAYOUT = "BUNDLE_RECYCLER_LAYOUT";
    private static final String BUNDLE_SHOWS_LIST_KEY = "BUNDLE_SHOWS_LIST_KEY";

    @BindView(R.id.search_edit_text)
    EditText searchEditText;
    @BindView(R.id.search_rv_main)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_tv_error_message_display)
    TextView searchTvErrorMessageDisplay;
    @BindView(R.id.search_pb_loading_indicator)
    ProgressBar searchPbLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.search_shows);
        }

        gridLayout = new StaggeredGridLayoutManager
                (Utilities.calculateNoOfColumnsShow(this), StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(gridLayout);

        tvShowAdapter = new TvShowAdapter(this, this);
        mRecyclerView.setAdapter(tvShowAdapter);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                timer.cancel();
                timer = new Timer();
                final String text = s.toString();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (text.equals("")) return;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downloadShows(text);
                            }
                        });

                    }
                }, DELAY);
            }

            private Timer timer = new Timer();
            private final long DELAY = 500; // milliseconds

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId== EditorInfo.IME_ACTION_SEARCH )   )
                {
                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(int movieId, String showTitle) {
        Intent intentToStartDetailActivity = new Intent(this, TvShowDetailActivity.class);
        intentToStartDetailActivity.putExtra(getString(R.string.intent_key_tv_show), movieId);
        intentToStartDetailActivity.putExtra(getString(R.string.intent_key_tv_show_update), showTitle);
        startActivity(intentToStartDetailActivity);
    }

    private void downloadShows(String query) {

        searchPbLoadingIndicator.setVisibility(View.VISIBLE);

        Call<ShowsResponse> call = apiService.searchForShows(query, API_KEY);

        call.enqueue(new Callback<ShowsResponse>() {
            @Override
            public void onResponse(Call<ShowsResponse> call, Response<ShowsResponse> response) {
                tvShows = response.body().getTvShows();
                searchPbLoadingIndicator.setVisibility(View.INVISIBLE);
                if (tvShows != null) {
                    showMovieView();
                    tvShowAdapter.setVideoData(tvShows);
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<ShowsResponse> call, Throwable t) {
                searchPbLoadingIndicator.setVisibility(View.INVISIBLE);
                showErrorMessage();
            }
        });


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            tvShows = savedInstanceState.getParcelableArrayList(BUNDLE_SHOWS_LIST_KEY);
            gridLayout.onRestoreInstanceState(savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT));
            tvShowAdapter.setVideoData(tvShows);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(BUNDLE_SHOWS_LIST_KEY, (ArrayList<TvShow>) tvShows);
    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showMovieView() {
        /* First, make sure the error is invisible */
        searchTvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * This method will make the error message visible and hide the weather
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        searchTvErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

}
