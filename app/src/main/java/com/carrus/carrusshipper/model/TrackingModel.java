package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 11/17/15.
 */
public class TrackingModel implements Serializable {

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

    @SerializedName("crruentTracking")
    public List<TrackingDetails> crruentTracking = new ArrayList<TrackingDetails>();

    @SerializedName("bookingCreatedAt")
    public String bookingCreatedAt;

    @SerializedName("acceptPrice")
    public String acceptPrice;

    public Rating rating;

    public Truck truck;

    public Cargo cargo;

    @SerializedName("paymentStatus")
    public String paymentStatus;

    public DropOffModel dropOff;

    public PickUpModel pickUp;


}