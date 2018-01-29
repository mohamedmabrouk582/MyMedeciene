package com.example.mohamed.mymedeciene.presenter.splash;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

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
