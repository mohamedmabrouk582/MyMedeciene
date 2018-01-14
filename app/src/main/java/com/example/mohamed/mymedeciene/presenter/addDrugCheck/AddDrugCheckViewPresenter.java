package com.example.mohamed.mymedeciene.presenter.addDrugCheck;

import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.utils.CheckListener;
import com.example.mohamed.mymedeciene.view.AddDrugCheckView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 14/01/2018.  time :00:01
 */

public class AddDrugCheckViewPresenter<v extends AddDrugCheckView> extends BasePresenter<v> implements AddDrugCheckPresenter<v> {

    @Override
    public void update(CheckListener listener) {
        listener.onSuccess();
        getView().decline();
    }
}
