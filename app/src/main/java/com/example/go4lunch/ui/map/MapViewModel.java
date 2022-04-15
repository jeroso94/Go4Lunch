package com.example.go4lunch.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.repository.PlaceRepository;
import com.example.go4lunch.data.repository.UserRepository;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;
import com.example.go4lunch.model.place_autocomplete.PlacePredictionModel;
import com.example.go4lunch.model.place_details_search.PlaceDetailsModel;

import java.util.List;

/**
 * Created by JeroSo94 on 04/03/2022.
 */
public class MapViewModel extends ViewModel {
    private final PlaceRepository mPlaceDataSource;
    private final UserRepository mUserDataSource;

    public MapViewModel(PlaceRepository placeDataSource,UserRepository userDataSource) {
        mPlaceDataSource = placeDataSource;
        mUserDataSource = userDataSource;
    }

    public LiveData<List<NearbyPlaceModel>> loadNearbyPlaces(double latitude, double longitude, int radius){
        return mPlaceDataSource.readNearbyPlaces(latitude, longitude, radius);
    }

    public LiveData<List<UserModel>> getAllUsers(){
        return mUserDataSource.readAllUsersData();
    }

    public LiveData<List<PlacePredictionModel>> loadPlacesPrediction(String input, double latitude, double longitude, int radius){
        return mPlaceDataSource.readPlacesPrediction(input, latitude, longitude, radius);
    }

    public LiveData<PlaceDetailsModel> loadPlaceDetails(String placeId){
        return mPlaceDataSource.readDetailsForPlaceId(placeId);
    }
}
