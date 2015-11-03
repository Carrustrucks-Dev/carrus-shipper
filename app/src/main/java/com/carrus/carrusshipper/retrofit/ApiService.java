package com.carrus.carrusshipper.retrofit;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Sunny on 11/3/15.
 */
public interface ApiService {

    @GET("/maps/api/directions/xml")
    public void getDriections(@Query("origin") String origin,@Query("destination") String destination,@Query("sensor") String sensor,@Query("units") String units,@Query("mode") String mode, Callback<String> callback);

}
