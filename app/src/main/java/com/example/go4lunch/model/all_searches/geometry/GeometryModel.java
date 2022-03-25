package com.example.go4lunch.model.all_searches.geometry;

import com.example.go4lunch.model.all_searches.geometry.location.LocationModel;
import com.example.go4lunch.model.all_searches.geometry.location.ViewportModel;
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
    @SerializedName("viewport")
    @Expose
    private ViewportModel mViewport;

    public GeometryModel() {
        location = new LocationModel();
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public ViewportModel getViewport() {
        return mViewport;
    }

    public void setViewport(ViewportModel viewport) {
        this.mViewport = viewport;
    }
}
