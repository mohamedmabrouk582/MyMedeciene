package com.example.mohamed.mymedeciene.presenter.base;

import android.view.View;

import com.example.mohamed.mymedeciene.view.MainView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :17:23
 */

@SuppressWarnings("unused")
public interface MainPresnter<v extends MainView> {
    void attachView(v view);

    void showSnakBar(View view, String msg);
}
