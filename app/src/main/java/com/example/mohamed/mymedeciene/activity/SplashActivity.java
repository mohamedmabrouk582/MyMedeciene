package com.example.mohamed.mymedeciene.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.presenter.splash.SplashViewPresenter;
import com.example.mohamed.mymedeciene.view.SplashView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :18:10
 */

public class SplashActivity extends AppCompatActivity implements SplashView {
     private SplashViewPresenter presenter;
     private FirebaseAuth mAuth;
     private DataManager dataManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        presenter=new SplashViewPresenter(this);
        presenter.attachView(this);
        mAuth= MyApp.getmAuth();
        dataManager =((MyApp) getApplication()).getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedActivity();
    }

    @Override
    public void selectedActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user=mAuth.getCurrentUser();
                if (user==null){
                    presenter.HomeActivity();
                }else {
                    presenter.PharmacyActivity(dataManager.getPharmacy());
                }
                finish();
            }
        },500);
    }
}
