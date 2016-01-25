package com.carrus.carrusshipper.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 1/15/16 for Fleet Owner for CarrusShipper.
 */
public class PartnerShip implements Serializable {

    @SerializedName("_id")
    @Expose
    private String Id;
    @SerializedName("partnershipName")
    @Expose
    private String partnershipName;
    @SerializedName("status")
    @Expose
    private String status;

    /**
     *
     * @return
     * The Id
     */
    public String getId() {
        return Id;
    }

    /**
     *
     * @param Id
     * The _id
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     *
     * @return
     * The partnershipName
     */
    public String getPartnershipName() {
        return partnershipName;
    }

    /**
     *
     * @param partnershipName
     * The partnershipName
     */
    public void setPartnershipName(String partnershipName) {
        this.partnershipName = partnershipName;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return getPartnershipName();
    }
}
