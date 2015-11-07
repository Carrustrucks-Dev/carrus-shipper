package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/7/15.
 */
public class Rating  implements Serializable {

    @SerializedName("shipperRate")
    public Long shipperRate;

    @SerializedName("truckerRate")
    public Long truckerRate;

    @SerializedName("shipperComment")
    public String shipperComment;

    @SerializedName("truckerComment")
    public String truckerComment;

}