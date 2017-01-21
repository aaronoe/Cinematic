package de.aaronoe.popularmovies.Data;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.aaronoe.popularmovies.MovieItem;

/**
 * Parses the Json-Data returned by:
 * http://api.themoviedb.org/3/movie/popular?api_key=de2c61fd451b50de11cee234a5d8346b
 * and
 * http://api.themoviedb.org/3/movie/top_rated?api_key=de2c61fd451b50de11cee234a5d8346b
 * Created by aaron on 21.01.17.
 */

public final class MovieJsonParser {

    // Logging tag
    private static final String TAG = MovieJsonParser.class.getSimpleName();

    public static List<MovieItem> extractMoviesFromJson(String JsonResponse) throws JSONException {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(JsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding movies to
        List<MovieItem> movieItemList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(JsonResponse);

            // Extract the JSONArray associated with the key called "movies",
            // which represents a list of launches
            JSONArray baseMovieList = baseJsonResponse.getJSONArray("results");

            // iterate through the list of movies and create a new {@link MovieItem} object
            for (int i = 0; i < baseMovieList.length(); i++) {

                // get a single movie at the position i
                JSONObject currentMovie = baseMovieList.getJSONObject(i);

                // for now we need the movie's data for the following variables:
                String PosterPath;
                String MovieDescription;
                String Title;
                int MovieId;

                // get the URL-Path for the movie poster, e.g.:
                // "\/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg"
                PosterPath = currentMovie.getString("poster_path");

                // get the movie description, e.g.:
                // "The quiet life of a terrier named Max is upended when his owner takes in Duke,
                // a stray whom Max instantly dislikes."
                MovieDescription = currentMovie.getString("overview");

                // get the movie's title, e.g.:
                // "The Secret Life of Pets"
                Title = currentMovie.getString("title");

                // get a movie's unique identifier, e.g.:
                // 328111
                MovieId = currentMovie.getInt("id");

                // Create a new movie object with the data, we just parsed
                MovieItem thisMovie =
                        new MovieItem(PosterPath, MovieDescription, Title, MovieId);

                movieItemList.add(thisMovie);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(TAG, "Problem parsing the launches JSON results", e);
        }

        return movieItemList;
    }

}
