package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/7/15.
 */
public class MyBookingDataModel  implements Serializable{

    @SerializedName("_id")
    public String id;

    @SerializedName("bid")
    public String bid;

    public Shipper shipper;

    @SerializedName("paymentMode")
    public String paymentMode;

    @SerializedName("paymentOn")
    public String paymentOn;

    @SerializedName("jobNote")
    public String jobNote;

    @SerializedName("bookingStatus")
    public String bookingStatus;

    @SerializedName("fleetOwner")
    public String fleetOwner;

    @SerializedName("tracking")
    public String tracking;

    @SerializedName("bookingCreatedAt")
    public String bookingCreatedAt;

    @SerializedName("acceptPrice")
    public String acceptPrice;

    public Rating rating;

    public Truck truck;

    @SerializedName("paymentStatus")
    public String paymentStatus;

    public DropOffModel dropOff;

    public PickUpModel pickUp;


}
