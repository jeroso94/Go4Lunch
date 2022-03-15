package com.example.go4lunch.data.di;

import android.content.Context;

import com.example.go4lunch.data.repository.PlaceRepository;
import com.example.go4lunch.ui.ViewModelFactory;

import java.util.concurrent.Executor;

/**
 * Created by JeroSo94 on 09/03/2022.
 */
public class ViewModelFactoryModule {

    public static ViewModelFactory provideMapViewModelFactory(Context context) {
        // Here is our "graph / tree" of injection : PlaceRepository needs NearbySearchService, and later on, MapViewModel will need PlaceRepository
        PlaceRepository mPlaceDataSource = new PlaceRepository(
                // We inject the call of NearbySearchService in the Repository constructor
                GoogleRetrofitModule.provideNearbyPlaces()
        );
        Executor mExecutor = ExecutorModule.provideExecutor();

        return new ViewModelFactory(mPlaceDataSource, mExecutor);
    }
}

