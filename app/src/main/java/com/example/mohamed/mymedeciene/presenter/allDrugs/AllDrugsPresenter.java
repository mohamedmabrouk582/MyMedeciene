package com.example.mohamed.mymedeciene.presenter.allDrugs;

import com.example.mohamed.mymedeciene.presenter.base.MainPresnter;
import com.example.mohamed.mymedeciene.view.AllDrugsView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 24/12/2017.  time :22:21
 */

@SuppressWarnings("unused")
interface AllDrugsPresenter<v extends AllDrugsView> extends MainPresnter<v> {
    void call(String phone);
}
