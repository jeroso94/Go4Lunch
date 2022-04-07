package com.example.go4lunch.ui;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.data.di.GoogleRetrofitModule;
import com.example.go4lunch.data.repository.PlaceRepository;
import com.example.go4lunch.data.repository.UserRepository;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.ui.list.ListViewModel;
import com.example.go4lunch.ui.map.MapViewModel;
import com.example.go4lunch.ui.place_details.PlaceDetailsViewModel;
import com.example.go4lunch.ui.workmates.WorkmatesViewModel;

import java.util.List;

import javax.inject.Singleton;

/**
 * Created by JeroSo94 on 04/03/2022.
 *
 * HomeActivity     -->   MapViewModel   --> PlaceRepository --> PlaceService
 *       View       -->     ViewModel    -->    Repository   --> Datasource (here, a Retrofit Api)
 *                              ↑
 *                    Injection starts here,
 *                    in the ViewModel layer
 *
 */

@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory{

    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();
                }
            }
        }
        return factory;
    }

    // Here is our "graph / tree" of injection : PlaceRepository needs PlaceService, and later on, MapViewModel will need PlaceRepository
    private final PlaceRepository mPlaceDataSource = new PlaceRepository(
            // We inject the call of PlaceService in the Repository constructor
            GoogleRetrofitModule.openRequestChannel()
    );

    private final UserRepository mUserDataSource = UserRepository.getInstance();

    private ViewModelFactory() {
        // Required public constructor (empty)
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> prototypeClass) {
        if (prototypeClass.isAssignableFrom(MapViewModel.class)) {
            // We inject the Repository in the ViewModel constructor
            return (T) new MapViewModel(mPlaceDataSource, mUserDataSource);
        }

        if (prototypeClass.isAssignableFrom(ListViewModel.class)) {
            // We inject the Repository in the ViewModel constructor
            return (T) new ListViewModel(mPlaceDataSource, mUserDataSource);
        }

        if (prototypeClass.isAssignableFrom(PlaceDetailsViewModel.class)) {
            // We inject the Repository in the ViewModel constructor
            return (T) new PlaceDetailsViewModel(mPlaceDataSource, mUserDataSource);
        }

        if (prototypeClass.isAssignableFrom(WorkmatesViewModel.class)){
            // We inject the Repository in the ViewModel constructor
            return (T) new WorkmatesViewModel(mUserDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
