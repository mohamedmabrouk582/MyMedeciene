package com.example.mohamed.mymedeciene.presenter.editProfile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.mohamed.mymedeciene.R;
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

@SuppressWarnings({"StatementWithEmptyBody", "unchecked"})
public class EditProfileViewPresenter<v extends EditProfileView> extends BasePresenter<v> implements EditProfilePresenter<v> {
    private final Activity activity;
    private final DatabaseReference mDatabaseReference;
    private ProgressDialog mProgressDialog;

    public EditProfileViewPresenter(Activity activity) {
        this.activity = activity;
        FirebaseAuth mAuth = MyApp.getmAuth();
        mProgressDialog=new ProgressDialog(activity);
        mProgressDialog.setMessage(activity.getString(R.string.update_));
        //noinspection ConstantConditions
        mDatabaseReference = MyApp.getmDatabaseReference().child("Pharmacy").child(mAuth.getUid());
    }


    @SuppressWarnings("unchecked")
    @Override
    public void save(final String phName, final String phPhone, final String phLocation, final editListner listner) {
        Map map = new HashMap();
        mProgressDialog.show();
        final String latLang = getLatLang(phLocation);
        //noinspection unchecked
        map.put("phName", phName);
        //noinspection unchecked
        map.put("phPhone", phPhone);
        //noinspection unchecked
        map.put("phLocation", phLocation);
        //noinspection unchecked
        map.put("latLang", latLang);

        //noinspection unchecked
        if (latLang!=null) {
            mDatabaseReference.updateChildren(map, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    mProgressDialog.dismiss();
                    if (databaseError == null) {
                        listner.onSuccess(latLang);

                    } else {
                        listner.onError(databaseError.getMessage());
                    }
                }
            });
        }else {
            mProgressDialog.dismiss();
            listner.onError("your Location not Found");
        }
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
