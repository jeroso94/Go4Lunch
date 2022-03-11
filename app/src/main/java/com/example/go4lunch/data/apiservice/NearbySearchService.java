package com.example.go4lunch.data.apiservice;

import com.example.go4lunch.model.nearby_search.NearbyResultModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by JeroSo94 on 02/03/2022.
 * Setup HTTP requests to send via GoogleRetrofitModule
 */
public interface NearbySearchService {
    @GET("/maps/api/place/nearbysearch/json")
    Call<NearbyResultModel> submitNearbySearch(@Query("key") String apiKey,
                                               @Query("location") String latlng,
                                               @Query("radius") String radius,
                                               @Query("type") String type);

    /*
    @GET("/maps/api/place/nearbysearch/json")
    Call<NearbyResultModel> goNextPageNearbyResult(@Query("key") String apiKey,
                                                   @Query("pagetoken") String pageToken);

     */
}
