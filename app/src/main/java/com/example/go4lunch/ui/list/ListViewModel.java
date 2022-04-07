package com.example.go4lunch.ui.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.repository.PlaceRepository;
import com.example.go4lunch.data.repository.UserRepository;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;

import java.util.List;

/**
 * Created by JeroSo94 on 04/03/2022.
 */
public class ListViewModel extends ViewModel {
    private final PlaceRepository mPlaceDataSource;
    private final UserRepository mUserDataSource;

    public ListViewModel(PlaceRepository placeDataSource, UserRepository userDataSource) {
        mPlaceDataSource = placeDataSource;
        mUserDataSource = userDataSource;
    }

    public LiveData<List<NearbyPlaceModel>> loadNearbyPlaces(double latitude, double longitude, int radius){
        return mPlaceDataSource.requestNearbyPlaces(latitude, longitude, radius);
    }

    public LiveData<List<UserModel>> getAllUsers(){
        return mUserDataSource.getAllUsersData();
    }
}
