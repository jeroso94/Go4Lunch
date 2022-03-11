package com.example.go4lunch.data.di;

import com.example.go4lunch.data.apiservice.NearbySearchService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JeroSo94 on 02/03/2022.
 * Setup Retrofit to connect GoogleAPIs and
 * provide a method sendRequest to link GoogleAPIs connexion with
 * NearbySearchService set of requests (Go4Lunch API)
 */
public class GoogleRetrofitModule {
    private static final String BASE_URL = "https://maps.googleapis.com";
    private static final Retrofit mGoogleAPI = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static NearbySearchService provideNearbyPlaces() {
        return mGoogleAPI.create(NearbySearchService.class);
    }
}
