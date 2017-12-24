package com.example.mohamed.mymedeciene.presenter.myDrugs;

import android.app.Activity;
import android.app.FragmentManager;
import android.view.View;

import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.fragment.AddGrugFragment;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.view.DrugsView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 23/12/2017.  time :23:44
 */

public class DrugsViewPresenter<v extends DrugsView> extends BasePresenter<v> implements DrugsPresenter<v> {
    private Activity activity;


    public DrugsViewPresenter(Activity activity){
        this.activity=activity;

    }

    @Override
    public void addNewDrugs() {
        FragmentManager fragmentManager=activity.getFragmentManager();
        AddGrugFragment fragment=AddGrugFragment.newFragment(new AddListener() {
            @Override
            public void onSuccess(String success) {
            }

            @Override
            public void OnError(String error) {

            }
        });
        fragment.show(fragmentManager,"");


    }

    @Override
    public void clickDrug(Drug drug) {

    }
}
