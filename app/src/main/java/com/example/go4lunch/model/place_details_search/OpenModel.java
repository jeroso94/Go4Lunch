package com.example.go4lunch.model.place_details_search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by JeroSo94 on 24/03/2022.
 */
public class OpenModel {
    @SerializedName("day")
    @Expose
    private Integer day;
    @SerializedName("time")
    @Expose
    private String time;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
