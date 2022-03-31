package com.example.go4lunch.Manager;

import android.content.Context;

import com.example.go4lunch.data.repository.UserRepository;
import com.example.go4lunch.model.UserModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import java.util.Collection;

/**
 * Created by JeroSo94 on 29/03/2022.
 */
public class UserManager {
    private static volatile UserManager instance;
    private static UserRepository userRepository;

    private UserManager() {
        userRepository = UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }

    public CollectionReference getUsersCollection() {return userRepository.getUsersCollection();}

    public FirebaseUser getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() != null);
    }

    public Task<Void> signOut(Context context){
        return userRepository.signOut(context);
    }

    public static void createUser(){
        userRepository.createUser();
    }

    public Task<UserModel> getUserData(){
        // Get the user from Firestore and cast it to a User model Object
        return userRepository.getUserData().continueWith(task -> task.getResult().toObject(UserModel.class)) ;
    }

    public Task<Void> updateLike(String like){
        return userRepository.updateLike(like);
    }

    public Task<Void> updatePlaceId(String placeId){
        return userRepository.updatePlaceId(placeId);
    }
}
