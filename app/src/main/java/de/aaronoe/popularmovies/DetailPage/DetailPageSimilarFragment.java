package de.aaronoe.popularmovies.DetailPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.BuildConfig;
import de.aaronoe.popularmovies.Data.ApiClient;
import de.aaronoe.popularmovies.Data.ApiInterface;
import de.aaronoe.popularmovies.Data.MovieAdapter;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.Movies.MovieItem;
import de.aaronoe.popularmovies.Movies.MovieResponse;
import de.aaronoe.popularmovies.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by aaron on 10.03.17.
 */

public class DetailPageSimilarFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler{

    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    @BindView(R.id.recyclerView_videos)
    RecyclerView mRecyclerView;
    @BindView(R.id.video_tv_error_message_display)
    TextView mErrorMessageTextView;
    @BindView(R.id.video_pb_loading_indicator)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_no_items_available) TextView mNotAvailableTextView;

    ApiInterface apiService;
    MovieItem mMovieItem;
    StaggeredGridLayoutManager gridLayout;
    public MovieAdapter mMovieAdapter;
    List<MovieItem> movieItemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_page_videos, container, false);
        ButterKnife.bind(this, rootView);
        mMovieItem = getArguments().getParcelable("thisMovie");

        gridLayout = new StaggeredGridLayoutManager
                (Utilities.calculateNoOfColumnsShow(getActivity()), StaggeredGridLayoutManager.VERTICAL);


        mRecyclerView.setLayoutManager(gridLayout);
        mMovieAdapter = new MovieAdapter(this, getActivity());
        mRecyclerView.setAdapter(mMovieAdapter);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        downloadSimilarMovieData(mMovieItem.getId());

        return rootView;
    }


    private void downloadSimilarMovieData(int movieId) {

        mProgressBar.setVisibility(View.VISIBLE);

        Call<MovieResponse> call = apiService.getRecommendations(movieId, API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movieItemList = response.body().getResults();

                mProgressBar.setVisibility(View.INVISIBLE);
                if (movieItemList != null) {
                    showMovieView();
                    mMovieAdapter.setMovieData(movieItemList);
                } else {
                    showErrorMessage();
                }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                showErrorMessage();
            }
        });

    }

    private void showMovieView() {
        /* First, make sure the error is invisible */
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    public static android.support.v4.app.Fragment newInstance(MovieItem mCurrentMovie) {
        DetailPageSimilarFragment myDetailFragment = new DetailPageSimilarFragment();

        Bundle movie = new Bundle();
        movie.putParcelable("thisMovie", mCurrentMovie);
        myDetailFragment.setArguments(movie);

        return myDetailFragment;
    }


    @Override
    public void onClick(MovieItem movieItem) {
        Intent intentToStartDetailActivity = new Intent(getActivity(), DetailActivity.class);
        intentToStartDetailActivity.putExtra("MovieId", movieItem.getId());
        intentToStartDetailActivity.putExtra(getString(R.string.intent_key_movie_name), movieItem.getTitle());
        startActivity(intentToStartDetailActivity);
    }
}
