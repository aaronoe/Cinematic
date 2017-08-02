package de.aaronoe.cinematic.ui.redesign.showdetail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Slide
import android.util.Log
import android.view.View
import android.widget.*
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import de.aaronoe.cinematic.R
import de.aaronoe.cinematic.database.Utilities
import de.aaronoe.cinematic.model.Crew.Credits
import de.aaronoe.cinematic.model.Crew.CrewAdapter
import de.aaronoe.cinematic.model.TvShow.FullShow.TvShowFull
import de.aaronoe.cinematic.model.TvShow.SeasonAdapter
import de.aaronoe.cinematic.model.TvShow.TvShow
import de.aaronoe.cinematic.ui.TvSeasonDetailActivity
import de.aaronoe.cinematic.ui.showdetail.ShowDetailContract
import de.aaronoe.cinematic.ui.showdetail.ShowDetailPresenterImpl
import de.aaronoe.cinematic.ui.showdetail.SimilarShowsAdapter
import de.aaronoe.cinematic.util.AnimUtils
import de.aaronoe.cinematic.util.Constants
import de.aaronoe.cinematic.util.bindView
import org.jetbrains.anko.collections.forEachWithIndex

class ShowDetailActivity : AppCompatActivity(), ShowDetailContract.View {

    val seasonsRecyclerView: RecyclerView by bindView(R.id.seasons_recycler_view)
    val showSeasonsPane: LinearLayout by bindView(R.id.show_seasons_pane)
    val newCastSectionContainer: LinearLayout by bindView(R.id.new_cast_section_container)
    val newMetaContainer: FrameLayout by bindView(R.id.new_meta_container)
    val newSuggestionContainer: LinearLayout by bindView(R.id.new_suggestion_container)
    val contentLoadingPb: ProgressBar by bindView(R.id.content_loading_pb)
    val metaBackdropIv: ImageView by bindView(R.id.meta_backdrop_iv)
    val metaLatestEpisode: TextView by bindView(R.id.meta_latest_episode)
    val backdropOverlayIv: ImageView by bindView(R.id.backdrop_overlay_iv)
    val detailBackdropImageview: ImageView by bindView(R.id.detail_backdrop_imageview)
    val detailTitleTextview: TextView by bindView(R.id.detail_title_textview)
    val detailYearTextview: TextView by bindView(R.id.detail_year_textview)
    val detailRatingTextview: TextView by bindView(R.id.detail_rating_textview)
    val categoryBubbleOne: TextView by bindView(R.id.category_bubble_one)
    val categoryBubbleTwo: TextView by bindView(R.id.category_bubble_two)
    val categoryBubbleThree: TextView by bindView(R.id.category_bubble_three)
    val detailOverviewTextview: TextView by bindView(R.id.detail_overview_textview)
    val castRecyclerView: RecyclerView by bindView(R.id.cast_recycler_view)
    val metaTitleTv: TextView by bindView(R.id.meta_title_tv)
    val metaCategoryTv: TextView by bindView(R.id.meta_category_tv)
    val metaReleaseTv: TextView by bindView(R.id.meta_release_tv)
    val metaDurationTv: TextView by bindView(R.id.meta_duration_tv)
    val metaSeasonsTv: TextView by bindView(R.id.meta_seasons_tv)
    val detailSuggestionRv: RecyclerView by bindView(R.id.detail_suggestion_rv)

