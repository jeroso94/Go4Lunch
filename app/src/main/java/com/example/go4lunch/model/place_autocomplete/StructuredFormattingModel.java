package com.example.go4lunch.model.place_autocomplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JeroSo94 on 13/04/2022.
 */
public class StructuredFormattingModel {

    @SerializedName("main_text")
    @Expose
    private String mainText;
    @SerializedName("main_text_matched_substrings")
    @Expose
    private List<MatchedSubstringModel> mMatchedSubstringsList = null;
    @SerializedName("secondary_text")
    @Expose
    private String secondaryText;

}
