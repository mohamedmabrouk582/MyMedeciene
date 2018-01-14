package com.example.mohamed.mymedeciene.utils;

import com.example.mohamed.mymedeciene.data.Pharmacy;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 23/12/2017.  time :18:36
 */

public interface QueryListener {
    void onSuccess(Pharmacy pharmacy);

    void onError(String error);
}
