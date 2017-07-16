package de.aaronoe.cinematic.database;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 *
 * Created by aaron on 03.04.17.
 */

public class MovieUpdateService extends IntentService {

    private static final String TAG = "MovieUpdateService";
    //Intent actions
    public static final String ACTION_INSERT_MOVIES = TAG + ".INSERT_MOVIES";
    public static final String ACTION_INSERT_SHOWS = TAG + ".INSERT_SHOWS";
    public static final String ACTION_DELETE = TAG + ".DELETE";

    public static final String EXTRA_VALUES = TAG + ".ContentValues";

    public MovieUpdateService() {super(TAG);}

    public static void insertNewMovie(Context context, ContentValues values) {
        Intent intent = new Intent(context, MovieUpdateService.class);
        intent.setAction(ACTION_INSERT_MOVIES);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void insertNewShow(Context context, ContentValues values) {
        Intent intent = new Intent(context, MovieUpdateService.class);
        intent.setAction(ACTION_INSERT_SHOWS);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void deleteItem(Context context, Uri uri) {
        Intent intent = new Intent(context, MovieUpdateService.class);
        intent.setAction(ACTION_DELETE);
        intent.setData(uri);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (ACTION_INSERT_MOVIES.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performInsertMovies(values);
        } else if (ACTION_DELETE.equals(intent.getAction())) {
            performDelete(intent.getData());
        } else if (ACTION_INSERT_SHOWS.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performInsertShows(values);
        }

    }

    private void performInsertMovies(ContentValues values) {
        if (getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, values) != null) {
            Log.d(TAG, "Inserted new movie");
        } else {
            Log.w(TAG, "Error inserting new movie");
        }
    }

    private void performInsertShows(ContentValues values) {
        if (getContentResolver().insert(MoviesContract.ShowEntry.CONTENT_URI, values) != null) {
            Log.d(TAG, "Inserted new show");
        } else {
            Log.w(TAG, "Error inserting new show");
        }
    }

    private void performDelete(Uri uri) {
        int count = getContentResolver().delete(uri, null, null);

        Log.d(TAG, "Deleted "+count+" movies/shows");
    }

}
