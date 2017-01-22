package de.aaronoe.popularmovies.Data;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 *
 * Created by aaron on 21.01.17.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();


    private static final String MOVIES_TOP_RATED_BASE_URL =
            "http://api.themoviedb.org/3/movie/top_rated";

    private static final String MOVIES_POPULAR_BASE_URL =
            "http://api.themoviedb.org/3/movie/popular";

    private final static String API_KEY = "de2c61fd451b50de11cee234a5d8346b";
    private final static String KEY_PARAM = "api_key";

    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param filter The filter that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String filter) {
        Uri builtUri = null;

        if (filter == "popular") {
            builtUri = Uri.parse(MOVIES_POPULAR_BASE_URL).buildUpon()
                    .appendQueryParameter(KEY_PARAM, API_KEY)
                    .build();
        } else {
            builtUri = Uri.parse(MOVIES_TOP_RATED_BASE_URL).buildUpon()
                    .appendQueryParameter(KEY_PARAM, API_KEY)
                    .build();
        }

        /*
        switch (filter) {
            case "popular":
                builtUri = Uri.parse(MOVIES_POPULAR_BASE_URL).buildUpon()
                        .appendQueryParameter(KEY_PARAM, API_KEY)
                        .build();
            case "top":
                builtUri = Uri.parse(MOVIES_TOP_RATED_BASE_URL).buildUpon()
                        .appendQueryParameter(KEY_PARAM, API_KEY)
                        .build();
        }
        */


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
