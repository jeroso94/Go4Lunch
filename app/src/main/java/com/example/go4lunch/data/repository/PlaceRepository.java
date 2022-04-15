package com.example.go4lunch.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.data.apiservice.PlaceService;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;
import com.example.go4lunch.model.nearby_search.NearbyResultModel;
import com.example.go4lunch.model.place_autocomplete.PlaceAutocompleteResultModel;
import com.example.go4lunch.model.place_autocomplete.PlacePredictionModel;
import com.example.go4lunch.model.place_details_search.PlaceDetailsModel;
import com.example.go4lunch.model.place_details_search.PlaceDetailsResultModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by JeroSo94 on 02/03/2022.
 */
public class PlaceRepository {
    public static final String PLACE_TYPE = "restaurant";
    public static final String PLACE_AUTOCOMPLETE_TYPE = "establishment";

    private final PlaceService mPlaceService;

    public PlaceRepository(PlaceService placeService) {
        mPlaceService = placeService;
    }

    public LiveData<List<NearbyPlaceModel>> readNearbyPlaces(double latitude, double longitude, int radius){
        MutableLiveData <List<NearbyPlaceModel>> listOfPlaces = new MutableLiveData<>();
        String latlng = latitude + "," + longitude;

        // let's request the server ('enqueue()' makes the request on another thread)...
        mPlaceService.requestNearbySearch(BuildConfig.PLACES_API_KEY, latlng, String.valueOf(radius), PLACE_TYPE)
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

    public LiveData<PlaceDetailsModel> readDetailsForPlaceId(String placeId){
        MutableLiveData <PlaceDetailsModel> onePlace = new MutableLiveData<>();

        mPlaceService.requestPlaceDetailsSearch(BuildConfig.PLACES_API_KEY, placeId)
                .enqueue(new Callback<PlaceDetailsResultModel>() {
                    @Override
                    public void onResponse(@NonNull Call<PlaceDetailsResultModel> call, @NonNull Response<PlaceDetailsResultModel> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("OK")){
                                onePlace.setValue(response.body().getPlaceDetails());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PlaceDetailsResultModel> call, @NonNull Throwable t) {
                        onePlace.setValue(null);
                    }
        });
    return onePlace;
    }

    public LiveData<List<PlacePredictionModel>> readPlacesPrediction(String input, double latitude, double longitude, int radius){
        MutableLiveData <List<PlacePredictionModel>> listOfPlacesPrediction = new MutableLiveData<>();
        String latlng = latitude + "," + longitude;

        mPlaceService.requestPlacesPrediction(BuildConfig.PLACES_API_KEY, latlng, input, String.valueOf(radius), PLACE_AUTOCOMPLETE_TYPE)
                .enqueue(new Callback<PlaceAutocompleteResultModel>() {
                    @Override
                    public void onResponse(@NonNull Call<PlaceAutocompleteResultModel> call, @NonNull Response<PlaceAutocompleteResultModel> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("OK")){
                                listOfPlacesPrediction.setValue(response.body().getPlacesPredictionList());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PlaceAutocompleteResultModel> call, @NonNull Throwable t) {
                        listOfPlacesPrediction.setValue(null);
                    }
                });
        return listOfPlacesPrediction;
    }
}
