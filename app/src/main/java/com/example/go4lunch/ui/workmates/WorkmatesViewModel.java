package com.example.go4lunch.ui.workmates;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.repository.UserRepository;
import com.example.go4lunch.model.UserModel;

import java.util.List;

/**
 * Created by JeroSo94 on 04/04/2022.
 */
public class WorkmatesViewModel extends ViewModel {
    private final UserRepository mUserDataSource;

    public WorkmatesViewModel(UserRepository userDataSource) {
        mUserDataSource = userDataSource;
    }

    public LiveData<List<UserModel>> loadUsers(){
        return mUserDataSource.readAllUsersData();
    }
}
