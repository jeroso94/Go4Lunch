package com.example.go4lunch.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.data.apiservice.NearbySearchService;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;
import com.example.go4lunch.model.nearby_search.NearbyResultModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by JeroSo94 on 02/03/2022.
 */
public class MapRepository {
    public static final String PLACE_TYPE = "restaurant";

    private final NearbySearchService mNearbySearchService;

    public MapRepository(NearbySearchService nearbySearchService) {
        mNearbySearchService = nearbySearchService;
    }

    public LiveData<List<NearbyPlaceModel>> requestNearbyPlaces(double latitude, double longitude, int radius){
        MutableLiveData <List<NearbyPlaceModel>> listOfPlaces = new MutableLiveData<>();
        String latlng = latitude + "," + longitude;

        // let's request the server ('enqueue()' makes the request on another thread)...
        mNearbySearchService.submitNearbySearch(BuildConfig.PLACES_API_KEY, latlng, String.valueOf(radius), PLACE_TYPE)
                .enqueue(new Callback<NearbyResultModel>() {
            @Override
            public void onResponse(@NonNull Call<NearbyResultModel> call, @NonNull Response<NearbyResultModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("OK")){
                        listOfPlaces.setValue(response.body().getListOfProvidedPlaces());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NearbyResultModel> call, @NonNull Throwable t) {
                listOfPlaces.setValue(null);
            }
        });
    return listOfPlaces;
    }
}
