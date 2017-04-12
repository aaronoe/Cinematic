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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.aaronoe.popularmovies.Data.MovieAdapter;
import de.aaronoe.popularmovies.Database.MoviesContract;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.DetailPage.DetailActivity;
import de.aaronoe.popularmovies.Movies.MovieItem;
import de.aaronoe.popularmovies.R;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

/**
 *
 * Created by aaronoe on 11.04.17.
 */

public class FavoriteMoviesFragment extends android.support.v4.app.Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        MovieAdapter.MovieAdapterOnClickHandler {

    StaggeredGridLayoutManager favoriteGridLayout;
    MovieAdapter movieAdapter;
    List<MovieItem> movieItemList;
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
        View rootview = inflater.inflate(R.layout.favorites_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootview);

        favoriteGridLayout = new StaggeredGridLayoutManager
                (Utilities.calculateNoOfColumns(getActivity()), StaggeredGridLayoutManager.VERTICAL);
        movieAdapter = new MovieAdapter(this);
        rvFavoriteFragment.setAdapter(movieAdapter);
        rvFavoriteFragment.setLayoutManager(favoriteGridLayout);

        getActivity().getSupportLoaderManager().restartLoader(FAVORITE_MOVIES_LOADER_ID, null, this);

        return rootview;
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
            faveTvErrorMessageDisplay.setText(getString(R.string.error_message));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(MovieItem movieItem) {
        Intent intentToStartDetailActivity = new Intent(getActivity(), DetailActivity.class);
        intentToStartDetailActivity.putExtra("MovieItem", movieItem);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader
                (getActivity(), MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieItemList = Utilities.extractMovieItemFromCursor(data);
        favePbLoadingIndicator.setVisibility(View.INVISIBLE);
        Log.d(TAG, "onLoadFinished: "+ movieItemList.size());

        if (movieItemList != null) {
            showFavoriteMovieView();
            movieAdapter.setMovieData(movieItemList);
            movieAdapter.notifyDataSetChanged();
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
