package com.example.go4lunch.ui;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.repository.PlaceRepository;
import com.example.go4lunch.data.repository.UserRepository;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.model.all_searches.geometry.location.LocationModel;
import com.google.android.gms.tasks.Task;

/**
 * Created by JeroSo94 on 20/04/2022.
 */
public class HomeViewModel extends ViewModel {
    private final PlaceRepository mPlaceDataSource;
    private final UserRepository mUserDataSource;

    public HomeViewModel(PlaceRepository placeDataSource, UserRepository userDataSource) {
        mPlaceDataSource = placeDataSource;
        mUserDataSource = userDataSource;
    }

    public LiveData<UserModel> loadUserDetails(){
        return mUserDataSource.readUserData();
    }

    public Task<Void> signOut(Context context){
        return mUserDataSource.signOut(context);
    }

    /* SETTERS */
    public void setSearchViewQuery(String query){
        mPlaceDataSource.setSearchViewQuery(query);
    }

    public void setMyLocation(LocationModel myLocation) {
        mPlaceDataSource.setMyLocation(myLocation);
    }
}
