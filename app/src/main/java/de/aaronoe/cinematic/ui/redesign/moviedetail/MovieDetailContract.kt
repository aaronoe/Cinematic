package de.aaronoe.cinematic.ui.redesign.moviedetail

import de.aaronoe.cinematic.model.Crew.Credits
import de.aaronoe.cinematic.model.FullMovie.FullMovie
import de.aaronoe.cinematic.movies.MovieItem

/**
 * Created by private on 8/2/17.
 *
 */
abstract class MovieDetailContract {

    interface View {
        fun showErrorLoading()
        fun showDetails(movie : FullMovie)
        fun showCast(cast : Credits)
        fun showSimilar(movieList : List<MovieItem>)
    }

    interface Presenter {
        fun downloadDetails()
        fun downloadCast()
        fun downloadSimilar()
    }

}