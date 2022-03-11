package com.example.go4lunch.model.nearby_search;

import com.example.go4lunch.model.all_search.geometry.GeometryModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JeroSo94 on 01/03/2022.
 * Setup NearbyPlaceModel data model composed with GeometryModel data model
 */
public class NearbyPlaceModel {
    @SerializedName("business_status")
    @Expose
    private String businessStatus;
    @SerializedName("geometry")
    @Expose
    private GeometryModel mGeometryForPlace;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHoursModel mOpeningHours;
    @SerializedName("photos")
    @Expose
    private List<PhotoModel> mPhotos = null;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("plus_code")
    @Expose
    private PlusCodeModel mPlusCode;
    @SerializedName("price_level")
    @Expose
    private Integer priceLevel;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("scope")
    @Expose
    private String scope;
    @SerializedName("types")
    @Expose
    private List<String> types = null;
    @SerializedName("user_ratings_total")
    @Expose
    private Integer userRatingsTotal;
    @SerializedName("vicinity")
    @Expose
    private String vicinity;
    @SerializedName("permanently_closed")
    @Expose
    private Boolean permanentlyClosed;

    /* Constructor */
    public NearbyPlaceModel() {
        mGeometryForPlace = new GeometryModel();
    }

    /* Getters and Setters */
    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public GeometryModel getGeometryAttributeForPlace() {
        return mGeometryForPlace;
    }

    public void setGeometryAttributeForPlace(GeometryModel geometryForPlace) {
        mGeometryForPlace = geometryForPlace;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public PlusCodeModel getPlusCode() {
        return mPlusCode;
    }

    public void setPlusCode(PlusCodeModel plusCode) {
        this.mPlusCode = plusCode;
    }

    public Integer getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(Integer priceLevel) {
        this.priceLevel = priceLevel;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Integer getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public void setUserRatingsTotal(Integer userRatingsTotal) {
        this.userRatingsTotal = userRatingsTotal;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Boolean getPermanentlyClosed() {
        return permanentlyClosed;
    }

    public void setPermanentlyClosed(Boolean permanentlyClosed) {
        this.permanentlyClosed = permanentlyClosed;
    }
}

