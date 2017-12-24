package com.example.mohamed.mymedeciene.presenter.register;

import android.support.v7.widget.RecyclerView;

import com.example.mohamed.mymedeciene.presenter.base.MainPresnter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.view.RegisterView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :23:34
 */

public interface RegisterPresenter<v extends RegisterView>  extends MainPresnter<v> {

    void register(String userName, String email, String password, String location, AddListener listener);
}
