package com.example.mohamed.mymedeciene.presenter.searchDrug;

import com.example.mohamed.mymedeciene.data.FullDrug;
import com.example.mohamed.mymedeciene.presenter.base.MainPresnter;
import com.example.mohamed.mymedeciene.view.SearchDrugView;

import java.util.List;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 24/01/2018.  time :19:12
 */

public interface SearchDrugPresenter<v extends SearchDrugView> extends MainPresnter<v> {
    void searchDrug(List<FullDrug> fullDrugs,String query);
    void clickDrug(FullDrug fullDrug);
    void clear();
}
