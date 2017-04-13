package de.aaronoe.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.BuildConfig;
import de.aaronoe.popularmovies.Data.ApiClient;
import de.aaronoe.popularmovies.Data.ApiInterface;
import de.aaronoe.popularmovies.Data.EndlessRecyclerViewScrollListener;
import de.aaronoe.popularmovies.Data.MovieAdapter;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.DetailPage.DetailActivity;
import de.aaronoe.popularmovies.Movies.MovieItem;
import de.aaronoe.popularmovies.Movies.MovieResponse;
import de.aaronoe.popularmovies.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoviesFragment extends Fragment
        implements MovieAdapter.MovieAdapterOnClickHandler {

    // for debugging purposes

    private static final String TAG = "MoviesFragment";
    public MovieAdapter mMovieAdapter;
    final String SELECTION_POPULAR = "popular";
    final String SELECTION_TOP_RATED = "top_rated";
    final String SELECTION_UPCOMING = "upcoming";
    String mCurrentSelection;
    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    ApiInterface apiService;
    List<MovieItem> movieItemList;
    StaggeredGridLayoutManager gridLayout;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private static final String BUNDLE_MOVIE_LIST_KEY = "BUNDLE_MOVIE_LIST_KEY";
    private static final String BUNDLE_SCROLL_POSITION = "BUNDLE_SCROLL_POSITION_MOVIES";
    private Parcelable mLayoutManagerSavedState;
    private EndlessRecyclerViewScrollListener scrollListener;
    private int scrollPosition = 1;


    @BindView(R.id.fab_menu) FloatingActionMenu fabMenu;
    @BindView(R.id.fab_action_top_rated) FloatingActionButton fabButtonTopRated;
    @BindView(R.id.fab_action_popular) FloatingActionButton fabButtonPopular;
    @BindView(R.id.fab_action_upcoming) FloatingActionButton fabButtonUpcoming;
    @BindView(R.id.rv_main_movie_list) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    public MoviesFragment() {}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main, container, false);

        ButterKnife.bind(this, rootView);

        // Get last state
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        mCurrentSelection = sharedPref.getString(getString(R.string.SAVE_SELECTION_KEY), SELECTION_POPULAR);

        gridLayout = new StaggeredGridLayoutManager
                (Utilities.calculateNoOfColumnsShow(getActivity()), StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(gridLayout);
        //mRecyclerView.hasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this, getActivity());
        mRecyclerView.setAdapter(mMovieAdapter);


        scrollListener = new EndlessRecyclerViewScrollListener(gridLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                downloadNextPageOfMovies(scrollPosition + 1);
                Log.d(TAG, "onLoadMore: page: "+ page + " scrollpos: "+ scrollPosition);
                scrollPosition++;
            }
        };

        mRecyclerView.addOnScrollListener(scrollListener);


        initializeFabMenu();
        if (savedInstanceState != null) {
            movieItemList = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIE_LIST_KEY);
            restorePosition();
        }

        apiService = ApiClient.getClient().create(ApiInterface.class);

        if (movieItemList == null || movieItemList.size() == 0) {
            downloadMovieData();
            mRecyclerView.addOnScrollListener(scrollListener);
        }

        return rootView;
    }


    private void restorePosition() {
        if (mLayoutManagerSavedState != null) {
            gridLayout.onRestoreInstanceState(mLayoutManagerSavedState);
            mLayoutManagerSavedState = null;
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            movieItemList = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIE_LIST_KEY);
            mMovieAdapter.setMovieData(movieItemList);
            Log.e(TAG, "Movie data size after restore: " + movieItemList.size());
            mLayoutManagerSavedState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            scrollPosition = savedInstanceState.getInt(BUNDLE_SCROLL_POSITION);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(BUNDLE_MOVIE_LIST_KEY, (ArrayList<MovieItem>) movieItemList);
        outState.putInt(BUNDLE_SCROLL_POSITION, scrollPosition);
    }


    private void downloadNextPageOfMovies(int page) {

        Call<MovieResponse> call = apiService.getPageOfMovies(mCurrentSelection, API_KEY, page);

        Log.e(TAG, "Downloading next page: " + (page));

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<MovieItem> newMovies = response.body().getResults();

                if (newMovies != null) {
                    int size = newMovies.size();
                    int previousSize = movieItemList.size();
                    movieItemList.addAll(newMovies);
                    mMovieAdapter.notifyItemRangeChanged(previousSize, size);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });

    }



    public void initializeFabMenu() {

        fabButtonPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPopular();
                fabMenu.close(true);
            }
        });

        fabButtonTopRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTopRated();
                fabMenu.close(true);
            }
        });

        fabButtonUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUpcoming();
                fabMenu.close(true);
            }
        });

    }


    private void downloadMovieData() {

        if (movieItemList != null) {
            movieItemList.clear();
            mMovieAdapter.notifyDataSetChanged();
            scrollPosition = 1;
            scrollListener.resetState();
        }

        mLoadingIndicator.setVisibility(View.VISIBLE);

        Call<MovieResponse> call = apiService.getMovies(mCurrentSelection, API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movieItemList = response.body().getResults();

                mLoadingIndicator.setVisibility(View.INVISIBLE);
                if (movieItemList != null) {
                    showMovieView();
                    mMovieAdapter.setMovieData(movieItemList);
                } else {
                    showErrorMessage();
                }
                //restorePosition();
                Log.e(TAG, "after download: " + movieItemList.size());

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                showErrorMessage();
            }
        });

    }

    @Override
    public void onClick(MovieItem movieItem) {
        Intent intentToStartDetailActivity = new Intent(getActivity(), DetailActivity.class);
        intentToStartDetailActivity.putExtra("MovieId", movieItem.getId());
        startActivity(intentToStartDetailActivity);
    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     *
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showMovieView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);

        scrollListener.resetState();
        mRecyclerView.addOnScrollListener(scrollListener);
    }


    private void saveSelection(String selection) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.SAVE_SELECTION_KEY), selection);
        editor.apply();
    }


    /**
     * This method will make the error message visible and hide the weather
     * View.
     *
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);

        // check if network conditions exists
        if (!Utilities.isOnline(getActivity())) {
            mErrorMessageDisplay.setText(getString(R.string.no_network_connection));
        } else {
            mErrorMessageDisplay.setText(getString(R.string.error_message));
        }
    }


    private boolean selectPopular() {
        if (mCurrentSelection.equals(SELECTION_POPULAR)) {
            Toast.makeText(getActivity(), "This option is already selected", Toast.LENGTH_SHORT).show();
            mRecyclerView.smoothScrollToPosition(1);
            return true;
        }

        mMovieAdapter.setMovieData(null);
        mCurrentSelection = SELECTION_POPULAR;
        downloadMovieData();
        saveSelection(SELECTION_POPULAR);
        return true;
    }


    private boolean selectTopRated() {
        // return if the option is already selected
        if (mCurrentSelection.equals(SELECTION_TOP_RATED)) {
            Toast.makeText(getActivity(), "This option is already selected", Toast.LENGTH_SHORT).show();
            mRecyclerView.smoothScrollToPosition(1);
            return true;
        }

        mMovieAdapter.setMovieData(null);
        mCurrentSelection = SELECTION_TOP_RATED;
        downloadMovieData();
        saveSelection(SELECTION_TOP_RATED);
        return true;
    }


    private boolean selectUpcoming() {
        if (mCurrentSelection.equals(SELECTION_UPCOMING)) {
            Toast.makeText(getActivity(), "This option is already selected", Toast.LENGTH_SHORT).show();
            mRecyclerView.smoothScrollToPosition(1);
            return true;
        }
        mMovieAdapter.setMovieData(null);
        mCurrentSelection = SELECTION_UPCOMING;
        downloadMovieData();
        saveSelection(SELECTION_UPCOMING);
        return true;
    }

}
