package com.example.mohamed.mymedeciene.view;

import com.example.mohamed.mymedeciene.data.Drug;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 24/12/2017.  time :22:10
 */

public interface AllDrugsView extends MainView {
    void searchDrugs(String drugName);
    void showProgress();
    void hideProgress();
    void DrugClickedMessage(Drug drug);
    void showDrugs();
}
