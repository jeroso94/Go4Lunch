package com.example.go4lunch.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JeroSo94 on 29/03/2022.
 */
public class UserModel {
    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    @Nullable
    private String placeId;
    @Nullable
    private String placeName;
    @Nullable
    private String placeAddress;
    @NonNull
    private List<String> likesList= new ArrayList<>();

    public UserModel(){    }

    public UserModel(String uid, String username, @Nullable String urlPicture, @Nullable String placeId, @Nullable String placeName, @Nullable String placeAddress, @NonNull List<String> likesList) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.placeId = placeId;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.likesList = likesList;
    }

    // --- GETTERS ---
    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    @Nullable
    public String getPlaceId() {
        return placeId;
    }

    @Nullable
    public String getPlaceName() {
        return placeName;
    }

    @Nullable
    public String getPlaceAddress() {
        return placeAddress;
    }

    @NonNull
    public List<String> getLikesList() {
        return likesList;
    }

    // --- SETTERS ---

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setPlaceId(@Nullable String placeId) {
        this.placeId = placeId;
    }

    public void setPlaceName(@Nullable String placeName) {
        this.placeName = placeName;
    }

    public void setPlaceAddress(@Nullable String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public void setLikesList(@NonNull List<String> likesList) {
        this.likesList = likesList;
    }
}
