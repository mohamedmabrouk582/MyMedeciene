package com.example.mohamed.mymedeciene.view;

import com.example.mohamed.mymedeciene.adapter.SearchDrugAdapter;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 24/01/2018.  time :19:09
 */

public interface SearchDrugView extends MainView{
    void showProgress();
    void hideProgress();
    void showDrugs(SearchDrugAdapter adapter);
    void initSwipe();

}
