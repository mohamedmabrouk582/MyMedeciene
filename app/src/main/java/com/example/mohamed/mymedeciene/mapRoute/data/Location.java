package com.example.mohamed.mymedeciene.mapRoute.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 03/01/2018.  time :23:16
 */

@SuppressWarnings("unused")
public class Location {
    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
