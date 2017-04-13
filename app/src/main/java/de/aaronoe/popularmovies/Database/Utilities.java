package de.aaronoe.popularmovies.Database;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.aaronoe.popularmovies.Data.TvShow.FullShow.Genre;
import de.aaronoe.popularmovies.Data.TvShow.FullShow.TvShowFull;
import de.aaronoe.popularmovies.Database.MoviesContract.MovieEntry;
import de.aaronoe.popularmovies.Database.MoviesContract.ShowEntry;
import de.aaronoe.popularmovies.Movies.MovieItem;
import de.aaronoe.popularmovies.R;

import static android.content.ContentValues.TAG;

/**
 *
 * Created by aaronoe on 20.02.17.
 */

public class Utilities {



    static HashMap<Integer, String> movieGenres = new HashMap<>();



    /**
     * This function takes the required fields of a {@link MovieItem} and puts them into
     * a {@link ContentValues} object to use the {@link MoviesContentProvider} to insert a
     * movie into the underlying Database ({@link MoviesDbHelper})
     *
     * @param movieItem a single MovieItem
     * @return a {@link ContentValues} object containing the required information to put the movie into the db
     */
    public static ContentValues getContentValuesForMovie(MovieItem movieItem) {

        ContentValues cv = new ContentValues();

        cv.put(MovieEntry.COLUMN_POSTER_PATH, movieItem.getPosterPath());
        cv.put(MovieEntry.COLUMN_DESCRIPTION, movieItem.getOverview());
        cv.put(MovieEntry.COLUMN_TITLE, movieItem.getTitle());
        cv.put(MovieEntry.COLUMN_MOVIE_ID, movieItem.getId());
        cv.put(MovieEntry.COLUMN_RELEASE_DATE, movieItem.getReleaseDate());
        cv.put(MovieEntry.COLUMN_VOTE_AVERAGE, movieItem.getVoteAverage());
        cv.put(MovieEntry.COLUMN_BACKDROP_PATH, movieItem.getBackdropPath());

        return cv;

    }


    public static ContentValues getContentValuesForShow(TvShowFull tvShowFull, Context context) {

        ContentValues cv = new ContentValues();

        cv.put(ShowEntry.COLUMN_ID, tvShowFull.getId());
        cv.put(ShowEntry.COLUMN_TITLE, tvShowFull.getName());
        cv.put(ShowEntry.COLUMN_BACKDROP_PATH, tvShowFull.getBackdropPath());
        cv.put(ShowEntry.COLUMN_FIRST_AIR_DATE, tvShowFull.getFirstAirDate());
        cv.put(ShowEntry.COLUMN_VOTE_AVERAGE, tvShowFull.getVoteAverage());

        List<Genre> genreList = tvShowFull.getGenres();
        String genreString = "";
        for (int i = 0; i < genreList.size(); i++) {
            if (i != 0) {
                genreString += ", ";
            }
            genreString += genreList.get(i).getName();
        }

        cv.put(ShowEntry.COLUMN_GENRES, genreString);

        return cv;
    }



    /**
     * Converts a date returned by the API into a different format.
     *
     * @param sourceDate a string representing a date in this format: 2015-12-15
     * @return a string representing a date in this format: December 15, 2015
     */
    public static String convertDate(String sourceDate) {

        DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

        if (sourceDate == null || sourceDate.equals("")) return null;

        Date date = null;
        try {
            date = sourceFormat.parse(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Error formatting the date");
            return null;
        }
        return targetFormat.format(date);

    }

    public static String convertDateShort(String sourceDate) {

        DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("MMM. dd, yy", Locale.ENGLISH);

        if (sourceDate == null || sourceDate.equals("")) return null;

        Date date = null;
        try {
            date = sourceFormat.parse(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Error formatting the date");
            return null;
        }
        return targetFormat.format(date);

    }

    /**
     * Get the difference between a date and the current point in time in days
     * @param sourceDate the source date
     * @return the difference in days
     */
    public static long computeDifferenceInDays(String sourceDate) {
        DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        if (sourceDate == null || sourceDate.equals("")) return Long.MAX_VALUE;

        Date date = null;
        try {
            date = sourceFormat.parse(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Error formatting the date");
            return Long.MAX_VALUE;
        }

        return getDateDiff(date, new Date(System.currentTimeMillis()), TimeUnit.DAYS);

    }

    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * Converts a date returned by the API into a different format.
     *
     * @param sourceDate a string representing a date in this format: 2015-12-15
     * @return a string representing a date in this format: 2015
     */
    public static String convertDateToYear(String sourceDate) {

        DateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

        if (sourceDate == null || sourceDate.equals("")) return null;

        Date date = null;
        try {
            date = sourceFormat.parse(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Error formatting the date");
        }
        return targetFormat.format(date);

    }

    /**
     * Checks if user is connected to a network to download data
     * @return true if user is connected to a network
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    public static int calculateNoOfColumnsShow(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 220);
    }

    public static Uri buildShowUri(int showId) {
        return Uri.withAppendedPath(ShowEntry.CONTENT_URI, String.valueOf(showId));
    }


    public static String extractMovieGenres(List<Integer> genres, Context mContext) {

        movieGenres = new HashMap<>();
        movieGenres.put(28, "Action");
        movieGenres.put(12, "Adventure");
        movieGenres.put(16, "Animation");
        movieGenres.put(35, "Comedy");
        movieGenres.put(80, "Crime");
        movieGenres.put(99, "Documentary");
        movieGenres.put(18, "Drama");
        movieGenres.put(10751, "Family");
        movieGenres.put(14, "Fantasy");
        movieGenres.put(36, "History");
        movieGenres.put(27, "Horror");
        movieGenres.put(10402, "Music");
        movieGenres.put(9648, "Mystery");
        movieGenres.put(10749, "Romance");
        movieGenres.put(878, "Science Fiction");
        movieGenres.put(10770, "TV Movie");
        movieGenres.put(53, "Thriller");

        final String SEPARATOR = ", ";
        if (genres == null || genres.size() == 0) return null;

        List<String> result = new ArrayList<>();

        for (int id : genres) {
            if (movieGenres.containsKey(id)) {
                result.add(movieGenres.get(id));
            } else {
                Log.d(TAG, "extractGenres: " + id);
            }
        }

        StringBuilder resBuilder = new StringBuilder();

        for (String item : result) {
            resBuilder.append(item);
            resBuilder.append(SEPARATOR);
        }

        String list = resBuilder.toString();
        if (list.length() == 0) return mContext.getString(R.string.genre_not_available);
        return list.substring(0, list.length() - SEPARATOR.length());

    }


}
