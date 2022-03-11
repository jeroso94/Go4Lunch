package com.example.go4lunch.data.di;

import android.content.Context;

import com.example.go4lunch.data.repository.MapRepository;
import com.example.go4lunch.ui.map.MapViewModelFactory;

import java.util.concurrent.Executor;

/**
 * Created by JeroSo94 on 09/03/2022.
 */
public class MapViewModelFactoryModule {

    public static MapViewModelFactory provideMapViewModelFactory(Context context) {
        // Here is our "graph / tree" of injection : MapRepository needs NearbySearchService, and later on, MapViewModel will need MapRepository
        MapRepository mMapDataSource = new MapRepository(
                // We inject the call of NearbySearchService in the Repository constructor
                GoogleRetrofitModule.provideNearbyPlaces()
        );
        Executor mExecutor = ExecutorModule.provideExecutor();

        return new MapViewModelFactory(mMapDataSource, mExecutor);
    }
}

