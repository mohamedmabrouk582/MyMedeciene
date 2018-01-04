package com.example.mohamed.mymedeciene.mapRoute.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 03/01/2018.  time :23:16
 */

public class Route {
    @SerializedName("overview_polyline")
    private OverviewPolyLine overviewPolyLine;


    @SerializedName("legs")
    private List<Legs> legs;

    public OverviewPolyLine getOverviewPolyLine() {
        return overviewPolyLine;
    }

    public List<Legs> getLegs() {
        return legs;
    }
}
