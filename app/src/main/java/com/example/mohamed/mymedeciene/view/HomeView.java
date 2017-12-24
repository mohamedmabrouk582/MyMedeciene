package com.example.mohamed.mymedeciene.view;

import android.support.v4.app.Fragment;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :18:42
 */

public interface HomeView extends MainView {
    void isPharmacy(boolean b);
    void login();
    void register();
    void setFragment(Fragment fragment);
}
