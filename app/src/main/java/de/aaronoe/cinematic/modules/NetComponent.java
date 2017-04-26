package de.aaronoe.cinematic.modules;

import javax.inject.Singleton;

import dagger.Component;
import de.aaronoe.cinematic.ui.MoviesFragment;

/**
 *
 * Created by aaron on 26.04.17.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {

    void inject(MoviesFragment fragment);

}
