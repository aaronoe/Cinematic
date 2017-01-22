package de.aaronoe.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import de.aaronoe.popularmovies.Data.MovieAdapter;
import de.aaronoe.popularmovies.Data.MovieJsonParser;
import de.aaronoe.popularmovies.Data.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    // for debugging purposes
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    public MovieAdapter mMovieAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate member variables:
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main_movie_list);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        GridLayoutManager gridLayout =
                new GridLayoutManager(MainActivity.this, 2);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(gridLayout);
        //mRecyclerView.hasFixedSize(true);
        mMovieAdapter = new MovieAdapter();
        mRecyclerView.setAdapter(mMovieAdapter);

        showMovieData("popular");

    }


    private void showMovieData(String setting) {
        showMovieView();
        new FetchMovieDataTask().execute(setting);
    }


    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     *
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showMovieView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * This method will make the error message visible and hide the weather
     * View.
     *
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }



    public class FetchMovieDataTask extends AsyncTask<String, Void, List<MovieItem>> {


        @Override
        protected void onPostExecute(List<MovieItem> movieItems) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieItems != null) {
                showMovieView();
                mMovieAdapter.setMovieData(movieItems);
            } else {
                showErrorMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }


        @Override
        protected List<MovieItem> doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }
            // parse the URL
            String filter = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(filter);

            // try to download the data and turn it into a list of MovieItems
            try {
                // Download the JSON-Response
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                // Parse the JSON-Response
                List<MovieItem> movieList = MovieJsonParser.extractMoviesFromJson(jsonMovieResponse);

                return movieList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }







    }


}
