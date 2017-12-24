package com.example.mohamed.mymedeciene.presenter.editProfile;

import android.app.Activity;

import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.view.EditProfileView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :21:56
 */

public class EditProfileViewPresenter<v extends EditProfileView> extends BasePresenter<v> implements EditProfilePresenter<v> {
     private Activity activity;
     private DatabaseReference mDatabaseReference;
     private FirebaseAuth mAuth;
     public EditProfileViewPresenter(Activity activity){
         this.activity=activity;
         mAuth=MyApp.getmAuth();
         mDatabaseReference=  MyApp.getmDatabaseReference().child("Pharmacy").child(mAuth.getUid());
     }


    @Override
    public void save(final String phName, final String phPhone, final String phLocation, final editListner listner) {
        Map map=new HashMap();
        map.put("phName",phName);
        map.put("phPhone",phPhone);
        map.put("phLocation",phLocation);

        mDatabaseReference.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError==null){
                    listner.onSucess();

                }else {
                    listner.onError(databaseError.getMessage());
                }
            }
        });
    }
}
