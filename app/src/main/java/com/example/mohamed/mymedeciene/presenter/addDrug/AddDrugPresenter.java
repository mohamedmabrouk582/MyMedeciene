package com.example.mohamed.mymedeciene.presenter.addDrug;

import android.net.Uri;
import android.widget.ImageButton;

import com.example.mohamed.mymedeciene.presenter.base.MainPresnter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.QueryListener;
import com.example.mohamed.mymedeciene.view.AddDrugView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 19/12/2017.  time :10:23
 */

public interface AddDrugPresenter<v extends AddDrugView> extends MainPresnter<v> {
    void addDrugIMG(Uri img, ImageButton imageButton);
    void addDrug(String name, String price, String type,String Quantity,AddListener listener);
}
