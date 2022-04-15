package com.example.go4lunch.model.place_autocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JeroSo94 on 13/04/2022.
 */
public class PlaceAutocompleteResultModel {

    @SerializedName("predictions")
    @Expose
    private List<PlacePredictionModel> mPlacesPredictionList = null;
    @SerializedName("status")
    @Expose
    private String status;

    /* Getters */
    public List<PlacePredictionModel> getPlacesPredictionList() {
        return mPlacesPredictionList;
    }

    public String getStatus() {
        return status;
    }

    /*Setters */
    public void setPlacesPredictionList(List<PlacePredictionModel> placesPredictionList) {
        this.mPlacesPredictionList = placesPredictionList;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
