package com.example.go4lunch.event;

/**
 * Created by JeroSo94 on 15/04/2022.
 */
public class MapViewEvent {

    public String mSearchViewQuery;
    public double mLatitude;
    public double mLongitude;
    public int mRadius;

    /**
     * Constructor
     * @param searchViewQuery
     * @param latitude
     * @param longitude
     * @param radius
     */
    public MapViewEvent(String searchViewQuery, double latitude, double longitude, int radius) {
        mSearchViewQuery = searchViewQuery;
        mLatitude = latitude;
        mLongitude = longitude;
        mRadius = radius;
    }
}
