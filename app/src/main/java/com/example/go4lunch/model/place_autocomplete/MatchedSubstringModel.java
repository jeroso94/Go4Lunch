package com.example.go4lunch.model.place_autocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by JeroSo94 on 13/04/2022.
 */
public class MatchedSubstringModel {

    @SerializedName("length")
    @Expose
    private int length;
    @SerializedName("offset")
    @Expose
    private int offset;

    /* GETTERS */
    public int getLength() {
        return length;
    }

    public int getOffset() {
        return offset;
    }

    /* SETTERS */
    public void setLength(int length) {
        this.length = length;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
