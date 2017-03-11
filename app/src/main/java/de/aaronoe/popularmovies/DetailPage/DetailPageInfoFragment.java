package de.aaronoe.popularmovies.DetailPage;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.aaronoe.popularmovies.BuildConfig;
import de.aaronoe.popularmovies.Data.ApiClient;
import de.aaronoe.popularmovies.Data.ApiInterface;
import de.aaronoe.popularmovies.Data.Crew.Cast;
import de.aaronoe.popularmovies.Data.Crew.Credits;
import de.aaronoe.popularmovies.Data.Crew.CrewAdapter;
import de.aaronoe.popularmovies.Database.MoviesContract;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.Movies.MovieItem;
import de.aaronoe.popularmovies.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by aaron on 21.02.17.
 */

public class DetailPageInfoFragment extends Fragment {

    MovieItem mMovieItem;
    private final static String API_KEY = BuildConfig.MOVIE_DB_API_KEY;

    @BindView(R.id.tv_movie_rating) TextView ratingTextView;
    @BindView(R.id.tv_movie_date) TextView dateTextView;
    @BindView(R.id.tv_movie_description) TextView descriptionTextView;
    @BindView(R.id.toggleFavoriteButton) ToggleButton toggleFavoriteButton;
    @BindView(R.id.actor_rv) RecyclerView mActorRecyclerView;

    ApiInterface apiService;
    List<Cast> castList;
    CrewAdapter crewAdapter;

    public DetailPageInfoFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detailpage_info, container, false);

        ButterKnife.bind(this, rootView);

        mMovieItem = getArguments().getParcelable("thisMovie");

        // Check if movie already is a favorite
        if (isMovieFavorite(mMovieItem)) {
            toggleFavoriteButton.setTextOn(getString(R.string.button_on));
            toggleFavoriteButton.setChecked(true);
        } else {
            toggleFavoriteButton.setTextOff(getString(R.string.button_off));
            toggleFavoriteButton.setChecked(false);
        }

        toggleFavoriteButton.setOnCheckedChangeListener(favoriteChangeListener);

        Log.d(DetailPageInfoFragment.class.getSimpleName(), "Title: " + mMovieItem.getmTitle());

        String ratingText = mMovieItem.getmVoteAverage() + "/10";
        ratingTextView.setText(ratingText);

        dateTextView.setText(Utilities.convertDate(mMovieItem.getmReleaseDate()));

        descriptionTextView.setText(mMovieItem.getmMovieDescription());

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        mActorRecyclerView.setLayoutManager(linearLayoutManager);
        crewAdapter = new CrewAdapter(getActivity());
        mActorRecyclerView.setAdapter(crewAdapter);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        downloadCredits(mMovieItem.getmMovieId());

        return rootView;

    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static android.support.v4.app.Fragment newInstance(MovieItem mCurrentMovie) {
        DetailPageInfoFragment myDetailFragment = new DetailPageInfoFragment();

        Bundle movie = new Bundle();
        movie.putParcelable("thisMovie", mCurrentMovie);
        myDetailFragment.setArguments(movie);

        return myDetailFragment;
    }


    /**
     * Checks if this movie is already a user's favorite
     * @param movieItem current movie
     * @return true if it is, false if it's not a favorite
     */
    private boolean isMovieFavorite(MovieItem movieItem) {

        int id = movieItem.getmMovieId();
        String[] selection = new String[]{Integer.toString(movieItem.getmMovieId())};

        Cursor result =
                getActivity().getContentResolver().query(
                        MoviesContract.MovieEntry.CONTENT_URI,
                        null,
                        MoviesContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                        selection,
                        null);

        return (result != null && result.getCount() > 0);
    }


    /**
     * This function handles Toggle-Button change events, and hence performs database operations
     * to favorite or un-favorite movies
     */
    private CompoundButton.OnCheckedChangeListener favoriteChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                // Toggle is enabled

                Uri uri = getActivity().getContentResolver().insert(
                        MoviesContract.MovieEntry.CONTENT_URI,
                        Utilities.getContentValuesForMovie(mMovieItem));

                Toast.makeText(getActivity(), getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show();


            } else {

                int numberOfItemsDeleted;
                int movieId = mMovieItem.getmMovieId();
                Uri deleteUri = MoviesContract.MovieEntry.CONTENT_URI.buildUpon().
                        appendPath(Integer.toString(movieId)).build();


                numberOfItemsDeleted = getActivity().getContentResolver().delete(
                        deleteUri,
                        null,
                        null
                );

                if (numberOfItemsDeleted > 0) Log.e("DetailActivity: ", "Items deleted: " + numberOfItemsDeleted);

            }
        }
    };


    /**
     * Download credits for a given movie
     * @param movieId unique identifier for the given movie, used to query moviedb API
     */
    private void downloadCredits(int movieId) {

        Call<Credits> call = apiService.getCredits(movieId, API_KEY);

        call.enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                castList = response.body().getCast();
                mActorRecyclerView.setVisibility(View.VISIBLE);
                if (castList != null) {
                    crewAdapter.setCastData(castList);
                } else {
                    mActorRecyclerView.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<Credits> call, Throwable t) {
                mActorRecyclerView.setVisibility(View.INVISIBLE);
            }
        });

    }



}
