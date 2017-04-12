package de.aaronoe.popularmovies.ui.Favorites;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.aaronoe.popularmovies.Data.TvShow.TvShow;
import de.aaronoe.popularmovies.Database.MoviesContract;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.R;
import de.aaronoe.popularmovies.ui.TvShowDetailActivity;

/**
 *
 * Created by aaron on 11.04.17.
 */

public class FavoriteShowsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, FavoriteTvShowsAdaper.TvShowAdapterOnClickHandler{

    Cursor showCursor;
    GridLayoutManager favoriteGridLayout;
    List<TvShow> tvShowList;
    FavoriteTvShowsAdaper tvShowAdapter;
    private static final int FAVORITE_SHOWS_LOADER_ID = 788;

    @BindView(R.id.rv_favorite_fragment)
    RecyclerView rvFavoriteFragment;
    @BindView(R.id.fave_tv_error_message_display)
    TextView faveTvErrorMessageDisplay;
    @BindView(R.id.fave_pb_loading_indicator)
    ProgressBar favePbLoadingIndicator;

    Unbinder unbinder;

    public FavoriteShowsFragment() {
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        favoriteGridLayout = new GridLayoutManager
                (getActivity(), Utilities.calculateNoOfColumnsShow(getActivity()));
        tvShowAdapter = new FavoriteTvShowsAdaper(getActivity(), this, showCursor);
        rvFavoriteFragment.setLayoutManager(favoriteGridLayout);
        rvFavoriteFragment.setAdapter(tvShowAdapter);

        getActivity().getSupportLoaderManager().initLoader(FAVORITE_SHOWS_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                MoviesContract.ShowEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data == null || data.getCount() == 0) {
            showErrorMessage();
            return;
        }

        showCursor = data;
        favePbLoadingIndicator.setVisibility(View.INVISIBLE);
        showFavoriteMovieView();
        tvShowAdapter.setVideoData(showCursor);
        tvShowAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        showCursor = null;
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
            faveTvErrorMessageDisplay.setText(R.string.no_shows_favorites);
        }
    }


    @Override
    public void onClick(int movieId) {
        Intent intentToStartDetailActivity = new Intent(getContext(), TvShowDetailActivity.class);
        intentToStartDetailActivity.putExtra(getString(R.string.intent_key_tv_show), movieId);
        getActivity().startActivity(intentToStartDetailActivity);
    }
}
