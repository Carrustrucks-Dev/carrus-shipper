package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 1/15/16 for Fleet Owner for CarrusShipper.
 */
public class StateCityInfo implements Serializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("state")
    public String state;

}
