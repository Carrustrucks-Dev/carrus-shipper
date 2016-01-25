package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/19/15 for CarrusShipper.
 */
public class FleetOwner implements Serializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("fullName")
    public String fullName;


    @SerializedName("phoneNumber")
    public String phoneNumber;

    @SerializedName("email")
    public String email;

}
