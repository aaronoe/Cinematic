package de.aaronoe.popularmovies.ui.Favorites;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.aaronoe.popularmovies.Database.MoviesContract;
import de.aaronoe.popularmovies.Database.Utilities;
import de.aaronoe.popularmovies.R;

/**
 *
 * Created by aaronoe on 11.04.17.
 */

public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    StaggeredGridLayoutManager favoriteGridLayout;

    public MoviesFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_main, container);

        favoriteGridLayout = new StaggeredGridLayoutManager
                (Utilities.calculateNoOfColumns(getActivity()), StaggeredGridLayoutManager.VERTICAL);

        return rootview;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
