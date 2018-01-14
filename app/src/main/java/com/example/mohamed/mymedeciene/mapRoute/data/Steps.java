package com.example.mohamed.mymedeciene.mapRoute.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 03/01/2018.  time :23:21
 */

@SuppressWarnings("unused")
public class Steps {
    @SerializedName("start_location")
    private Location start_location;
    @SerializedName("end_location")
    private Location end_location;
    @SerializedName("polyline")
    private OverviewPolyLine polyline;

    public Location getStart_location() {
        return start_location;
    }

    public Location getEnd_location() {
        return end_location;
    }

    public OverviewPolyLine getPolyline() {
        return polyline;
    }
}
