package com.example.mohamed.mymedeciene.presenter.myDrugs;

import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.presenter.base.MainPresnter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.view.DrugsView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 23/12/2017.  time :23:42
 */

public interface DrugsPresenter<v extends DrugsView> extends MainPresnter<v> {
    void addNewDrugs(AddListener listener);
    void deleteDrug(String drugId , AddListener listener);
    void editDrug(Drug drug,String id,AddListener listener);
}
