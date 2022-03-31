package com.example.go4lunch.data.repository;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.go4lunch.model.UserModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by JeroSo94 on 29/03/2022.
 */
public class UserRepository {
    private static final String COLLECTION_NAME = "users";
    private static final String LIKE_FIELD = "like";
    private static final String PLACEID_FIELD = "placeId";

    private static volatile UserRepository instance;

    private UserRepository() { }

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
    public static FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    public String getCurrentUserUID(){
        FirebaseUser user = getCurrentUser();
        return (user != null)? user.getUid() : null;
    }

    public Task<Void> signOut(Context context){
        return AuthUI.getInstance().signOut(context);
    }

    /********************
     * FIRESTORE *
     ********************/

    // Get the Collection Reference
    public CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if(user != null){
            String uid = user.getUid();
            String username = user.getDisplayName();
            /* TODO: Réviser le code pour obtentir la photo (pas sûr que ce soit une url). Faut-il plutôt la charger depuis sampledata ?*/
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String placeId = null;
            List<String> like= null;

            UserModel userToCreate = new UserModel(uid, username, urlPicture, placeId, like);

            Task<DocumentSnapshot> userData = getUserData();
            // If the user already exist in Firestore, we get his data (LIKE, PLACE_ID)
            userData.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains(LIKE_FIELD)){
                    userToCreate.setLike((List<String>) documentSnapshot.get(LIKE_FIELD));
                }

                if (documentSnapshot.contains(PLACEID_FIELD)){
                    userToCreate.setPlaceId((String) documentSnapshot.get(PLACEID_FIELD));
                }
                this.getUsersCollection().document(uid).set(userToCreate);
            });
        }
    }

    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData(){
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).get();
        }else{
            return null;
        }
    }

    // Update User like
    public Task<Void> updateLike(String like) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(LIKE_FIELD, LIKE_FIELD + like);
        }else{
            return null;
        }
    }

    // Update User placeId
    public Task<Void> updatePlaceId(String placeId) {
        String uid = this.getCurrentUserUID();
        if(uid != null){
            return this.getUsersCollection().document(uid).update(PLACEID_FIELD, placeId);
        }else{
            return null;
        }
    }
}
