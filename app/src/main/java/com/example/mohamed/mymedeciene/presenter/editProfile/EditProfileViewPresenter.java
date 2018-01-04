package com.example.mohamed.mymedeciene.presenter.editProfile;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.view.EditProfileView;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
        final String latLang=getLatLang(phLocation);
        map.put("phName",phName);
        map.put("phPhone",phPhone);
        map.put("phLocation",phLocation);
        map.put("latLang",latLang);

        mDatabaseReference.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError==null){
                    listner.onSuccess(latLang);

                }else {
                    listner.onError(databaseError.getMessage());
                }
            }
        });
    }

    private String getLatLang(String location){
        if(!Geocoder.isPresent()){
            Log.w("zebia", "Geocoder implementation not present !");
        }
        Geocoder geoCoder = new Geocoder(activity, Locale.getDefault());

        try {
            List<Address> addresses = geoCoder.getFromLocationName(location, 5);
            int tentatives = 0;
            while (addresses.size()==0 && (tentatives < 10)) {
                addresses = geoCoder.getFromLocationName("<address goes here>", 1);
                tentatives ++;
            }


            if(addresses.size() > 0){
                Log.d("zebia", "reverse Geocoding : locationName " + location + "Latitude " + addresses.get(0).getLatitude() );
                return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()).toString().replace("(","").replace(")","").replace("lat/lng:","");
            }else{
                //use http api
            }

        } catch (IOException e) {
            // Log.d(Geocoding.class.getName(), "not possible finding LatLng for Address : " + locationName);
        }
        return null;
    }
}
