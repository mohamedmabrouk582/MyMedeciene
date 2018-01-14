package com.example.mohamed.mymedeciene.presenter.login;

import com.example.mohamed.mymedeciene.presenter.base.MainPresnter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.view.LoginView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :22:57
 */

@SuppressWarnings("unused")
interface LoginPresenter<v extends LoginView> extends MainPresnter<v> {
    void login(String email, String password, AddListener listener);
}
