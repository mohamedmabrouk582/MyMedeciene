package com.example.mohamed.mymedeciene.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.mohamed.mymedeciene.fragment.LoginFragment;
import com.example.mohamed.mymedeciene.utils.SingleFragmentActivity;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :23:16
 */

public class LoginActivity extends SingleFragmentActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);

        context.startActivity(intent);
    }

    @Override
    public Fragment CreateFragment() {
        return LoginFragment.newFragment();
    }
}
