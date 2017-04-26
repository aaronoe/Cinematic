package de.aaronoe.cinematic;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import de.aaronoe.cinematic.modules.AppModule;
import de.aaronoe.cinematic.modules.DaggerNetComponent;
import de.aaronoe.cinematic.modules.NetComponent;
import de.aaronoe.cinematic.modules.NetModule;

/**
 *
 * Created by aaron on 04.04.17.
 */

public class PopularMoviesApplication extends Application {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(BASE_URL))
                .build();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }

}
