package com.example.go4lunch.model.all_search.geometry.location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by JeroSo94 on 08/03/2022.
 */
public class ViewportModel {
    @SerializedName("mNortheast")
    @Expose
    private NortheastModel mNortheast;
    @SerializedName("mSouthwest")
    @Expose
    private SouthwestModel mSouthwest;

    public NortheastModel getNortheast() {
        return mNortheast;
    }

    public void setNortheast(NortheastModel northeast) {
        this.mNortheast = northeast;
    }

    public SouthwestModel getSouthwest() {
        return mSouthwest;
    }

    public void setSouthwest(SouthwestModel southwest) {
        this.mSouthwest = southwest;
    }
}
