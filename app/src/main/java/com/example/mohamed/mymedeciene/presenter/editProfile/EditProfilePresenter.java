package com.example.mohamed.mymedeciene.presenter.editProfile;

import com.example.mohamed.mymedeciene.presenter.base.MainPresnter;
import com.example.mohamed.mymedeciene.view.EditProfileView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :21:49
 */

public interface EditProfilePresenter<v extends EditProfileView> extends MainPresnter<v>{
    interface editListner{
        void onSuccess(String latLang);
        void onError(String error);
    }
    void save(String phName,String phPhone,String phLocation,editListner listner);
}
