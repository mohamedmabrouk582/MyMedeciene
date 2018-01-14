package com.example.mohamed.mymedeciene.mapRoute.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 03/01/2018.  time :23:16
 */

@SuppressWarnings("unused")
public class Legs {

    @SerializedName("steps")
    private List<Steps> steps;

    public List<Steps> getSteps() {
        return steps;
    }
}
