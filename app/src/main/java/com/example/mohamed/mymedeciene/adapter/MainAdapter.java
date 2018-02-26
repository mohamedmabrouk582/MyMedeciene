package com.example.mohamed.mymedeciene.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mohamed.mymedeciene.fragment.AllDrugsFragment;
import com.example.mohamed.mymedeciene.fragment.DrugsFragment;
import com.example.mohamed.mymedeciene.fragment.PostsFragment;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 30/01/2018.  time :20:47
 */

public class MainAdapter  extends FragmentPagerAdapter{

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return AllDrugsFragment.newFragment();
            case 1:
                return PostsFragment.newFragment();
            case 2:
                return DrugsFragment.newFragment();
        }
        return AllDrugsFragment.newFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 :
                return "All Drug";
            case 1:
                return "Posts";
            case 2:
                return "My Drug";

        }
        return null;
    }
}
