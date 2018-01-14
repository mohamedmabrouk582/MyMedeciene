package com.example.mohamed.mymedeciene.presenter.splash;

import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.view.SplashView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :18:02
 */

@SuppressWarnings("unused")
interface SplashPrenter<v extends SplashView> {
    void PharmacyActivity(Pharmacy pharmacy);

    void HomeActivity();
}
