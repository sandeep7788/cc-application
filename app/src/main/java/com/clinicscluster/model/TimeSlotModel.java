package com.clinicscluster.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSlotModel {

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("option")
    @Expose
    private String option;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
