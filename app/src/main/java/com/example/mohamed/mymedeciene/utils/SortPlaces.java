package com.example.mohamed.mymedeciene.utils;

import android.support.annotation.NonNull;

import com.example.mohamed.mymedeciene.data.FullDrug;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 23/01/2018.  time :00:46
 */

public class SortPlaces implements Comparator<FullDrug> {
    LatLng currentLoc;

    public SortPlaces(LatLng current){
        currentLoc = current;
    }
    @Override
    public int compare(FullDrug place1, FullDrug place2) {
        String[] latlangP1 = place1.getPharmacy().getLatLang().split(",");
        String[] latlangP2 = place2.getPharmacy().getLatLang().split(",");
        double lat1 = Double.parseDouble(latlangP1[0]);
        double lon1 = Double.parseDouble(latlangP1[1]);
        double lat2 = Double.parseDouble(latlangP2[0]);
        double lon2 = Double.parseDouble(latlangP2[1]);

        double distanceToPlace1 = distance(currentLoc.latitude, currentLoc.longitude, lat1, lon1);
        double distanceToPlace2 = distance(currentLoc.latitude, currentLoc.longitude, lat2, lon2);
        return (int) (distanceToPlace1 - distanceToPlace2);
    }


    public double distance(double fromLat, double fromLon, double toLat, double toLon) {
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLon - fromLon;
        double angle = 2 * Math.asin( Math.sqrt(
                Math.pow(Math.sin(deltaLat/2), 2) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon/2), 2) ) );
        return radius * angle;
    }
}
