package com.example.go4lunch.data.di;

import androidx.annotation.VisibleForTesting;

import com.example.go4lunch.data.apiservice.PlaceService;

import okhttp3.HttpUrl;
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

    public static PlaceService openRequestChannel() {
        return openRequestChannel(HttpUrl.get(BASE_URL));
    }

    @VisibleForTesting
    public static PlaceService openRequestChannel(HttpUrl baseUrl) {
        return new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaceService.class);
    }
}
