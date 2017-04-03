package de.aaronoe.popularmovies.Database;

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
    public static final String ACTION_INSERT = TAG + ".INSERT";
    public static final String ACTION_DELETE = TAG + ".DELETE";

    public static final String EXTRA_VALUES = TAG + ".ContentValues";

    public MovieUpdateService() {super(TAG);}

    public static void insertNewMovie(Context context, ContentValues values) {
        Intent intent = new Intent(context, MovieUpdateService.class);
        intent.setAction(ACTION_INSERT);
        intent.putExtra(EXTRA_VALUES, values);
        context.startService(intent);
    }

    public static void deleteTask(Context context, Uri uri) {
        Intent intent = new Intent(context, MovieUpdateService.class);
        intent.setAction(ACTION_DELETE);
        intent.setData(uri);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (ACTION_INSERT.equals(intent.getAction())) {
            ContentValues values = intent.getParcelableExtra(EXTRA_VALUES);
            performInsert(values);
        } else if (ACTION_DELETE.equals(intent.getAction())) {
            performDelete(intent.getData());
        }

    }

    private void performInsert(ContentValues values) {
        if (getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, values) != null) {
            Log.d(TAG, "Inserted new task");
        } else {
            Log.w(TAG, "Error inserting new task");
        }
    }

    private void performDelete(Uri uri) {
        int count = getContentResolver().delete(uri, null, null);

        Log.d(TAG, "Deleted "+count+" tasks");
    }


}
