package com.example.mohamed.mymedeciene.presenter.splash;

import android.app.Activity;

import com.example.mohamed.mymedeciene.activity.HomeActivity;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.view.SplashView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :18:07
 */

public class SplashViewPresenter<v extends SplashView> extends BasePresenter<v> implements SplashPrenter<v> {

    private final Activity activity;

    public SplashViewPresenter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void PharmacyActivity(Pharmacy pharmacy) {
        HomeActivity.newIntentPharmacy(activity, pharmacy);
    }

    @Override
    public void HomeActivity() {
        HomeActivity.newIntentUser(activity);
        activity.finish();
    }
}
