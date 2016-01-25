package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/7/15 for CarrusShipper.
 */
public class Shipper  implements Serializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("email")
    public String email;

    @SerializedName("firstName")
    public String firstName;

    @SerializedName("lastName")
    public String lastName;

    @SerializedName("phoneNumber")
    public String phoneNumber;

    @SerializedName("rating")
    public Double rating;




}
