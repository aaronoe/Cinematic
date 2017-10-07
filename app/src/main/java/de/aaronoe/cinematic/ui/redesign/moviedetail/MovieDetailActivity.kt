package de.aaronoe.cinematic.ui.redesign.moviedetail

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import de.aaronoe.cinematic.CinematicApp
import de.aaronoe.cinematic.R
import de.aaronoe.cinematic.database.Utilities
import de.aaronoe.cinematic.model.Crew.Credits
import de.aaronoe.cinematic.model.Crew.CrewAdapter
import de.aaronoe.cinematic.model.FullMovie.FullMovie
import de.aaronoe.cinematic.movies.MovieItem
import de.aaronoe.cinematic.util.AnimUtils
import de.aaronoe.cinematic.util.Constants
import de.aaronoe.cinematic.util.bindView
import io.realm.Realm
import org.jetbrains.anko.collections.forEachWithIndex
import java.text.NumberFormat

class MovieDetailActivity : AppCompatActivity(), MovieDetailContract.View {

    val metaBackdropIv: ImageView by bindView(R.id.meta_backdrop_iv)
    val metaTitleTv: TextView by bindView(R.id.meta_title_tv)
    val metaStatus: TextView by bindView(R.id.meta_status)
    val metaRuntime: TextView by bindView(R.id.meta_runtime)
    val metaReleaseTv: TextView by bindView(R.id.meta_release_tv)
    val metaBudgetTv: TextView by bindView(R.id.meta_budget_tv)
    val metaRevenue: TextView by bindView(R.id.meta_revenue)
    val metaHomepage: TextView by bindView(R.id.meta_homepage)
    val newMetaContainer: FrameLayout by bindView(R.id.new_meta_container)
    val detailSuggestionRv: RecyclerView by bindView(R.id.detail_suggestion_rv)
    val newSuggestionContainer: LinearLayout by bindView(R.id.new_suggestion_container)
    val contentLoadingPb: ProgressBar by bindView(R.id.content_loading_pb)

    val detailBackdropImageview: ImageView by bindView(R.id.detail_backdrop_imageview)
    val backdropOverlayIv: ImageView by bindView(R.id.backdrop_overlay_iv)
    val detailTitleTextview: TextView by bindView(R.id.detail_title_textview)
    val detailYearTextview: TextView by bindView(R.id.detail_year_textview)
    val detailRuntimeTv: TextView by bindView(R.id.detail_runtime_tv)
    val detailRatingTextview: TextView by bindView(R.id.detail_rating_textview)
    val categoryBubbleOne: TextView by bindView(R.id.category_bubble_one)
    val categoryBubbleTwo: TextView by bindView(R.id.category_bubble_two)
    val categoryBubbleThree: TextView by bindView(R.id.category_bubble_three)
    val detailOverviewTextview: TextView by bindView(R.id.detail_overview_textview)
    val plotContainer: LinearLayout by bindView(R.id.plot_container)
    val castRecyclerView: RecyclerView by bindView(R.id.cast_recycler_view)
    val newCastSectionContainer: LinearLayout by bindView(R.id.new_cast_section_container)
    val toolbar : Toolbar by bindView(R.id.app_bar)

    lateinit var enterMovie : MovieItem
    lateinit var presenter : MovieDetailPresenterImpl
    lateinit var realm : Realm
    var showTransition = false
    var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        ButterKnife.bind(this)

