package de.aaronoe.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.BuildConfig;
import de.aaronoe.popularmovies.Data.ApiClient;
import de.aaronoe.popularmovies.Data.ApiInterface;
import de.aaronoe.popularmovies.Data.EndlessRecyclerViewScrollListener;
import de.aaronoe.popularmovies.Data.TvShow.ShowsResponse;
import de.aaronoe.popularmovies.Data.TvShow.TvShow;
import de.aaronoe.popularmovies.Data.TvShow.TvShowAdapter;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by aaron on 26.03.17.
 */

public class TvShowsFragment extends Fragment implements TvShowAdapter.TvShowAdapterOnClickHandler {

    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    ApiInterface apiService;
    List<TvShow> tvShowList;
    LinearLayoutManager linearLayout;
    TvShowAdapter tvShowAdapter;
    EndlessRecyclerViewScrollListener scrollListener;


    private static final String TAG = "TvShowsFragment";
    private static final String SELECTION_POPULAR = "popular";

    @BindView(R.id.rv_main_movie_list)
    RecyclerView rvMainMovieList;
    @BindView(R.id.tv_error_message_display)
    TextView tvErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar pbLoadingIndicator;
    @BindView(R.id.fab_action_popular)
    FloatingActionButton fabActionPopular;
    @BindView(R.id.fab_action_top_rated)
    FloatingActionButton fabActionTopRated;
    @BindView(R.id.fab_action_upcoming)
    FloatingActionButton fabActionUpcoming;
    @BindView(R.id.fab_action_favorite)
    FloatingActionButton fabActionFavorite;
    @BindView(R.id.fab_action_search)
    FloatingActionButton fabActionSearch;
    @BindView(R.id.fab_menu)
    FloatingActionMenu fabMenu;
    @BindView(R.id.activity_main)
    FrameLayout activityMain;

    public TvShowsFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main, container, false);

        ButterKnife.bind(this, rootView);

        linearLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        tvShowAdapter = new TvShowAdapter(getActivity(), this);

        rvMainMovieList.setLayoutManager(linearLayout);
        rvMainMovieList.setAdapter(tvShowAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG, "onLoadMore: with page: " + page);
                donwloadNextPageOfShows(page + 1);
            }
        };
        rvMainMovieList.addOnScrollListener(scrollListener);


        apiService = ApiClient.getClient().create(ApiInterface.class);
        downloadShowData(1);

        return rootView;
    }


    private void downloadShowData(int page) {

        pbLoadingIndicator.setVisibility(View.VISIBLE);

        Call<ShowsResponse> call = apiService.getTvShows(SELECTION_POPULAR, API_KEY, page);

        call.enqueue(new Callback<ShowsResponse>() {
            @Override
            public void onResponse(Call<ShowsResponse> call, Response<ShowsResponse> response) {
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

        Call<ShowsResponse> call = apiService.getTvShows(SELECTION_POPULAR, API_KEY, page);

        Log.v(TAG, "Downloading next page: " + (page));

        call.enqueue(new Callback<ShowsResponse>() {
            @Override
            public void onResponse(Call<ShowsResponse> call, Response<ShowsResponse> response) {
                List<TvShow> newShows = response.body().getTvShows();

                if (newShows != null) {
                    int size = newShows.size();
                    int previousSize = tvShowList.size();
                    tvShowList.addAll(newShows);
                    tvShowAdapter.notifyItemRangeChanged(previousSize, size);
                    Log.d(TAG, "onResponse: size: " + size + " previoussize: " + previousSize + "- fullsize: " +tvShowList.size() );
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


    @Override
    public void onClick(int movieId) {
        Intent intentToStartDetailActivity = new Intent(getContext(), TvShowDetailActivity.class);
        intentToStartDetailActivity.putExtra(getString(R.string.intent_key_tv_show), movieId);
        getActivity().startActivity(intentToStartDetailActivity);
    }
}
