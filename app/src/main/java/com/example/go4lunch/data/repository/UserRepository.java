package com.example.go4lunch.data.repository;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.UserModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JeroSo94 on 29/03/2022.
 */
public class UserRepository {
    private static final String COLLECTION_NAME = "users";
    private static final String LIKE_FIELD = "likesList";
    private static final String PLACEID_FIELD = "placeId";
    private static final String PLACENAME_FIELD = "placeName";
    private static final String PLACEADDRESS_FIELD = "placeAddress";

    private static volatile UserRepository instance;

    public UserRepository() { }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }

    @Nullable
    public static FirebaseUser readCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    public String readCurrentUserUID(){
        FirebaseUser user = readCurrentUser();
        return (user != null)? user.getUid() : null;
    }

    public Task<Void> signOut(Context context){
        return AuthUI.getInstance().signOut(context);
    }

    /********************
     * FIRESTORE *
     ********************/

    // Get the Collection Reference
    public CollectionReference readUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = readCurrentUser();
        if(user == null){
            String uid = user.getUid();
            String username = user.getDisplayName();
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;

            UserModel userToCreate = new UserModel(uid, username, urlPicture, null, null, null, new ArrayList<>());
            this.readUsersCollection().document(uid).set(userToCreate);
        }
    }

    // Get User Data from Firestore
    public LiveData<UserModel> readUserData(){
        String uid = this.readCurrentUserUID();
        MutableLiveData<UserModel> mutableUser = new MutableLiveData<>();
        if(uid != null){
            this.readUsersCollection().document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        UserModel user = task.getResult().toObject(UserModel.class);
                        mutableUser.setValue(user);
                    } else {
                        Log.d(TAG, "Error getting document fields or sub-collection: ", task.getException());
                    }
                }
            });
            return mutableUser;
        }else{
            return null;
        }
    }

    // Get All Users Data from Firestore
    public LiveData<List<UserModel>> readAllUsersData(){
        MutableLiveData <List<UserModel>> mutableUsersList = new MutableLiveData<>();
        List<UserModel> usersList = new ArrayList<>();
        this.readUsersCollection().get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                usersList.add(document.toObject(UserModel.class));
                            }
                            mutableUsersList.setValue(usersList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return mutableUsersList;
    }

    // Update User likes List
    public void updateLikeInCollection(String like) {
        String uid = this.readCurrentUserUID();
        List<String> newLikesList = new ArrayList<>();
        if (uid != null) {
            this.readUsersCollection().document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        UserModel user = task.getResult().toObject(UserModel.class);
                        assert user != null;
                        newLikesList.addAll(user.getLikesList());
                        if (!newLikesList.contains(like)){
                            newLikesList.add(like);
                            readUsersCollection().document(uid).update(LIKE_FIELD, newLikesList);
                        }
                    } else {
                        Log.d(TAG, "Error getting document fields or sub-collection: ", task.getException());
                    }
                }
            });
        }
    }

    // Update User's placeData
    public void updatePlaceDataInCollection(String placeId, String placeName, String placeAddress) {
        String uid = this.readCurrentUserUID();
        if(uid != null) {
            this.readUsersCollection().document(uid).update(PLACEID_FIELD, placeId,
                    PLACENAME_FIELD, placeName,
                    PLACEADDRESS_FIELD, placeAddress);
        }
    }
}
