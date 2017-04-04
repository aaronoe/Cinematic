package de.aaronoe.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 *
 * Created by aaron on 04.04.17.
 */

public class PopularMoviesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();


        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }
}
