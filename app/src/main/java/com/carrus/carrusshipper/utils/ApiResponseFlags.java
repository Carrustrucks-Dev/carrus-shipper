package com.carrus.carrusshipper.utils;

/**
 * Created by Sunny on 11/6/15 for CarrusShipper.
 */
public enum ApiResponseFlags {

    OK(200),
    Created(201),
    Bad_Request(400),
    Unauthorized(401),
    Not_Found(404),
    Not_MORE_RESULT(405),
    Already_Exists(409),
    Internal_Server_Error(500);

    private final int ordinal;

    ApiResponseFlags(int ordinal) {
        this.ordinal = ordinal;
    }

    public int getOrdinal() {
        return ordinal;
    }
}
