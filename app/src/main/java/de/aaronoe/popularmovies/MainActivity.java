package de.aaronoe.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.List;

import de.aaronoe.popularmovies.Data.MovieAdapter;
import de.aaronoe.popularmovies.Data.MovieJsonParser;
import de.aaronoe.popularmovies.Data.NetworkUtils;


public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler {

    // for debugging purposes
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    public MovieAdapter mMovieAdapter;
    String mCurrentSelection;
    final String SELECTION_POPULAR = "popular";
    final String SELECTION_TOP_RATED = "top";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // popular or top
        mCurrentSelection = SELECTION_POPULAR;

        // instantiate member variables:
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main_movie_list);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        GridLayoutManager gridLayout =
                new GridLayoutManager(MainActivity.this, 2);

        mRecyclerView.setLayoutManager(gridLayout);
        //mRecyclerView.hasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        showMovieData(mCurrentSelection);

    }


    /**
     * Start the asynchronous download task with the passed in setting
     * @param setting single String setting which represents the filter option for the download,
     *                can be either "top" or "popular"
     */
    private void showMovieData(String setting) {
        showMovieView();
        new FetchMovieDataTask().execute(setting);
    }


    @Override
    public void onClick(MovieItem movieItem) {
        Intent intentToStartDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
        intentToStartDetailActivity.putExtra("MovieItem", movieItem);
        startActivity(intentToStartDetailActivity);
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


        /**
         * Feed the downloaded movie data to the adapter to populate the RecyclerView
         * @param movieItems List of movie items, which was downlaoded in doInBackground
         */
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

        /**
         * Set the UI to show the loading indicator
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }


        /**
         * Downloads the movie data asynchronously from the web service
         * @param params single String filter, either "top" or "popular"
         * @return list of downloaded movie items
         */
        @Override
        protected List<MovieItem> doInBackground(String... params) {

            /* If there's no setting, there's nothing to look up. */
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
                return MovieJsonParser.extractMoviesFromJson(jsonMovieResponse);

            } catch (Exception e) {
                Log.e(TAG, "Error parsing the URL or downloading the movie data");
                e.printStackTrace();
                return null;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.movie_filter, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {

            // return if the option is already selected
            if (mCurrentSelection.equals(SELECTION_POPULAR)) {
                Toast.makeText(this, "This option is already selected", Toast.LENGTH_SHORT).show();
                return true;
            }

            mMovieAdapter.setMovieData(null);
            mCurrentSelection = SELECTION_POPULAR;
            showMovieData(mCurrentSelection);
            return true;
        }

        if (id == R.id.action_top_rated) {

            // return if the option is already selected
            if (mCurrentSelection.equals(SELECTION_TOP_RATED)) {
                Toast.makeText(this, "This option is already selected", Toast.LENGTH_SHORT).show();
                return true;
            }

            mMovieAdapter.setMovieData(null);
            mCurrentSelection = SELECTION_TOP_RATED;
            showMovieData(mCurrentSelection);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
