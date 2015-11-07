package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/7/15.
 */
public class Truck  implements Serializable {

    @SerializedName("truckNumber")
    public Long truckNumber;

    public TruckDetails truckType;

}
