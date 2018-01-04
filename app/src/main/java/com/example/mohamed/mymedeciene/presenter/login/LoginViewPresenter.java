package com.example.mohamed.mymedeciene.presenter.login;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mohamed.mymedeciene.activity.HomeActivity;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.view.LoginView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :22:59
 */

public class LoginViewPresenter<v extends LoginView> extends BasePresenter<v> implements LoginPresenter<v> {
    private Activity activity;
    private DataManager dataManager;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    public LoginViewPresenter(Activity  activity){
        this.activity=activity;
        dataManager=((MyApp) activity.getApplication()).getData();
        mAuth=MyApp.getmAuth();
        mDatabaseReference=MyApp.getmDatabaseReference().child("Pharmacy");
    }


    @Override
    public void login(String email, String password, final AddListener listener) {
      mAuth.signInWithEmailAndPassword(email+"@gmail.com",password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()){
                  mDatabaseReference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(DataSnapshot dataSnapshot) {
                          Pharmacy pharmacy=dataSnapshot.getValue(Pharmacy.class);
                          dataManager.setPharmacy(pharmacy.getPhName(),pharmacy.getPhPhone(),pharmacy.getPhImgURL(),pharmacy.getPhLocation(),pharmacy.getLatLang());
                          HomeActivity.newIntentPharmacy(activity,pharmacy);
                          Log.d("ph", pharmacy.toString() + "");
                          activity.finish();
                      }

                      @Override
                      public void onCancelled(DatabaseError databaseError) {
                       listener.OnError(databaseError.getMessage());
                      }
                  });
              }else {
                  listener.OnError(task.getException().getMessage());
              }
          }
      });
    }
}
