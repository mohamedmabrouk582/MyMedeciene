package com.example.mohamed.mymedeciene.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.mohamed.mymedeciene.fragment.RegisterFragment;
import com.example.mohamed.mymedeciene.utils.SingleFragmentActivity;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 19/12/2017.  time :00:02
 */

public class RegisterActivity extends SingleFragmentActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public Fragment CreateFragment() {
        return RegisterFragment.newFragment();
    }
}
