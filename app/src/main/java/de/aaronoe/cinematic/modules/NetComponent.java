package de.aaronoe.cinematic.modules;

import javax.inject.Singleton;

import dagger.Component;
import de.aaronoe.cinematic.auth.AuthManager;
import de.aaronoe.cinematic.ui.detailpage.ActorDetails.ActorDetailsActivity;
import de.aaronoe.cinematic.ui.detailpage.DetailActivity;
import de.aaronoe.cinematic.ui.detailpage.DetailPageInfoFragment;
import de.aaronoe.cinematic.ui.detailpage.DetailPageReviewFragment;
import de.aaronoe.cinematic.ui.detailpage.DetailPageSimilarFragment;
import de.aaronoe.cinematic.ui.detailpage.DetailPageVideosFragment;
import de.aaronoe.cinematic.ui.MoviesFragment;
import de.aaronoe.cinematic.ui.login.LoginActivity;
import de.aaronoe.cinematic.ui.search.SearchMoviesActivity;
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

    void inject(AuthManager authManager);
    void inject(LoginActivity loginActivity);

}
