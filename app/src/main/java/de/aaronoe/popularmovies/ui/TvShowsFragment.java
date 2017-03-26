package de.aaronoe.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class TvShowsFragment extends Fragment {

    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    ApiInterface apiService;
    List<TvShow> tvShowList;
    LinearLayoutManager linearLayout;
    TvShowAdapter tvShowAdaper;


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
        tvShowAdaper = new TvShowAdapter(getActivity());

        rvMainMovieList.setLayoutManager(linearLayout);
        rvMainMovieList.setAdapter(tvShowAdaper);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        downloadShowData();

        return rootView;
    }


    private void downloadShowData() {

        pbLoadingIndicator.setVisibility(View.VISIBLE);

        Call<ShowsResponse> call = apiService.getTvShows(SELECTION_POPULAR, API_KEY);

        call.enqueue(new Callback<ShowsResponse>() {
            @Override
            public void onResponse(Call<ShowsResponse> call, Response<ShowsResponse> response) {
                tvShowList = response.body().getTvShows();

                pbLoadingIndicator.setVisibility(View.INVISIBLE);
                if (tvShowList != null) {
                    showMovieView();
                    tvShowAdaper.setVideoData(tvShowList);
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


    private void showMovieView() {
        /* First, make sure the error is invisible */
        tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        //TODO scroll listener
        // scrollListener.resetState();
        //rvMainMovieList.addOnScrollListener(scrollListener);
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




}
