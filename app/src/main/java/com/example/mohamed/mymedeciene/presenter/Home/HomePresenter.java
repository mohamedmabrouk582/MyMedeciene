package com.example.mohamed.mymedeciene.presenter.Home;

import android.net.Uri;

import com.example.mohamed.mymedeciene.presenter.base.MainPresnter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.QueryListener;
import com.example.mohamed.mymedeciene.view.HomeView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :18:43
 */

@SuppressWarnings("unused")
interface HomePresenter<v extends HomeView> extends MainPresnter<v> {
    void editIMG(Uri uri, QueryListener listener);

    void editProfile(QueryListener listener);

    void addDrug(AddListener listener);

    void logout();
}
