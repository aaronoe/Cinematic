package de.aaronoe.cinematic;

import android.app.Application;

import de.aaronoe.cinematic.auth.AuthManager;
import de.aaronoe.cinematic.modules.AppModule;
import de.aaronoe.cinematic.modules.DaggerNetComponent;
import de.aaronoe.cinematic.modules.NetComponent;
import de.aaronoe.cinematic.modules.NetModule;

/**
 *
 * Created by aaron on 04.04.17.
 */

public class CinematicApp extends Application {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String BASE_URL_V4 = "https://api.themoviedb.org/4/";
    public static final String TMDB_LOGIN_CALLBACK = "tmdb-auth-callback";
    public static final String TMDB_REDIRECT_URI = "cinematic://tmdb-auth-callback";
    public static final String PICTURE_URL_500 = "http://image.tmdb.org/t/p/w500";
    public static final String TMDB_API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    public static final String TMDB_PUBLIC_ACCESS_TOKEN = BuildConfig.TmdbPublicAccessToken;


    private NetComponent mNetComponent;
    private static CinematicApp instance;
    public AuthManager mAuthManager;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(BASE_URL, BASE_URL_V4))
                .build();

        mAuthManager = new AuthManager(this);

        instance = this;
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    public static CinematicApp getInstance() {
        return instance;
    }
}
