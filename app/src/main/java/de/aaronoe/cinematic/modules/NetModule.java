package de.aaronoe.cinematic.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.aaronoe.cinematic.model.remote.ApiInterface;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * Created by aaron on 26.04.17.
 */

@Module
public class NetModule {

    private String mBaseUrl;
    private String mBaseUserUrl;

    public NetModule(String baseUrl, String baseUserUrl) {
        mBaseUrl = baseUrl;
        mBaseUserUrl = baseUserUrl;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    ApiInterface provideApiInterface() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiInterface.class);
    }


}
