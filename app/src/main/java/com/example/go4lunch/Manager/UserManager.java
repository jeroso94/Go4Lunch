package com.example.go4lunch.Manager;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.repository.UserRepository;
import com.example.go4lunch.model.UserModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

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

    /*TODO : Transfert de ces méthodes vers WorkmatesViewModel */
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

    public void createUser(){
        userRepository.createUser();
    }

}
