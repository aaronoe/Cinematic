package de.aaronoe.popularmovies.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import de.aaronoe.popularmovies.Database.MoviesContract.MovieEntry;
import de.aaronoe.popularmovies.Database.MoviesContract.ShowEntry;

/**
 *
 * Created by aaron on 20.02.17.
 */

class MoviesDbHelper extends SQLiteOpenHelper {

    // the name of the database
    private static final String DATABASE_NAME = "fave_movies.db";

    private static final int VERSION = 3;

    MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE =
                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                        MovieEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL);";
        db.execSQL(CREATE_TABLE);

        final String CREATE_SHOW_TABLE =
                "CREATE TABLE " + ShowEntry.TABLE_NAME + " (" +
                        ShowEntry._ID + " INTEGER PRIMARY KEY, " +
                        ShowEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                        ShowEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        ShowEntry.COLUMN_GENRES + " TEXT, " +
                        ShowEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                        ShowEntry.COLUMN_FIRST_AIR_DATE + " TEXT NOT NULL, " +
                        ShowEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL);";
        db.execSQL(CREATE_SHOW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + ShowEntry.TABLE_NAME );
        onCreate(db);
    }
}
