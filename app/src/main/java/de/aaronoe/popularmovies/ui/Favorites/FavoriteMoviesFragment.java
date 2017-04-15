package de.aaronoe.popularmovies.ui.Favorites;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.aaronoe.popularmovies.Database.MoviesContract;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.DetailPage.DetailActivity;
import de.aaronoe.popularmovies.R;

/**
 *
 * Created by aaronoe on 11.04.17.
 */

public class FavoriteMoviesFragment extends android.support.v4.app.Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        FavoriteMoviesAdapter.MovieAdapterOnClickHandler {

    StaggeredGridLayoutManager favoriteGridLayout;
    FavoriteMoviesAdapter favoriteMoviesAdapter;
    private static final int FAVORITE_MOVIES_LOADER_ID = 314;


    @BindView(R.id.rv_favorite_fragment)
    RecyclerView rvFavoriteFragment;
    @BindView(R.id.fave_tv_error_message_display)
    TextView faveTvErrorMessageDisplay;
    @BindView(R.id.fave_pb_loading_indicator)
    ProgressBar favePbLoadingIndicator;
    Unbinder unbinder;

    public FavoriteMoviesFragment() {}


    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        favoriteGridLayout = new StaggeredGridLayoutManager
                (Utilities.calculateNoOfColumns(getActivity()), StaggeredGridLayoutManager.VERTICAL);
        favoriteMoviesAdapter = new FavoriteMoviesAdapter(getActivity(), this);
        rvFavoriteFragment.setAdapter(favoriteMoviesAdapter);
        rvFavoriteFragment.setLayoutManager(favoriteGridLayout);

        getActivity().getSupportLoaderManager().initLoader(FAVORITE_MOVIES_LOADER_ID, null, this);

        return rootView;
    }


    private void showFavoriteMovieView() {
        /* First, make sure the error is invisible */
        faveTvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        rvFavoriteFragment.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        rvFavoriteFragment.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        faveTvErrorMessageDisplay.setVisibility(View.VISIBLE);

        // check if network conditions exists
        if (!Utilities.isOnline(getActivity())) {
            faveTvErrorMessageDisplay.setText(getString(R.string.no_network_connection));
        } else {
            faveTvErrorMessageDisplay.setText(R.string.no_movies_favorites);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader
                (getActivity(), MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data == null || data.getCount() == 0) {
            showErrorMessage();
            return;
        }
        favePbLoadingIndicator.setVisibility(View.INVISIBLE);
        showFavoriteMovieView();
        favoriteMoviesAdapter.changeCursor(data);
        favoriteMoviesAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onClick(int movieId, String movieName) {
        Intent intentToStartDetailActivity = new Intent(getActivity(), DetailActivity.class);
        intentToStartDetailActivity.putExtra("MovieId", movieId);
        intentToStartDetailActivity.putExtra(getString(R.string.intent_key_movie_name), movieName);
        startActivity(intentToStartDetailActivity);
    }
}
