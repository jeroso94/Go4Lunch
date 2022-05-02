package com.example.go4lunch.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.repository.PlaceRepository;
import com.example.go4lunch.model.all_searches.geometry.location.LocationModel;

/**
 * Created by JeroSo94 on 20/04/2022.
 */
public class HomeViewModel extends ViewModel {
    private final PlaceRepository mPlaceDataSource;

    public HomeViewModel(PlaceRepository placeDataSource) {
        mPlaceDataSource = placeDataSource;
    }

    /* SETTERS */
    public void setSearchViewQuery(String query){
        mPlaceDataSource.setSearchViewQuery(query);
    }

    public void setMyLocation(LocationModel myLocation) {
        mPlaceDataSource.setMyLocation(myLocation);
    }
}
