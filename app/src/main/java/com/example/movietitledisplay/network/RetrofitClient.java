package com.example.movietitledisplay.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton class to create and return a Retrofit client instance.
 * Used for making network requests to the OMDb API.
 */
public class RetrofitClient {

    // Retrofit instance reused across the app
    private static Retrofit retrofit;

    /**
     * Returns a single shared Retrofit instance.
     * Builds it if not already initialized.
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.omdbapi.com/") // OMDb API base URL
                    .addConverterFactory(GsonConverterFactory.create()) // Parses JSON responses
                    .build();
        }
        return retrofit;
    }
}