        realm = Realm.getDefaultInstance()

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
        }

        if (intent.hasExtra(getString(R.string.intent_transition_enter_mode))) {
            showTransition = (intent.getIntExtra(getString(R.string.intent_transition_enter_mode), Constants.BACKDROP_ENTER) != Constants.NONE)
        }

        if (intent.hasExtra(getString(R.string.INTENT_KEY_MOVIE))) {
            enterMovie = intent.getParcelableExtra(getString(R.string.INTENT_KEY_MOVIE))
            presenter = MovieDetailPresenterImpl(enterMovie.id, this)
            initViews()
        }

    }

    private fun initViews() {

        val movies = realm.where(MovieItem::class.java).equalTo("id", enterMovie.id).findAll()
        isFavorite = !movies.isEmpty()
        invalidateOptionsMenu()

        val pictureUrl = CinematicApp.PICTURE_URL_500 + enterMovie.backdropPath

        if (showTransition) {
            supportPostponeEnterTransition()

            Picasso.with(this)
                    .load(pictureUrl)
                    .into(object : Target {
                        override fun onBitmapLoaded(bitmap: Bitmap, loadedFrom: Picasso.LoadedFrom) {
                            Log.e("MovieDetail", bitmap.toString())
                            detailBackdropImageview.setImageBitmap(bitmap)
                            AnimUtils.animShow(backdropOverlayIv, 1000, 0f, 1f)
                            supportStartPostponedEnterTransition()
                            AnimUtils.animShow(toolbar, 1000, 0f, 1f)
                        }

                        override fun onBitmapFailed(drawable: Drawable) {
                            Log.e("MovieDetail", drawable.toString())
                            supportStartPostponedEnterTransition()
                        }

                        override fun onPrepareLoad(drawable: Drawable?) {

                        }
                    })
        } else {

            // For some reason Picasso does not load the without a transition

            Picasso.with(this)
                    .load(pictureUrl)
                    .into(detailBackdropImageview)

            AnimUtils.animShow(backdropOverlayIv, 1000, 0f, 1f)
            AnimUtils.animShow(toolbar, 1000, 0f, 1f)
        }



        detailTitleTextview.text = enterMovie.title
        detailRatingTextview.text = String.format("%.1f", enterMovie.voteAverage)
        detailYearTextview.text = Utilities.convertDateToYear(enterMovie.releaseDate)
        detailOverviewTextview.text = enterMovie.overview

        val genreList = Utilities.extractMovieGenres(enterMovie.genreIds)
        when (genreList.size) {
            0 -> {
                categoryBubbleOne.visibility = View.INVISIBLE
                categoryBubbleTwo.visibility = View.GONE
                categoryBubbleThree.visibility = View.GONE
            }
            1 -> {
                categoryBubbleTwo.visibility = View.GONE
                categoryBubbleThree.visibility = View.GONE
            }
            2 -> {
                categoryBubbleThree.visibility = View.GONE
            }
        }
        genreList?.subList(0, minOf(genreList.size, 3))?.forEachWithIndex { i, s ->
            when (i) {
                0 -> categoryBubbleOne.text = s
                1 -> categoryBubbleTwo.text = s
                2 -> categoryBubbleThree.text = s
            }
        }

        downloadDetails()
    }

    fun downloadDetails() {
        presenter.downloadCast()
        presenter.downloadDetails()
        presenter.downloadSimilar()
    }

    override fun showErrorLoading() {

    }

    override fun showDetails(movie: FullMovie) {
        detailRuntimeTv.text = Utilities.getRuntimeString(this, movie.runtime)
        AnimUtils.animShow(detailRuntimeTv)

        AnimUtils.animShow(newMetaContainer)
        contentLoadingPb.visibility = View.INVISIBLE

        if (movie.backdropPath != enterMovie.backdropPath) {
            val pictureUrl = "http://image.tmdb.org/t/p/w500/" + movie.backdropPath
            Picasso.with(this).load(pictureUrl).into(metaBackdropIv)
        }

        metaHomepage.text = movie.homepage
        metaRuntime.text = Utilities.getRuntimeString(this, movie.runtime)
        metaReleaseTv.text = Utilities.convertDate(movie.releaseDate)
        metaBudgetTv.text = NumberFormat.getCurrencyInstance().format(movie.budget)
        metaRevenue.text = NumberFormat.getCurrencyInstance().format(movie.revenue)
        metaStatus.text = movie.status
        metaTitleTv.text = movie.originalTitle

        if (enterMovie.genreIds == null || enterMovie.genreIds.size == 0) {

            movie.genres?.subList(0, movie.genres.size)?.forEachWithIndex { i, genre ->
                when (i) {
                    0 -> {
                        categoryBubbleOne.apply {
                            text = genre.name
                            AnimUtils.animShow(categoryBubbleOne)
                        }
                    }
                    1 -> {
                        categoryBubbleTwo.apply {
                            text = genre.name
                            AnimUtils.animShow(categoryBubbleTwo)
                        }
                    }
                    2 -> {
                        categoryBubbleThree.apply {
                            text = genre.name
                            AnimUtils.animShow(categoryBubbleThree)
                        }
                    }
                }
            }


        }

    }

    override fun showCast(cast: Credits) {

        if (cast.cast.size == 0) return

        AnimUtils.animShow(newCastSectionContainer)
        contentLoadingPb.visibility = View.INVISIBLE

        val creditsAdapter = CrewAdapter(this)
        creditsAdapter.setCastData(cast.cast?.sortedBy { it.order })
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        castRecyclerView.layoutManager = layoutManager
        castRecyclerView.adapter = creditsAdapter
    }

    override fun showSimilar(movieList: List<MovieItem>) {
        AnimUtils.animShow(newSuggestionContainer)
        contentLoadingPb.visibility = View.INVISIBLE

        val similarMoviesAdapter = SimilarMoviesAdapter(this)
        similarMoviesAdapter.setMovieList(movieList)
        val layoutManager = GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        detailSuggestionRv.layoutManager = layoutManager
        detailSuggestionRv.adapter = similarMoviesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_activity, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.favorite)?.apply {
            isVisible = !isFavorite
            isEnabled = !isFavorite
        }

        menu?.findItem(R.id.unfavorite)?.apply {
            isVisible = isFavorite
            isEnabled = isFavorite
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.favorite -> {
                favoriteMovie()
                return true
            }
            R.id.unfavorite -> {
                unfavoriteMovie()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

    private fun favoriteMovie() {
        realm.executeTransaction {
            it.copyToRealmOrUpdate(enterMovie)
            isFavorite = true
            invalidateOptionsMenu()
        }
    }

    private fun unfavoriteMovie() {
        val movies = realm.where(MovieItem::class.java).equalTo("id", enterMovie.id).findAll()

        realm.executeTransaction {
            movies.forEach {
                it.deleteFromRealm()
            }
        }
        isFavorite = false
        invalidateOptionsMenu()
    }

}
