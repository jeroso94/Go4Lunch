package com.example.go4lunch.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.repository.PlaceRepository;
import com.example.go4lunch.model.nearby_search.NearbyPlaceModel;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by JeroSo94 on 04/03/2022.
 */
public class MapViewModel extends ViewModel {
    private final PlaceRepository mPlaceDataSource;
    private final Executor mExecutor;

    public MapViewModel(PlaceRepository placeDataSource, Executor executor) {
        mPlaceDataSource = placeDataSource;
        mExecutor = executor;
    }

    public LiveData<List<NearbyPlaceModel>> displayNearbyPlaces(double latitude, double longitude, int radius){
        return mMapDataSource.requestNearbyPlaces(latitude, longitude, radius);
    }
}
