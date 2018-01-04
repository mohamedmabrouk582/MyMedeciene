package com.example.mohamed.mymedeciene.mapRoute.api;


import com.example.mohamed.mymedeciene.mapRoute.data.RouteRepons;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 03/01/2018.  time :23:24
 */

public interface MapApi {

    @GET("/maps/api/directions/json")
    Observable<RouteRepons> getRouteReponsObservable(@Query("origin") String origin, @Query("destination") String destination);
  Call<RouteRepons> getReponsCall(@Query("origin") String origin, @Query("destination") String destination);
}
