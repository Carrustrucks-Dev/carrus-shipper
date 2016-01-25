package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/17/15 for CarrusShipper.
 */
public class CargoDetails implements Serializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("typeCargoName")
    public String typeCargoName;
}