    var showTransition : Boolean = true
    lateinit var presenter : ShowDetailPresenterImpl
    lateinit var enterShow : TvShow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.redesign_detail_activity)
        ButterKnife.bind(this)

        presenter = ShowDetailPresenterImpl(this)

        if (intent.hasExtra(getString(R.string.intent_transition_enter_mode))) {
            showTransition = intent.getIntExtra(getString(R.string.intent_transition_enter_mode), Constants.BACKDROP_ENTER) != Constants.NONE
        }

        if (intent.hasExtra(getString(R.string.INTENT_KEY_TV_SHOW_ITEM))) {
            enterShow = intent.getParcelableExtra(getString(R.string.INTENT_KEY_TV_SHOW_ITEM))
            initViews()
        }
    }


    private fun initViews() {

        val pictureUrl = "http://image.tmdb.org/t/p/w500/" + enterShow.backdropPath
        Log.e("Test: ", showTransition.toString())
        if (showTransition) supportPostponeEnterTransition()
        Picasso.with(this)
                .load(pictureUrl)
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap, loadedFrom: Picasso.LoadedFrom) {
                        detailBackdropImageview.setImageBitmap(bitmap)
                        AnimUtils.animShow(backdropOverlayIv, 1000, 0f, 1f)
                        if (showTransition) supportStartPostponedEnterTransition()
                    }

                    override fun onBitmapFailed(drawable: Drawable) {
                        if (showTransition) supportStartPostponedEnterTransition()
                    }

                    override fun onPrepareLoad(drawable: Drawable?) {

                    }
                })

        detailTitleTextview.text = enterShow.name
        detailRatingTextview.text = String.format("%.1f", enterShow.voteAverage)
        detailYearTextview.text = Utilities.convertDateToYear(enterShow.firstAirDate)
        detailOverviewTextview.text = enterShow.overview

        val genreList = Utilities.extractGenreList(enterShow.genreIds)
        when (genreList.size) {
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

        downloadShowDetails()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupWindowAnimations() {
        val slide = Slide()
        slide.duration = 1000
        window.enterTransition = slide
    }

    fun downloadShowDetails() {
        presenter.downloadCast(enterShow.id)
        presenter.downloadInfo(enterShow.id)
        presenter.downloadSimilar(enterShow.id)
    }

    override fun showInfo(showFull: TvShowFull?) {
        AnimUtils.animShow(newMetaContainer)
        contentLoadingPb.visibility = View.INVISIBLE

        metaTitleTv.text = showFull?.name
        metaReleaseTv.text = Utilities.convertDate(showFull?.firstAirDate)

        if (showFull?.backdropPath != enterShow.backdropPath) {
            val pictureUrl = "http://image.tmdb.org/t/p/w500/" + showFull?.backdropPath
            Picasso.with(this).load(pictureUrl).into(metaBackdropIv)
        }

        val genreList = showFull?.genres
        var genreString = ""
        if (genreList != null) {
            for (i in genreList.indices) {
                if (i != 0) {
                    genreString += ", "
                }
                genreString += genreList[i].name
            }
            metaCategoryTv.text = genreString
        }

        val runtime = showFull?.episodeRunTime
        var runtimeString = ""
        if (runtime != null) {
            for (i in runtime.indices) {
                if (i != 0) {
                    runtimeString += ", "
                }
                runtimeString += runtime[i]
            }
            runtimeString += " " + getString(R.string.runtime_minutes)
        }
        metaDurationTv.text = runtimeString

        metaSeasonsTv.text = "${showFull?.numberOfSeasons} Seasons and ${showFull?.numberOfEpisodes} Episodes"

        val diff = Utilities.computeDifferenceInDays(showFull?.lastAirDate).toInt()
        var lastRuntime: String = Utilities.convertDate(showFull?.lastAirDate)

        if (diff <= 0) {
            val daysDifference: String
            if (diff < 0) {
                daysDifference = resources
                        .getQuantityString(R.plurals.x_days_ago_plurals, Math.abs(diff), Math.abs(diff))
            } else {
                daysDifference = getString(R.string.today_show)
            }
            lastRuntime += " " + daysDifference
        }
        metaLatestEpisode.text = lastRuntime

        val seasonList = showFull?.seasons

        AnimUtils.animShow(showSeasonsPane)
        // new season pane
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        seasonsRecyclerView.layoutManager = linearLayoutManager
        seasonsRecyclerView.isNestedScrollingEnabled = false
        val seasonAdapter = SeasonAdapter(this, { season, view ->
            val intent = Intent(this, TvSeasonDetailActivity::class.java)
            intent.putExtra(getString(R.string.intent_key_tvshow), showFull)
            intent.putExtra(getString(R.string.intent_key_selected_season), season)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, getString(R.string.TRANSITION_KEY_TV_SEASON))
                startActivity(intent, options.toBundle())
            } else {
                startActivity(intent)
            }
        })
        seasonsRecyclerView.adapter = seasonAdapter
        seasonAdapter.setSeasonList(seasonList)
    }

    override fun showCast(credits: Credits?) {
        AnimUtils.animShow(newCastSectionContainer)
        contentLoadingPb.visibility = View.INVISIBLE

        val creditsAdapter = CrewAdapter(this)
        creditsAdapter.setCastData(credits?.cast?.sortedBy { it.order })
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        castRecyclerView.layoutManager = layoutManager
        castRecyclerView.adapter = creditsAdapter
    }

    override fun showSimilar(similarList: MutableList<TvShow>?) {
        AnimUtils.animShow(newSuggestionContainer)
        contentLoadingPb.visibility = View.INVISIBLE

        val similarMoviesAdapter = SimilarShowsAdapter(this)
        similarMoviesAdapter.setShowData(similarList)
        val layoutManager = GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        detailSuggestionRv.layoutManager = layoutManager
        detailSuggestionRv.adapter = similarMoviesAdapter
    }

    override fun showErrorInfo() {
    }

    override fun showErrorCast() {
    }

    override fun showErrorSimilar() {
    }

}
