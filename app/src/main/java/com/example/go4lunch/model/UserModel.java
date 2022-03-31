package com.example.go4lunch.model;

import androidx.annotation.Nullable;

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
    private List<String> like;

    public UserModel(){    }

    public UserModel(String uid, String username, @Nullable String urlPicture, @Nullable String placeId, @Nullable List<String> like) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.placeId = placeId;
        this.like = like;
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

    public String getPlaceId() {
        return placeId;
    }

    public List<String> getLike() {
        return like;
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

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setLike(List<String> like) {
        this.like = like;
    }
}
