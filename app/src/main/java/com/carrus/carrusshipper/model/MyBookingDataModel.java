package com.carrus.carrusshipper.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 11/7/15 for CarrusShipper.
 */
public class MyBookingDataModel implements Serializable {

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

    @SerializedName("quoteNote")
    public String quoteNote;

    @SerializedName("bookingStatus")
    public String bookingStatus;

    public FleetOwner fleetOwner;

    @SerializedName("tracking")
    public String tracking;

    @SerializedName("bookingCreatedAt")
    public String bookingCreatedAt;

    @SerializedName("acceptPrice")
    public String acceptPrice;

    public Rating rating;

    public DocModel doc;

    public Truck truck;

    public Cargo cargo;

    @SerializedName("paymentStatus")
    public String paymentStatus;

    @SerializedName("bookingUpdatedAt")
    public String bookingUpdatedAt;

    public DropOffModel dropOff;

    public PickUpModel pickUp;

    @SerializedName("crn")
    public String crn;

    @SerializedName("truckerNote")
    public String truckerNote;

    @SerializedName("crruentTracking")
    public List<TrackingDetails> crruentTracking = new ArrayList<TrackingDetails>();
}
