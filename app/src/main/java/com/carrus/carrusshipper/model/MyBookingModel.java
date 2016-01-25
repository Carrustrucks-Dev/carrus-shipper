package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 11/7/15 for CarrusShipper.
 */
public class MyBookingModel   implements Serializable {

    @SerializedName("statusCode")
    public String statusCode;


    @SerializedName("message")
    public String message;


    @SerializedName("data")
    public List<MyBookingDataModel> mData = new ArrayList<MyBookingDataModel>();
}
