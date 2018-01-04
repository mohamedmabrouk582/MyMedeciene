package com.example.mohamed.mymedeciene.mapRoute.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 03/01/2018.  time :23:10
 */

public class RouteRepons {
    @SerializedName("routes")
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }
}
