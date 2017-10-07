package de.aaronoe.cinematic.migration

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import de.aaronoe.cinematic.BuildConfig
import de.aaronoe.cinematic.CinematicApp
import de.aaronoe.cinematic.database.MoviesContract
import de.aaronoe.cinematic.model.remote.ApiInterface
import de.aaronoe.cinematic.movies.MovieItem
import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RealmMigrationService : IntentService("RealmMigrationService") {

    @Inject
    lateinit var apiService : ApiInterface

    companion object {
        private val ACTION_MOVIES = "MIGRATE_MOVIES"
        private val ACTION_SHOWS = "MIGRATE_SHOWS"
        val MIGRATION_PREFS = "migration_prefs"
        private val MOVIE_MIGRATION_DONE = "movie_migration"
        private val SHOW_MIGRATION_DONE = "show_migration"

        fun migrateMovies(context: Context) {
            val intent = Intent(context, RealmMigrationService::class.java).apply {
                action = ACTION_MOVIES
                context.startService(this)
            }
        }

        fun migrateShows(context: Context) {
            val intent = Intent(context, RealmMigrationService::class.java).apply {
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
        val cursor = contentResolver.query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);

        var counter = 0
        val cursorSize = cursor.count

        while (cursor.moveToNext()) {
            Log.e("MovieMigration", cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE)))
            val movieId = cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID))
            val call = apiService.getMovieDetailsAsMovieItem(movieId, BuildConfig.MOVIE_DB_API_KEY)
            call.enqueue(object : Callback<MovieItem> {
                override fun onResponse(call: Call<MovieItem>?, response: Response<MovieItem>?) {
                    if (response == null || !response.isSuccessful || response.body() == null) {
                        return
                    }
                    val realm = Realm.getDefaultInstance()
                    realm.executeTransaction {
                        it.copyToRealmOrUpdate(response.body())
                        counter++
                    }
                    realm.close()

                }
                override fun onFailure(call: Call<MovieItem>?, t: Throwable?) {
                    Log.e("Realm Copy: ", "Failed Download")
                }
            })
        }
        cursor.close()

        val realm = Realm.getDefaultInstance()

        realm.executeTransaction {
            val list = it.where(MovieItem::class.java).findAll()
            list.forEach {
                Log.e("RealmResults : ", it.title)
            }
        }

        if (counter == cursorSize) {
            Log.e("RealmResults : ", "Move Migration Done")

            getSharedPreferences(MIGRATION_PREFS, Context.MODE_PRIVATE)
                    .edit().putBoolean(MOVIE_MIGRATION_DONE, true)
                    .apply()
        }
    }

    private fun doShowMigration() {

    }

}