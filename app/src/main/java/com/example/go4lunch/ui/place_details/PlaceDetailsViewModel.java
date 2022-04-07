package com.example.go4lunch.ui.place_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.repository.PlaceRepository;
import com.example.go4lunch.data.repository.UserRepository;
import com.example.go4lunch.model.UserModel;
import com.example.go4lunch.model.place_details_search.PlaceDetailsModel;

/**
 * Created by JeroSo94 on 24/03/2022.
 */
public class PlaceDetailsViewModel extends ViewModel {
    private final PlaceRepository mPlaceDataSource;
    private final UserRepository mUserDataSource;

    public PlaceDetailsViewModel(PlaceRepository placeDataSource, UserRepository userDataSource) {
        mPlaceDataSource = placeDataSource;
        mUserDataSource = userDataSource;
    }

    public LiveData<PlaceDetailsModel> loadPlaceDetails(String placeId){
        return mPlaceDataSource.requestDetailsForPlaceId(placeId);
    }

    public LiveData<UserModel> loadUserDetails(){
        return mUserDataSource.getUserData();
    }

    public void updateUserChoice(String placeId, String placeName, String placeAddress){
        mUserDataSource.updatePlaceDataInCollection(placeId, placeName, placeAddress);
    }

    public void updateLike(String like){
        mUserDataSource.updateLikeInCollection(like);
    }
}
