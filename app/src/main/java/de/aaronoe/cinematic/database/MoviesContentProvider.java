package de.aaronoe.cinematic.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import de.aaronoe.cinematic.database.MoviesContract.MovieEntry;
import de.aaronoe.cinematic.database.MoviesContract.ShowEntry;
import de.aaronoe.cinematic.ui.detailpage.DetailActivity;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;
import static de.aaronoe.cinematic.database.MoviesContract.MovieEntry.TABLE_NAME;

/**
 *
 * Created by aaron on 20.02.17.
 */

public class MoviesContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;
    private static final int SHOWS = 671;
    private static final int SHOWS_WITH_ID = 470;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    /**
     * This function builds the URI matcher
     * @return {@link UriMatcher} which matches Content Uri's to the corresponding tasks
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI
                (MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_FAVORITE, MOVIES);
        uriMatcher.addURI
                (MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_FAVORITE + "/#", MOVIES_WITH_ID);
        uriMatcher.addURI
                (MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_FAVORITE_SHOWS, SHOWS);
        uriMatcher.addURI
                (MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_FAVORITE_SHOWS + "/#", SHOWS_WITH_ID);

        return uriMatcher;
    }



    private MoviesDbHelper mMoviesDbHelper;


    @Override
    public boolean onCreate() {
        mMoviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    /**
     * Query the items in the favorite movies database. This operation will be used in the
     * to display a list of the user's favorite movies. Hence, for now, we just
     * need to return all the items in the table
     * @param uri to specify action
     * @param projection columns to be returned
     * @param selection filter
     * @param selectionArgs filter arguments
     * @param sortOrder not necessary
     * @return Cursor, pointing to the queried result
     */
    @Override
    public Cursor query(@NonNull  Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        Cursor result;
        Log.d(TAG, "query() called with: uri = [" + uri + "], projection = [" + projection + "], selection = [" + selection + "], selectionArgs = [" + selectionArgs + "], sortOrder = [" + sortOrder + "]");

        switch (sUriMatcher.match(uri)) {

            case MOVIES:

                result = db.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            case MOVIES_WITH_ID:

                String id = uri.getPathSegments().get(1);

                result = db.query(
                        TABLE_NAME,
                        projection,
                        MovieEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{id},
                        null,
                        null,
                        null);

                break;

            case SHOWS_WITH_ID:

                String show_id = uri.getPathSegments().get(1);

                result = db.query(
                        ShowEntry.TABLE_NAME,
                        projection,
                        ShowEntry.COLUMN_ID + "=?",
                        new String[]{show_id},
                        null,
                        null,
                        null);

                break;

            case SHOWS:

                result = db.query(
                        ShowEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            default:
                throw new UnsupportedOperationException("Unsupported Operation for: "+ uri);

        }

        result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;

    }


    /**
     * Used to insert a movie into the database of favorite movies. Since this operation can only
     * be performed on a single movie by clicking the button in
     * {@link DetailActivity} we only need to implement functionality to insert
     * one item at a time
     *
     * @param uri to specify action
     * @param values key-value pairs for the data to be inserted
     * @return Uri of item inserted
     */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        Log.d(TAG, "insert() called with: uri = [" + uri + "], values = [" + values + "]");

        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {

            // We use this as a default case, since we will only be adding one movie at a time
            case MOVIES:

                Log.e(MoviesContentProvider.class.getSimpleName(), "Insert called with Uri: " + uri);

                long id = db.insert(
                        TABLE_NAME,
                        null,
                        values);

                Log.e(MoviesContentProvider.class.getSimpleName(), "Insert called with ID: " + id);


                if (id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
                } else {
                    throw new SQLiteException("Failed to insert row into " + uri);
                }

                break;

            case SHOWS:

                long show_id = db.insert(
                        ShowEntry.TABLE_NAME,
                        null,
                        values);

                if (show_id > 0) {
                    returnUri = ContentUris.withAppendedId(ShowEntry.CONTENT_URI, show_id);
                } else {
                    throw new SQLiteException("Failed to insert row into " + uri);
                }

                break;

            default:
                throw new UnsupportedOperationException("Unsupported Operation for: " + uri);

        }

        return returnUri;

    }


    /**
     * Used to delete a movie from the favorites when the corresponding button is clicked in
     * {@link DetailActivity}. Since we will only be deleting one movie
     * at a time
     * @param uri to specify operation
     * @param selection filter
     * @param selectionArgs filter selection
     * @return number of rows deleted
     */
    @Override
    public int delete(@NonNull  Uri uri, String selection, String[] selectionArgs) {

        Log.d(TAG, "delete() called with: uri = [" + uri + "], selection = [" + selection + "], selectionArgs = [" + selectionArgs + "]");

        int numberOfRowsDeleted;

        SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case MOVIES_WITH_ID:

                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                numberOfRowsDeleted = db.delete(
                        TABLE_NAME,
                        MovieEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{id});

                break;

            case SHOWS_WITH_ID:

                String showId = uri.getPathSegments().get(1);

                numberOfRowsDeleted = db.delete(
                        ShowEntry.TABLE_NAME,
                        ShowEntry.COLUMN_ID + "=?",
                        new String[]{showId});

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        // Notify the resolver of a change and return the number of items deleted
        if (numberOfRowsDeleted != 0) {
            // A task was deleted, set notification
            try {
                getContext().getContentResolver().notifyChange(uri, null);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        return numberOfRowsDeleted;


    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


}
