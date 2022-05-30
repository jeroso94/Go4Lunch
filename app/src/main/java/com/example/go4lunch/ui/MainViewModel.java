package com.example.go4lunch.ui;

import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.repository.UserRepository;

/**
 * Created by JeroSo94 on 09/05/2022.
 */
public class MainViewModel extends ViewModel {

    private final UserRepository mUserDataSource;

    public MainViewModel(UserRepository userDataSource) {
        mUserDataSource = userDataSource;
    }

    public void createUser(){
        mUserDataSource.createUser();
    }
}
