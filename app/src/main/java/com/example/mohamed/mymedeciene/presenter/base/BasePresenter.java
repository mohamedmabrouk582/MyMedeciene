package com.example.mohamed.mymedeciene.presenter.base;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.mohamed.mymedeciene.view.MainView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :17:26
 */

public class BasePresenter<v extends MainView> implements MainPresnter<v> {
    private v view;

    protected v getView() {
        return view;
    }

    @Override
    public void attachView(v view) {
        this.view = view;
    }

    @Override
    public void showSnakBar(View view, String msg) {
        Snackbar snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}
