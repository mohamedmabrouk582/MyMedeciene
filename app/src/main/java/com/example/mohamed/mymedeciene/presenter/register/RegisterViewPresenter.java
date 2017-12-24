package com.example.mohamed.mymedeciene.presenter.register;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.view.RegisterView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :23:40
 */

public class RegisterViewPresenter<v extends RegisterView> extends BasePresenter<v> implements RegisterPresenter<v> {
     private Activity  activity;
     private FirebaseAuth mAuth;
     private DatabaseReference mDatabaseReference;
     private DataManager dataManager;

     public RegisterViewPresenter(Activity activity){
         this.activity=activity;
         mAuth= MyApp.getmAuth();
         dataManager =((MyApp) activity.getApplication()).getData();
         mDatabaseReference=MyApp.getmDatabaseReference().child("Pharmacy");

     }


    @Override
    public void register(final String userName, final String email, String password, final String location, final AddListener listener) {
     mAuth.createUserWithEmailAndPassword(email+ "@gmail.com",password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
         @Override
         public void onComplete(@NonNull Task<AuthResult> task) {
           if (task.isSuccessful()){
               Map<String,String> map=new HashMap<>();
               map.put("phName",userName);
               map.put("phPhone",email);
               map.put("phLocation",location);
               map.put("phImgURL","null");
               mDatabaseReference.child(mAuth.getCurrentUser().getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       dataManager.clear();
                       dataManager.setPharmacy(userName,email,"null",location);
                       listener.onSuccess("done");
                   }
               });
           }   else {
              listener.OnError(task.getException().getMessage());
           }
          }
     });
    }
}
