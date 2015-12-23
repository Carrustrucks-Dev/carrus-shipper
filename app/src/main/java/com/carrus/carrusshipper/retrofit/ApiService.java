package com.carrus.carrusshipper.retrofit;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by Sunny on 11/3/15.
 */
public interface ApiService {
    String LOGOUT_URL = "/api/v1/shipper/logout";
    String FORGETPASSWORD_URL = "/api/v1/shipper/forgotPassword";

    String AUTHORIZATION = "authorization";
    @GET("/maps/api/directions/xml")
    public void getDriections(@Query("origin") String origin, @Query("destination") String destination, @Query("sensor") String sensor, @Query("units") String units, @Query("mode") String mode, Callback<String> callback);

    /**
     * @param email
     * @param password
     * @param deviceType
     * @param deviceName
     * @param deviceToken
     */
    @FormUrlEncoded
    @POST("/api/v1/shipper/login")
    public void login(@Field("email") String email, @Field("password") String password, @Field("deviceType") String deviceType, @Field("deviceName") String deviceName, @Field("deviceToken") String deviceToken, Callback<String> callback);

    @GET("/api/v1/shipper/getUpComingApp")
    public void getOnGoing(@Header(AUTHORIZATION) String authorization, @Query("limit") String limit, @Query("skip") String skip, @Query("sort") String sort, Callback<String> callback);

    @GET("/api/v1/shipper/getPast")
    public void getPast(@Header(AUTHORIZATION) String authorization, @Query("limit") String limit, @Query("skip") String skip, @Query("sort") String sort, Callback<String> callback);

    @PUT(LOGOUT_URL)
    public void logout(@Header(AUTHORIZATION) String authorization, Callback<String> callback);

    @FormUrlEncoded
    @PUT(FORGETPASSWORD_URL)
    public void forgotPassword(@Field("email") String body, Callback<String> callback);

    @GET("/api/v1/shipper/getOnGoingBookingTrack")
    public void getAllOnGoingBookingTrack(@Header(AUTHORIZATION) String authorization,  @Query("limit") Integer limit, @Query("skip") Integer skip, @Query("sort") String sort, Callback<String> callback);


    @GET("/api/v1/shipper/getOnGoingBookingTrack")
    public void getSingleOnGoingBookingTrack(@Header(AUTHORIZATION) String authorization,  @Query("bookingId") String bookingId, @Query("limit") Integer limit, @Query("skip") Integer skip, @Query("sort") String sort, Callback<String> callback);


    @FormUrlEncoded
    @PUT("/api/v1/shipper/addRating")
    public void setRating(@Header(AUTHORIZATION) String authorization, @Field("bookingId") String bookingId,@Field("rating") String rating,@Field("comment") String comment, Callback<String> callback);

    @FormUrlEncoded
    @PUT("/api/v1/shipper/cancelBooking/{bookingId}")
    public void cancelBooking(@Header(AUTHORIZATION) String authorization, @Field("bookingStatus") String bookingStatus, @Path("bookingId") String bookingId,Callback<String> callback);

    @Multipart
    @PUT("/api/v1/shipper/uploadProfilePic")
    public void uploadProfilePic(@Header(AUTHORIZATION) String authorization,@Part("image") TypedFile body, Callback<String> callback);
}
