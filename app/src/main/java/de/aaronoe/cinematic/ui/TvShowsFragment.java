package de.aaronoe.cinematic.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.cinematic.BuildConfig;
import de.aaronoe.cinematic.database.Utilities;
import de.aaronoe.cinematic.PopularMoviesApplication;
import de.aaronoe.cinematic.R;
import de.aaronoe.cinematic.model.ApiInterface;
import de.aaronoe.cinematic.model.EndlessRecyclerViewScrollListener;
import de.aaronoe.cinematic.model.TvShow.ShowsResponse;
import de.aaronoe.cinematic.model.TvShow.TvShow;
import de.aaronoe.cinematic.model.TvShow.TvShowAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by aaron on 26.03.17.
 */

public class TvShowsFragment extends Fragment implements TvShowAdapter.TvShowAdapterOnClickHandler {

    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    List<TvShow> tvShowList;
    GridLayoutManager linearLayout;
    TvShowAdapter tvShowAdapter;
    private Parcelable mLayoutManagerSavedState;
    EndlessRecyclerViewScrollListener scrollListener;

    @Inject SharedPreferences sharedPref;
    @Inject ApiInterface apiService;


    private static final String BUNDLE_RECYCLER_LAYOUT = "tvshows.recycler.layout";
    private static final String BUNDLE_SCROLL_POSITION = "tvshows.scroll.position";
    private static final String BUNDLE_SHOW_LIST_KEY = "BUNDLE_SHOW_LIST_KEY";
    private static final String TAG = "TvShowsFragment";
    private static final String SELECTION_POPULAR = "popular";
    private static final String SELECTION_TOP_RATED = "top_rated";
    private static final String SELECTION_ON_THE_AIR = "on_the_air";
    private String mCurrentSelection;
    private int scrollListenerPosition = 1;

    @BindView(R.id.rv_main_movie_list)
    RecyclerView rvMainMovieList;
    @BindView(R.id.tv_error_message_display)
    TextView tvErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar pbLoadingIndicator;
    @BindView(R.id.fab_action_show_popular)
    FloatingActionButton fabActionShowPopular;
    @BindView(R.id.fab_action_show_top_rated)
    FloatingActionButton fabActionShowTopRated;
    @BindView(R.id.fab_action_shows_on_the)
    FloatingActionButton fabActionShowsOnThe;
    @BindView(R.id.fab_menu)
    FloatingActionMenu fabMenu;


