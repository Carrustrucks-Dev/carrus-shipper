package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 11/17/15.
 */
public class OnGoingShipper implements Serializable {

    @SerializedName("statusCode")
    public String statusCode;


    @SerializedName("message")
    public String message;


    @SerializedName("data")
    public List<TrackingModel> mData = new ArrayList<TrackingModel>();
}
