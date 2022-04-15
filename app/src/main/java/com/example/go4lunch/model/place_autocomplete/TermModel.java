package com.example.go4lunch.model.place_autocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by JeroSo94 on 13/04/2022.
 */
public class TermModel {

    @SerializedName("offset")
    @Expose
    private int offset;
    @SerializedName("value")
    @Expose
    private String value;

    /* GETTERS */
    public int getOffset() {
        return offset;
    }

    public String getValue() {
        return value;
    }
    /* SETTERS */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
