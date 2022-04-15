package com.example.go4lunch.model.place_autocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JeroSo94 on 13/04/2022.
 */
public class PlacePredictionModel {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("matched_substrings")
    @Expose
    private List<MatchedSubstringModel> mMatchedSubstringsList = null;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("structured_formatting")
    @Expose
    private StructuredFormattingModel mStructuredFormatting;
    @SerializedName("terms")
    @Expose
    private List<TermModel> mTermsList = null;
    @SerializedName("types")
    @Expose
    private List<String> typesList = null;


    /* GETTERS */
    public String getDescription() {
        return description;
    }

    public List<MatchedSubstringModel> getMatchedSubstringsList() {
        return mMatchedSubstringsList;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getReference() {
        return reference;
    }

    public StructuredFormattingModel getStructuredFormatting() {
        return mStructuredFormatting;
    }

    public List<TermModel> getTermsList() {
        return mTermsList;
    }

    public List<String> getTypesList() {
        return typesList;
    }

    /* SETTERS */
    public void setDescription(String description) {
        this.description = description;
    }

    public void setMatchedSubstringsList(List<MatchedSubstringModel> matchedSubstringsList) {
        mMatchedSubstringsList = matchedSubstringsList;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setStructuredFormatting(StructuredFormattingModel structuredFormatting) {
        mStructuredFormatting = structuredFormatting;
    }

    public void setTermsList(List<TermModel> termsList) {
        mTermsList = termsList;
    }

    public void setTypesList(List<String> typesList) {
        this.typesList = typesList;
    }
}
