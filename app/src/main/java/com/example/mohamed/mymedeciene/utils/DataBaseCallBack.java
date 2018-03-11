package com.example.mohamed.mymedeciene.utils;

import java.util.List;

import io.reactivex.Observer;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 04/03/2018.  time :23:42
 */

public interface DataBaseCallBack<t> {
    void onResponse(List<t> list);
    void onError(Throwable t);
}
