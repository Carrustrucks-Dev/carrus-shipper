package com.carrus.carrusshipper.retrofit;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Sunny on 11/3/15.
 */
public interface ApiService {

    @GET("/maps/api/directions/xml")
    public void getDriections(@Query("origin") String origin,@Query("destination") String destination,@Query("sensor") String sensor,@Query("units") String units,@Query("mode") String mode, Callback<String> callback);

    /***
     *
     * @param email
     * @param password
     * @param deviceType
     * @param deviceName
     * @param deviceToken
     */
    @FormUrlEncoded
    @POST("/api/v1/shipper/login")
    public void login(@Field("email") String email, @Field("password") String password, @Field("deviceType") String deviceType, @Field("deviceName") String deviceName, @Field("deviceToken") String deviceToken, Callback<String> callback);

    @GET("/api/v1/shipper/getOnGoing")
    public void getOnGoing(@Header("authorization") String authorization, @Query("limit") String limit, @Query("skip") String skip, @Query("sort") String sort, Callback<String> callback);


}
