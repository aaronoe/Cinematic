package de.aaronoe.cinematic.migration

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import de.aaronoe.cinematic.BuildConfig
import de.aaronoe.cinematic.CinematicApp
import de.aaronoe.cinematic.database.MoviesContract
import de.aaronoe.cinematic.model.remote.ApiInterface
import io.realm.Realm
import javax.inject.Inject

class RealmMigrationService : IntentService("RealmMigrationService") {

    @Inject
    lateinit var apiService : ApiInterface

    companion object {
        private val ACTION_MOVIES = "MIGRATE_MOVIES"
        private val ACTION_SHOWS = "MIGRATE_SHOWS"
        val MIGRATION_PREFS = "migration_prefs"
        val MOVIE_MIGRATION_DONE = "movie_migration"
        val SHOW_MIGRATION_DONE = "show_migration"

        fun migrateMovies(context: Context) {
            Intent(context, RealmMigrationService::class.java).apply {
                action = ACTION_MOVIES
                context.startService(this)
            }
        }

        fun migrateShows(context: Context) {
            Intent(context, RealmMigrationService::class.java).apply {
                action = ACTION_SHOWS
                context.startService(this)
            }
        }

    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_MOVIES -> {
                doMovieMigration()
            }
            ACTION_SHOWS -> {
                doShowMigration()
            }
        }
    }

    private fun doMovieMigration() {
        (application as CinematicApp).netComponent.inject(this)
        val cursor = contentResolver.query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null)
        val realm = Realm.getDefaultInstance()

        var counter = 0
        val cursorSize = cursor.count

        while (cursor.moveToNext()) {
            val movieId = cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID))
            val call = apiService.getMovieDetailsAsMovieItem(movieId, BuildConfig.MOVIE_DB_API_KEY)

            val response = call.execute()

            if (response == null || !response.isSuccessful || response.body() == null) {
                return
            }

            realm.executeTransaction {
                it.copyToRealmOrUpdate(response.body())
                Log.e("RealmMigrationService", response.body().toString())
                counter++
            }

        }
        cursor.close()


        Log.e("Realm Status", "Counter: $counter - CursorSize: $cursorSize")

        if (counter == cursorSize) {
            getSharedPreferences(MIGRATION_PREFS, Context.MODE_PRIVATE)
                    .edit().putBoolean(MOVIE_MIGRATION_DONE, true)
                    .apply()
        }
    }

    private fun doShowMigration() {
        (application as CinematicApp).netComponent.inject(this)
        val cursor = contentResolver.query(MoviesContract.ShowEntry.CONTENT_URI, null, null, null, null)

        var counter = 0
        val cursorSize = cursor.count

        val realm = Realm.getDefaultInstance()

        while (cursor.moveToNext()) {
            val showId = cursor.getInt(cursor.getColumnIndex(MoviesContract.ShowEntry.COLUMN_ID))
            val call = apiService.getTvShowDetailsAsTvShow(showId, BuildConfig.MOVIE_DB_API_KEY)

            val response = call.execute()

            if (response == null || !response.isSuccessful || response.body() == null) {
                continue
            }

            realm.executeTransaction {
                it.copyToRealmOrUpdate(response.body())
                Log.e("RealmMigrationService", response.body().toString())
                counter++
            }

        }

        cursor.close()

        Log.e("Realm Status", "Counter: $counter - CursorSize: $cursorSize")

        if (counter == cursorSize) {
            getSharedPreferences(MIGRATION_PREFS, Context.MODE_PRIVATE)
                    .edit().putBoolean(SHOW_MIGRATION_DONE, true)
                    .apply()
        }
    }

}