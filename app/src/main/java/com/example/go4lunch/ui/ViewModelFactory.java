package com.example.go4lunch.ui;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.data.repository.PlaceRepository;
import com.example.go4lunch.ui.map.MapViewModel;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

/**
 * Created by JeroSo94 on 04/03/2022.
 *
 * HomeActivity     -->   MapViewModel   --> PlaceRepository --> NearbySearchService
 *       View       -->     ViewModel    -->    Repository   --> Datasource (here, a Retrofit Api)
 *                              ↑
 *                    Injection starts here,
 *                    in the ViewModel layer
 *
 */

@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory{
    private final PlaceRepository mPlaceDataSource;
    private final Executor mExecutor;

    public ViewModelFactory(PlaceRepository placeDataSource, Executor executor) {
        mPlaceDataSource = placeDataSource;
        mExecutor = executor;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> prototypeClass) {
        if (prototypeClass.isAssignableFrom(MapViewModel.class)) {
            // We inject the Repository in the ViewModel constructor
            return (T) new MapViewModel(mMapDataSource, mExecutor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
