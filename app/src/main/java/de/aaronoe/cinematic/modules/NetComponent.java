package de.aaronoe.cinematic.modules;

import javax.inject.Singleton;

import dagger.Component;
import de.aaronoe.cinematic.DetailPage.ActorDetails.ActorDetailsActivity;
import de.aaronoe.cinematic.DetailPage.DetailActivity;
import de.aaronoe.cinematic.DetailPage.DetailPageInfoFragment;
import de.aaronoe.cinematic.DetailPage.DetailPageReviewFragment;
import de.aaronoe.cinematic.DetailPage.DetailPageSimilarFragment;
import de.aaronoe.cinematic.DetailPage.DetailPageVideosFragment;
import de.aaronoe.cinematic.ui.MoviesFragment;
import de.aaronoe.cinematic.ui.Search.SearchMoviesActivity;
import de.aaronoe.cinematic.ui.TvSeasonDetailActivity;
import de.aaronoe.cinematic.ui.TvShowDetailActivity;
import de.aaronoe.cinematic.ui.TvShowsFragment;

/**
 *
 * Created by aaron on 26.04.17.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {

    void inject(MoviesFragment fragment);
    void inject(TvShowsFragment fragment);
    void inject(SearchMoviesActivity activity);
    void inject(TvSeasonDetailActivity activity);
    void inject(TvShowDetailActivity activity);


    void inject(ActorDetailsActivity activity);
    void inject(DetailActivity activity);
    void inject(DetailPageInfoFragment activity);
    void inject(DetailPageReviewFragment activity);
    void inject(DetailPageSimilarFragment activity);
    void inject(DetailPageVideosFragment activity);

}
