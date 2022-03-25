package com.example.go4lunch.model.place_details_search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JeroSo94 on 24/03/2022.
 */
public class OpeningHoursModel {
    @SerializedName("open_now")
    @Expose
    private Boolean openNow;
    @SerializedName("periods")
    @Expose
    private List<PeriodModel> mPeriods = null;
    @SerializedName("weekday_text")
    @Expose
    private List<String> weekdayText = null;

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public List<PeriodModel> getPeriods() {
        return mPeriods;
    }

    public void setPeriods(List<PeriodModel> periods) {
        this.mPeriods = periods;
    }

    public List<String> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<String> weekdayText) {
        this.weekdayText = weekdayText;
    }
}
