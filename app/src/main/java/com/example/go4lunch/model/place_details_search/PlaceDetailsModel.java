package com.example.go4lunch.model.place_details_search;

import com.example.go4lunch.model.all_searches.PhotoModel;
import com.example.go4lunch.model.all_searches.geometry.GeometryModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JeroSo94 on 24/03/2022.
 */
public class PlaceDetailsModel {
    @SerializedName("address_components")
    @Expose
    private List<AddressComponentModel> mAddressComponents = null;
    @SerializedName("international_phone_number")
    @Expose
    private String internationalPhoneNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHoursModel mOpeningHours;
    @SerializedName("utc_offset")
    @Expose
    private Integer UtcOffset;
    @SerializedName("vicinity")
    @Expose
    private String vicinity;
    @SerializedName("photos")
    @Expose
    private List<PhotoModel> mPhotos = null;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("geometry")
    @Expose
    private GeometryModel mGeometry;
    @SerializedName("business_status")
    @Expose
    private String businessStatus;

    public PlaceDetailsModel() {
        mGeometry = new GeometryModel();
    }

    public List<AddressComponentModel> getAddressComponents() {
        return mAddressComponents;
    }

    public void setAddressComponents(List<AddressComponentModel> addressComponents) {
        this.mAddressComponents = addressComponents;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHoursModel getOpeningHours() {
        return mOpeningHours;
    }

    public void setOpeningHours(OpeningHoursModel openingHours) {
        this.mOpeningHours = openingHours;
    }

    public Integer getUtcOffset() {
        return UtcOffset;
    }

    public void setUtcOffset(Integer utcOffset) {
        UtcOffset = utcOffset;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public List<PhotoModel> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<PhotoModel> photos) {
        this.mPhotos = photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getWebsite() {
        return website;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public GeometryModel getGeometry() {
        return mGeometry;
    }

    public void setGeometry(GeometryModel geometry) {
        this.mGeometry = geometry;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
