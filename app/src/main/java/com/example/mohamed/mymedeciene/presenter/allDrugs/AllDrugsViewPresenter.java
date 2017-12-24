package com.example.mohamed.mymedeciene.presenter.allDrugs;

import android.app.Activity;

import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.view.AllDrugsView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 24/12/2017.  time :22:22
 */

public class AllDrugsViewPresenter<v extends AllDrugsView> extends BasePresenter<v> implements AllDrugsPresenter<v> {
    private Activity activity;

    public AllDrugsViewPresenter(Activity activity){
        this.activity=activity;
    }
    @Override
    public void clickDrugs(Drug drug) {

    }
}
