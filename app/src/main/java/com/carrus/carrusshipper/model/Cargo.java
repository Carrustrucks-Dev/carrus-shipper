package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/17/15 for CarrusShipper.
 */
public class Cargo implements Serializable {

    @SerializedName("weight")
    public String weight;

    public CargoDetails cargoType;

}
