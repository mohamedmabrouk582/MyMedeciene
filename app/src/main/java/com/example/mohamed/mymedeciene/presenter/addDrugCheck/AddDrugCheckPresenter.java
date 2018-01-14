package com.example.mohamed.mymedeciene.presenter.addDrugCheck;

import com.example.mohamed.mymedeciene.presenter.base.MainPresnter;
import com.example.mohamed.mymedeciene.utils.CheckListener;
import com.example.mohamed.mymedeciene.view.AddDrugCheckView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 13/01/2018.  time :23:58
 */

@SuppressWarnings("unused")
interface AddDrugCheckPresenter<v extends AddDrugCheckView> extends MainPresnter<v> {
    void update(CheckListener listener);
}
