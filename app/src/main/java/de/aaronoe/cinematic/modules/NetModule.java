package de.aaronoe.cinematic.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.aaronoe.cinematic.auth.AuthenticationInterceptor;
import de.aaronoe.cinematic.model.remote.ApiInterface;
import de.aaronoe.cinematic.model.remote.UserApi;
import okhttp3.OkHttpClient;
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
    OkHttpClient provideOkHttpClient(AuthenticationInterceptor authenticationInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authenticationInterceptor)
                .build();
    }

    @Provides
    @Singleton
    AuthenticationInterceptor provideAuthenticationInterceptor() {
        return new AuthenticationInterceptor();
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


    @Provides
    @Singleton
    UserApi provideUserApi(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUserUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(UserApi.class);
    }


}
