package com.example.mohamed.mymedeciene.presenter.Home;

import android.net.Uri;

import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.presenter.base.MainPresnter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.QueryListener;
import com.example.mohamed.mymedeciene.view.HomeView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :18:43
 */

public interface HomePresenter<v extends HomeView> extends MainPresnter<v>{
    void editIMG(Uri uri,QueryListener listener);
    void editProfile(QueryListener listener);
    void addDrug(AddListener listener);
    void logout();
    void viewDrugs();
}
