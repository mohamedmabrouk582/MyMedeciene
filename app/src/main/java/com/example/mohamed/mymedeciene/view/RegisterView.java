package com.example.mohamed.mymedeciene.view;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :23:33
 */

@SuppressWarnings("unused")
public interface RegisterView extends MainView {
    void register(String userName, String email, String password, String location);

    void login();

    void showProgress();

    void hideProgress();
}
