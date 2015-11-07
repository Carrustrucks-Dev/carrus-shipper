package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/7/15.
 */
public class PickUpModel  implements Serializable {

    @SerializedName("companyName")
    public String companyName;

    @SerializedName("name")
    public String name;

    @SerializedName("tin")
    public String tin;

    @SerializedName("contactNumber")
    public String contactNumber;

    @SerializedName("address")
    public String address;

    @SerializedName("city")
    public String city;

    @SerializedName("state")
    public String state;

    @SerializedName("zipCode")
    public String zipCode;

    @SerializedName("date")
    public String date;

    public CoordinatesModel coordinates;

    @SerializedName("time")
    public String time;

}
