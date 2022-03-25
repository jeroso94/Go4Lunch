package com.example.go4lunch.ui.place_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.repository.PlaceRepository;
import com.example.go4lunch.model.place_details_search.PlaceDetailsModel;

/**
 * Created by JeroSo94 on 24/03/2022.
 */
public class PlaceDetailsViewModel extends ViewModel {
    private final PlaceRepository mPlaceDataSource;

    public PlaceDetailsViewModel(PlaceRepository placeDataSource) {
        mPlaceDataSource = placeDataSource;
    }

    public LiveData<PlaceDetailsModel> loadPlaceDetails(String placeId){
        return mPlaceDataSource.requestDetailsForPlaceId(placeId);
    }
}
