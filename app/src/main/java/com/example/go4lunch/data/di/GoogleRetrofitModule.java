package com.example.go4lunch.data.di;

import com.example.go4lunch.data.apiservice.PlaceService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JeroSo94 on 02/03/2022.
 * Setup Retrofit to connect GoogleAPIs and
 * provide a method sendRequest to link GoogleAPIs connexion with
 * PlaceService set of requests (Go4Lunch API)
 */
public class GoogleRetrofitModule {
    private static final String BASE_URL = "https://maps.googleapis.com";
    private static final Retrofit mGoogleAPI = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static PlaceService openRequestChannel() {
        return mGoogleAPI.create(PlaceService.class);
    }
}
