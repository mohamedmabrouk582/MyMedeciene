package com.example.mohamed.mymedeciene.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.mohamed.mymedeciene.R;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :22:48
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment CreateFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.Fragment_Container);

        if (fragment == null) {
            fragment = CreateFragment();
            fragmentManager.beginTransaction().add(R.id.Fragment_Container, fragment).commit();
        }

    }
}
