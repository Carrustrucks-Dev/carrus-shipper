package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/17/15.
 */
public class CoordinatesPickUpModel implements Serializable{

    @SerializedName("pickUpLat")
    public Double pickUpLat;

    @SerializedName("pickUpLong")
    public Double pickUpLong;

}
