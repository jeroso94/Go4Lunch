package com.example.go4lunch.model.all_search.geometry;

import com.example.go4lunch.model.all_search.geometry.location.LocationModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by JeroSo94 on 01/03/2022.
 *  Setup Geometry data model composed with LocationModel data model
 */
public class GeometryModel {

    @SerializedName("location")
    @Expose
    private LocationModel location;

    public GeometryModel() {
        location = new LocationModel();
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }
}
