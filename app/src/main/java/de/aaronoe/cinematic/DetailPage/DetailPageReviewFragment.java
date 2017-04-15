package de.aaronoe.cinematic.DetailPage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.cinematic.BuildConfig;
import de.aaronoe.cinematic.Data.ApiClient;
import de.aaronoe.cinematic.Data.ApiInterface;
import de.aaronoe.cinematic.Movies.MovieItem;
import de.aaronoe.cinematic.Movies.ReviewItem;
import de.aaronoe.cinematic.Movies.ReviewResponse;
import de.aaronoe.cinematic.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by aaron on 22.02.17.
 */

public class DetailPageReviewFragment extends android.support.v4.app.Fragment {

    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    @BindView(R.id.recyclerView_videos) RecyclerView mRecyclerView;
    @BindView(R.id.video_tv_error_message_display) TextView mErrorMessageTextView;
    @BindView(R.id.video_pb_loading_indicator) ProgressBar mProgressBar;
    @BindView(R.id.tv_no_items_available) TextView mNotAvailableTextView;

    ApiInterface apiService;
    MovieItem mMovieItem;
    ReviewAdapter mReviewAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_page_videos, container, false);
        ButterKnife.bind(this, rootView);
        mMovieItem = getArguments().getParcelable("thisMovie");

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mReviewAdapter = new ReviewAdapter(getContext());
        mRecyclerView.setAdapter(mReviewAdapter);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        downloadReviewData();
        return rootView;
    }


    private void downloadReviewData() {

        mProgressBar.setVisibility(View.VISIBLE);
        int movieId = mMovieItem.getId();

        Call<ReviewResponse> call = apiService.getReviews(movieId, API_KEY);

        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {

                List<ReviewItem> videoItems = response.body().getResults();

                if (videoItems.size() == 0) {
                    showNotAvailableView();
                    return;
                }

                if (videoItems != null) {
                    showReviewView();
                    mReviewAdapter.setReviewData(videoItems);
                } else {
                    showErrorMessage();
                }

            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                showErrorMessage();
            }
        });


    }


    private void showNotAvailableView() {
        /* First, make sure the error is invisible */
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.INVISIBLE);
        mNotAvailableTextView.setVisibility(View.VISIBLE);
    }

    private void showReviewView() {
        mNotAvailableTextView.setVisibility(View.INVISIBLE);
        /* First, make sure the error is invisible */
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mNotAvailableTextView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }


    public static android.support.v4.app.Fragment newInstance(MovieItem mCurrentMovie) {
        DetailPageReviewFragment myDetailFragment = new DetailPageReviewFragment();

        Bundle movie = new Bundle();
        movie.putParcelable("thisMovie", mCurrentMovie);
        myDetailFragment.setArguments(movie);

        return myDetailFragment;
    }



}
