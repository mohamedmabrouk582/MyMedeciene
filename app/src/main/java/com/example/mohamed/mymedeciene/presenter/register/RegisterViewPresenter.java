package com.example.mohamed.mymedeciene.presenter.register;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.view.RegisterView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :23:40
 */

@SuppressWarnings({"StatementWithEmptyBody", "AccessStaticViaInstance"})
public class RegisterViewPresenter<v extends RegisterView> extends BasePresenter<v> implements RegisterPresenter<v> {
    private final Activity activity;
    private final FirebaseAuth mAuth;
    private final DatabaseReference mDatabaseReference;
    private final DataManager dataManager;

    public RegisterViewPresenter(Activity activity) {
        this.activity = activity;
        mAuth = MyApp.getmAuth();
        dataManager = ((MyApp) activity.getApplication()).getData();
        mDatabaseReference = MyApp.getmDatabaseReference().child("Pharmacy");

    }


    @Override
    public void register(final String userName, final String email, String password, final String location, final AddListener listener) {
        mAuth.createUserWithEmailAndPassword(email + "@gmail.com", password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, String> map = new HashMap<>();
                    final String latLang = getLatLang(location);
                    map.put("phName", userName);
                    map.put("phPhone", email);
                    map.put("phLocation", location);
                    map.put("phImgURL", "null");
                    map.put("latLang", latLang);
                    //noinspection ConstantConditions
                    mDatabaseReference.child(mAuth.getCurrentUser().getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dataManager.clear();
                            dataManager.setPharmacy(userName, email, "null", location, latLang);
                            listener.onSuccess("done");
                        }
                    });
                } else {
                    //noinspection ConstantConditions
                    listener.OnError(task.getException().getMessage());
                }
            }
        });
    }

    private String getLatLang(String location) {

        Geocoder geoCoder = new Geocoder(activity, Locale.getDefault());

        try {
            List<Address> addresses = geoCoder.getFromLocationName(location, 5);
            int tentatives = 0;
            while (addresses.size() == 0 && (tentatives < 10)) {
                addresses = geoCoder.getFromLocationName(location, 1);
                tentatives++;
            }


            if (addresses.size() > 0) {
                return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()).toString().replace("(", "").replace(")", "").replace("lat/lng:", "");
            } else {
                //use http api
            }

        } catch (IOException e) {
        }
        return null;
    }
}
