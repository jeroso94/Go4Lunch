package com.example.go4lunch.data.apiservice;

import com.example.go4lunch.model.nearby_search.NearbyResultModel;
import com.example.go4lunch.model.place_autocomplete.PlaceAutocompleteResultModel;
import com.example.go4lunch.model.place_details_search.PlaceDetailsResultModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by JeroSo94 on 02/03/2022.
 * Setup HTTP requests to send via GoogleRetrofitModule
 */
public interface PlaceService {
    @GET("/maps/api/place/nearbysearch/json")
    Call<NearbyResultModel> requestNearbySearch(@Query("key") String apiKey,
                                                @Query("location") String latlng,
                                                @Query("radius") String radius,
                                                @Query("type") String type);

    /*
    @GET("/maps/api/place/nearbysearch/json")
    Call<NearbyResultModel> goNextPageNearbyResult(@Query("key") String apiKey,
                                                   @Query("pagetoken") String pageToken);
     */

    @GET("/maps/api/place/details/json")
    Call<PlaceDetailsResultModel> requestPlaceDetailsSearch(@Query("key") String apiKey,
                                                            @Query("place_id") String placeId);

    @GET("/maps/api/place/autocomplete/json?strictbounds=true")
    Call<PlaceAutocompleteResultModel> requestPlacesPrediction(@Query("key") String apiKey,
                                                               @Query("input") String input,
                                                               @Query("location") String latlng,
                                                               @Query("radius") String radius,
                                                               @Query("type") String type);
}
