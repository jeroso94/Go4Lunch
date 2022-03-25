package com.example.go4lunch.model.place_details_search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JeroSo94 on 24/03/2022.
 */
public class PlaceDetailsResultModel {
    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("result")
    @Expose
    private PlaceDetailsModel mPlaceDetails;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public PlaceDetailsModel getPlaceDetails() {
        return mPlaceDetails;
    }

    public void setPlaceDetails(PlaceDetailsModel placeDetails) {
        this.mPlaceDetails = placeDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