    public TvShowsFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_shows_list, container, false);

        ButterKnife.bind(this, rootView);

        ((PopularMoviesApplication) getActivity().getApplication()).getNetComponent().inject(this);

        // Get last state

        mCurrentSelection = sharedPref.getString(getString(R.string.pref_key_shows), SELECTION_POPULAR);

        initializeFabMenu();

        linearLayout = new GridLayoutManager
                (getActivity(), Utilities.calculateNoOfColumnsShow(getContext()));
        tvShowAdapter = new TvShowAdapter(getActivity(), this);

        rvMainMovieList.setLayoutManager(linearLayout);
        rvMainMovieList.setAdapter(tvShowAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.e(TAG, "onLoadMore: with page: " + page + "  scrollposition" + scrollListenerPosition);
                donwloadNextPageOfShows(scrollListenerPosition + 1);
                scrollListenerPosition++;
            }
        };

        if (savedInstanceState != null) {
            tvShowList = savedInstanceState.getParcelableArrayList(BUNDLE_SHOW_LIST_KEY);
            restorePosition();
            Log.d(TAG, "onCreateView:  restoring position" );
        }

        rvMainMovieList.addOnScrollListener(scrollListener);

        if (tvShowList == null || tvShowList.size() == 0) {
            downloadShowData(1);
        }

        return rootView;
    }


    private void downloadShowData(int page) {

        pbLoadingIndicator.setVisibility(View.VISIBLE);

        Call<ShowsResponse> call = apiService.getTvShows(mCurrentSelection, API_KEY, page);

        call.enqueue(new Callback<ShowsResponse>() {
            @Override
            public void onResponse(Call<ShowsResponse> call, Response<ShowsResponse> response) {

                if (response == null || response.body() == null || response.body().getTvShows() == null) {
                    return;
                }

                tvShowList = response.body().getTvShows();

                pbLoadingIndicator.setVisibility(View.INVISIBLE);
                if (tvShowList != null) {
                    showMovieView();
                    tvShowAdapter.setVideoData(tvShowList);
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<ShowsResponse> call, Throwable t) {
                pbLoadingIndicator.setVisibility(View.INVISIBLE);
                showErrorMessage();
            }
        });
    }


    private void donwloadNextPageOfShows(int page) {

        Call<ShowsResponse> call = apiService.getTvShows(mCurrentSelection, API_KEY, page);

        Log.e(TAG, "Downloading next page: " + (page));

        call.enqueue(new Callback<ShowsResponse>() {
            @Override
            public void onResponse(Call<ShowsResponse> call, Response<ShowsResponse> response) {

                if (response == null || response.body() == null || response.body().getTvShows() == null) {
                    return;
                }

                List<TvShow> newShows = response.body().getTvShows();

                if (newShows != null) {
                    int size = newShows.size();
                    int previousSize = tvShowList.size();
                    tvShowList.addAll(newShows);
                    tvShowAdapter.notifyItemRangeChanged(previousSize, size);
                    Log.d(TAG, "onResponse: size: " + size + " previoussize: " + previousSize + "- fullsize: " + tvShowList.size());
                }
            }

            @Override
            public void onFailure(Call<ShowsResponse> call, Throwable t) {
                pbLoadingIndicator.setVisibility(View.INVISIBLE);
                showErrorMessage();
            }
        });
    }


    private void showMovieView() {
        /* First, make sure the error is invisible */
        tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        rvMainMovieList.setVisibility(View.VISIBLE);

        rvMainMovieList.smoothScrollToPosition(0);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        rvMainMovieList.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        tvErrorMessageDisplay.setVisibility(View.VISIBLE);

        // check if network conditions exists
        if (!Utilities.isOnline(getActivity())) {
            tvErrorMessageDisplay.setText(getString(R.string.no_network_connection));
        } else {
            tvErrorMessageDisplay.setText(getString(R.string.error_message));
        }
    }

    private void initializeFabMenu() {
        fabActionShowPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPopular();
                fabMenu.close(true);
            }
        });
        fabActionShowsOnThe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOnTheAir();
                fabMenu.close(true);
            }
        });
        fabActionShowTopRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTopRated();
                fabMenu.close(true);
            }
        });
    }

    private void saveSelection(String selection) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_key_shows), selection);
        editor.apply();
    }

    private boolean selectPopular() {
        if (mCurrentSelection.equals(SELECTION_POPULAR)) {
            Toast.makeText(getActivity(), "This option is already selected", Toast.LENGTH_SHORT).show();
            rvMainMovieList.smoothScrollToPosition(1);
            return true;
        }

        tvShowAdapter.setVideoData(null);
        scrollListenerPosition = 1;
        mCurrentSelection = SELECTION_POPULAR;
        downloadShowData(1);
        saveSelection(SELECTION_POPULAR);
        return true;
    }

    private boolean selectTopRated() {
        // return if the option is already selected
        if (mCurrentSelection.equals(SELECTION_TOP_RATED)) {
            Toast.makeText(getActivity(), "This option is already selected", Toast.LENGTH_SHORT).show();
            rvMainMovieList.smoothScrollToPosition(1);
            return true;
        }

        tvShowAdapter.setVideoData(null);
        scrollListenerPosition = 1;
        mCurrentSelection = SELECTION_TOP_RATED;
        downloadShowData(1);
        saveSelection(SELECTION_TOP_RATED);
        return true;
    }

    private boolean selectOnTheAir() {
        // return if the option is already selected
        if (mCurrentSelection.equals(SELECTION_ON_THE_AIR)) {
            Toast.makeText(getActivity(), "This option is already selected", Toast.LENGTH_SHORT).show();
            rvMainMovieList.smoothScrollToPosition(1);
            return true;
        }

        tvShowAdapter.setVideoData(null);
        scrollListenerPosition = 1;
        mCurrentSelection = SELECTION_ON_THE_AIR;
        downloadShowData(1);
        saveSelection(SELECTION_ON_THE_AIR);
        return true;
    }

    @Override
    public void onClick(int movieId, String showTitle) {
        Intent intentToStartDetailActivity = new Intent(getContext(), TvShowDetailActivity.class);
        intentToStartDetailActivity.putExtra(getString(R.string.intent_key_tv_show), movieId);
        intentToStartDetailActivity.putExtra(getString(R.string.intent_key_tv_show_update), showTitle);

        getActivity().startActivity(intentToStartDetailActivity);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            tvShowList = savedInstanceState.getParcelableArrayList(BUNDLE_SHOW_LIST_KEY);
            tvShowAdapter.setVideoData(tvShowList);
            mLayoutManagerSavedState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            scrollListenerPosition = savedInstanceState.getInt(BUNDLE_SCROLL_POSITION);
        }
    }

    private void restorePosition() {
        if (mLayoutManagerSavedState != null) {
            linearLayout.onRestoreInstanceState(mLayoutManagerSavedState);
            mLayoutManagerSavedState = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, rvMainMovieList.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(BUNDLE_SHOW_LIST_KEY, (ArrayList<TvShow>) tvShowList);
        outState.putInt(BUNDLE_SCROLL_POSITION, scrollListenerPosition);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
