package de.aaronoe.cinematic.ui.redesign.showdetail

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import de.aaronoe.cinematic.R
import de.aaronoe.cinematic.database.Utilities
import de.aaronoe.cinematic.model.Crew.Credits
import de.aaronoe.cinematic.model.TvShow.FullShow.TvShowFull
import de.aaronoe.cinematic.model.TvShow.TvShow
import de.aaronoe.cinematic.ui.showdetail.ShowDetailContract
import de.aaronoe.cinematic.ui.showdetail.ShowDetailPresenterImpl
import de.aaronoe.cinematic.util.AnimUtils
import de.aaronoe.cinematic.util.bindView
import org.jetbrains.anko.collections.forEachWithIndex

class ShowDetailActivity : AppCompatActivity(), ShowDetailContract.View {

    val backdropOverlayIv: ImageView by bindView(R.id.backdrop_overlay_iv)
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val detailBackdropImageview: ImageView by bindView(R.id.detail_backdrop_imageview)
    val runtimeTextview: TextView by bindView(R.id.runtime_textview)
    val detailTitleTextview: TextView by bindView(R.id.detail_title_textview)
    val detailYearTextview: TextView by bindView(R.id.detail_year_textview)
    val detailRatingTextview: TextView by bindView(R.id.detail_rating_textview)
    val categoryBubbleOne: TextView by bindView(R.id.category_bubble_one)
    val categoryBubbleTwo: TextView by bindView(R.id.category_bubble_two)
    val categoryBubbleThree: TextView by bindView(R.id.category_bubble_three)
    val collapsingToolbar: CollapsingToolbarLayout by bindView(R.id.collapsing_toolbar)
    val detailOverviewTextview: TextView by bindView(R.id.detail_overview_textview)
    val castRecyclerView: RecyclerView by bindView(R.id.cast_recycler_view)
    val metaTitleTv: TextView by bindView(R.id.meta_title_tv)
    val metaCategoryTv: TextView by bindView(R.id.meta_category_tv)
    val metaReleaseTv: TextView by bindView(R.id.meta_release_tv)
    val metaDurationTv: TextView by bindView(R.id.meta_duration_tv)
    val metaSeasonsTv: TextView by bindView(R.id.meta_seasons_tv)
    val detailSuggestionRv: RecyclerView by bindView(R.id.detail_suggestion_rv)

    lateinit var presenter : ShowDetailPresenterImpl
    lateinit var enterShow : TvShow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.redesign_detail_activity)
        ButterKnife.bind(this)

        presenter = ShowDetailPresenterImpl(this)

        if (intent.hasExtra(getString(R.string.INTENT_KEY_TV_SHOW_ITEM))) {
            enterShow = intent.getParcelableExtra(getString(R.string.INTENT_KEY_TV_SHOW_ITEM))
            if (supportActionBar != null) {
                supportActionBar!!.title = enterShow.name
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }
            initViews()
        }
    }


    private fun initViews() {

        val pictureUrl = "http://image.tmdb.org/t/p/w500/" + enterShow.backdropPath

        supportPostponeEnterTransition()
        Picasso.with(this)
                .load(pictureUrl)
                .placeholder(R.drawable.poster_show_loading)
                .error(R.drawable.poster_show_not_available)
                .into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap, loadedFrom: Picasso.LoadedFrom) {
                        detailBackdropImageview.setImageBitmap(bitmap)
                        AnimUtils.animShow(backdropOverlayIv, 1000, 0f, 1f)
                        supportStartPostponedEnterTransition()
                    }

                    override fun onBitmapFailed(drawable: Drawable) {
                        supportStartPostponedEnterTransition()
                    }

                    override fun onPrepareLoad(drawable: Drawable) {

                    }
                })

        detailTitleTextview.text = enterShow.name
        detailRatingTextview.text = String.format("%.1f", enterShow.voteAverage)
        detailYearTextview.text = Utilities.convertDateToYear(enterShow.firstAirDate)
        detailOverviewTextview.text = enterShow.overview
        runtimeTextview.text = enterShow.voteCount.toString()

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

    }

    override fun showInfo(showFull: TvShowFull?) {
    }

    override fun showCast(credits: Credits?) {
    }

    override fun showSimilar(similarList: MutableList<TvShow>?) {
    }

    override fun showErrorInfo() {
    }

    override fun showErrorCast() {
    }

    override fun showErrorSimilar() {
    }

}
