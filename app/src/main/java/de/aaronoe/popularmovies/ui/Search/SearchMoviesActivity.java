package de.aaronoe.popularmovies.ui.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import de.aaronoe.popularmovies.Data.MultiSearch.MultiSearchResponse;
import de.aaronoe.popularmovies.Data.MultiSearch.SearchItem;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.DetailPage.ActorDetails.ActorDetailsActivity;
import de.aaronoe.popularmovies.DetailPage.DetailActivity;
import de.aaronoe.popularmovies.R;
import de.aaronoe.popularmovies.ui.TvShowDetailActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMoviesActivity extends AppCompatActivity
        implements MultiSearchAdapter.MultiSearchItemOnClickHandler {

    private static final String TAG = "SearchMoviesActivity";

    List<SearchItem> searchItemList;
    public MultiSearchAdapter mMultiSearchAdapter;
    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    ApiInterface apiService;
    StaggeredGridLayoutManager gridLayout;

    private static final String BUNDLE_RECYCLER_LAYOUT = "BUNDLE_RECYCLER_LAYOUT";
    private static final String BUNDLE_MOVIE_LIST_KEY = "BUNDLE_MOVIE_LIST_KEY";
    private Timer timer = new Timer();
    private final long DELAY = 300; // milliseconds

    @BindView(R.id.search_rv_main)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_pb_loading_indicator)
    ProgressBar searchProgressBar;
    @BindView(R.id.search_tv_error_message_display)
    TextView searchErrorTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.search_movies);
        }

        gridLayout = new StaggeredGridLayoutManager
                (Utilities.calculateNoOfColumnsShow(this), StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(gridLayout);
        //mRecyclerView.hasFixedSize(true);
        mMultiSearchAdapter = new MultiSearchAdapter(this, this);
        mRecyclerView.setAdapter(mMultiSearchAdapter);

        apiService = ApiClient.getClient().create(ApiInterface.class);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            searchItemList = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIE_LIST_KEY);
            gridLayout.onRestoreInstanceState(savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT));
            mMultiSearchAdapter.setData(searchItemList);
        }
    }

    private void downloadMovieData(String query) {

        Log.d(TAG, "downloadMovieData() called with: query = [" + query + "]");
        searchProgressBar.setVisibility(View.VISIBLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.results_for_query, query));
        }

        Call<MultiSearchResponse> call = apiService.multiSearch(query, API_KEY);


        call.enqueue(new Callback<MultiSearchResponse>() {
            @Override
            public void onResponse(Call<MultiSearchResponse> call, Response<MultiSearchResponse> response) {
                searchItemList = response.body().getResults();
                Log.d(TAG, "onResponse: Items: " + response.body().getTotalResults());

                searchProgressBar.setVisibility(View.INVISIBLE);
                if (searchItemList != null) {
                    showMovieView();
                    Log.d(TAG, "onResponse: List size " + searchItemList.size());
                    mMultiSearchAdapter.setData(searchItemList);
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<MultiSearchResponse> call, Throwable t) {
                searchProgressBar.setVisibility(View.INVISIBLE);
                showErrorMessage();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(BUNDLE_MOVIE_LIST_KEY, (ArrayList<SearchItem>) searchItemList);
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
        searchErrorTv.setVisibility(View.INVISIBLE);
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
        searchErrorTv.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(int itemId, String itemType) {
        switch (itemType) {
            case MultiSearchAdapter.MEDIA_TYPE_MOVIE:
                Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
                intentToStartDetailActivity.putExtra("MovieId", itemId);
                startActivity(intentToStartDetailActivity);
                break;
            case MultiSearchAdapter.MEDIA_TYPE_TV:
                Intent intentToStartShowActivity = new Intent(this, TvShowDetailActivity.class);
                intentToStartShowActivity.putExtra(getString(R.string.intent_key_tv_show), itemId);
                startActivity(intentToStartShowActivity);
                break;
            case MultiSearchAdapter.MEDIA_TYPE_PERSON:
                Intent intentPersonActivity = new Intent(this, ActorDetailsActivity.class);
                intentPersonActivity.putExtra(getString(R.string.intent_key_cast_id), itemId);
                startActivity(intentPersonActivity);
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.searchbar, menu);

        final MenuItem searchItem = menu.findItem(R.id.actionbar_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchItem.collapseActionView();
                downloadMovieData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                timer.cancel();
                timer = new Timer();
                final String text = newText;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // download movie stuff
                        Log.d(TAG, "run: "+ text);
                        if (text.equals("")) return;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                downloadMovieData(text);
                            }
                        });

                    }
                }, DELAY);
                return false;
            }
        });
        return true;
    }
}