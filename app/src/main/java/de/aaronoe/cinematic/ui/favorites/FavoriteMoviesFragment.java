package de.aaronoe.cinematic.ui.favorites;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.aaronoe.cinematic.R;
import de.aaronoe.cinematic.database.Utilities;
import de.aaronoe.cinematic.model.MovieAdapter;
import de.aaronoe.cinematic.movies.MovieItem;
import de.aaronoe.cinematic.ui.redesign.moviedetail.MovieDetailActivity;
import de.aaronoe.cinematic.util.Constants;
import io.realm.Realm;

/**
 *
 * Created by aaronoe on 11.04.17.
 */

public class FavoriteMoviesFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler {

    StaggeredGridLayoutManager favoriteGridLayout;
    MovieAdapter moviesAdapter;

    @BindView(R.id.rv_favorite_fragment)
    RecyclerView rvFavoriteFragment;
    @BindView(R.id.fave_tv_error_message_display)
    TextView faveTvErrorMessageDisplay;
    @BindView(R.id.fave_pb_loading_indicator)
    ProgressBar favePbLoadingIndicator;
    Unbinder unbinder;
    Realm realm;

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
                (Utilities.calculateNoOfColumnsShow(getActivity()), StaggeredGridLayoutManager.VERTICAL);
        moviesAdapter = new MovieAdapter(this, getActivity());
        rvFavoriteFragment.setAdapter(moviesAdapter);
        rvFavoriteFragment.setLayoutManager(favoriteGridLayout);

        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                moviesAdapter.setMovieData(realm.where(MovieItem.class).findAll());
                if (moviesAdapter.getItemCount() == 0) {
                    showErrorMessage();
                } else {
                    showFavoriteMovieView();
                }
            }
        });

        return rootView;
    }


    private void showFavoriteMovieView() {
        /* First, make sure the error is invisible */
        faveTvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        favePbLoadingIndicator.setVisibility(View.INVISIBLE);
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
        realm.close();
    }


    @Override
    public void onClick(MovieItem movieItem, ImageView backdropImageView) {
        Intent intentToStartDetailActivity = new Intent(getActivity(), MovieDetailActivity.class);
        intentToStartDetailActivity.putExtra(getString(R.string.INTENT_KEY_MOVIE), movieItem);
        intentToStartDetailActivity.putExtra(getString(R.string.intent_transition_enter_mode), Constants.BACKDROP_ENTER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), backdropImageView, getString(R.string.transition_shared_key));
            getActivity().startActivity(intentToStartDetailActivity, options.toBundle());
        } else {
            getActivity().startActivity(intentToStartDetailActivity);
        }
    }
}
