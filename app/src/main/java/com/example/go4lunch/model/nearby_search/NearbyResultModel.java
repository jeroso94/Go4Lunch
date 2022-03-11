package com.example.go4lunch.model.nearby_search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JeroSo94 on 01/03/2022.
 * Setup NearbyPlaceModel Search Result data model composed with NearbyPlaceModel data model
 */
public class NearbyResultModel {
    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;
    @SerializedName("results")
    @Expose
    private List<NearbyPlaceModel> listOfProvidedPlaces = null;
    @SerializedName("status")
    @Expose
    private String status;

    /* Getters and Setters */
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<NearbyPlaceModel> getListOfProvidedPlaces() {
        return listOfProvidedPlaces;
    }

    public void setListOfProvidedPlaces(List<NearbyPlaceModel> listOfProvidedPlaces) {
        this.listOfProvidedPlaces = listOfProvidedPlaces;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
