package com.example.mohamed.mymedeciene.view;

import com.example.mohamed.mymedeciene.data.Drug;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 23/12/2017.  time :23:41
 */

public interface DrugsView extends MainView {
    void showProgress();
    void hideProgress();
    void DrugClickedMessage(Drug drug);
    void showDrugs();
}
