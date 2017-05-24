package de.aaronoe.cinematic.model;

import retrofit2.Retrofit;

/**
 *
 * Created by aaron on 24.01.17.
 */


public class ApiClient {


    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;

    /*
    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    */
}