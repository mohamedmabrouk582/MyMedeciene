package com.example.mohamed.mymedeciene.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.data.AllFullDrug;
import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.data.FullDrug;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.presenter.splash.SplashViewPresenter;
import com.example.mohamed.mymedeciene.view.SplashView;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :18:10
 */

@SuppressWarnings({"AccessStaticViaInstance", "unchecked"})
public class SplashActivity extends AppCompatActivity implements SplashView  {
    private SplashViewPresenter presenter;
    private FirebaseAuth mAuth;
    private DataManager dataManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        presenter = new SplashViewPresenter(this);
        presenter.attachView(this);
        mAuth = MyApp.getmAuth();
        dataManager = ((MyApp) getApplication()).getData();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
       selectedActivity();

    }

    @Override
    public void selectedActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getAllDrugs();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null) {

                    presenter.HomeActivity();

                } else {
                    presenter.PharmacyActivity(dataManager.getPharmacy());
                }

                finish();
            }
        }, 500);
    }

    public void getAllDrugs(){
        DatabaseReference reference=MyApp.getmDatabaseReference().child("Drugs").child("AllDrugs");
                reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        AllFullDrug.getAllFullDrug().clear();
                        final Drug drug=dataSnapshot.getValue(Drug.class);
                        DatabaseReference databaseReference=MyApp.getmDatabaseReference().child("Pharmacy").child(drug.getPhKey());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Pharmacy pharmacy=dataSnapshot.getValue(Pharmacy.class);
                                AllFullDrug.getAllFullDrug().setAllFullDrug(new FullDrug(drug,pharmacy));
                                Log.d("dataSnapshot", "drug is :"+drug.toString() + "\n pharmacy is :"+pharmacy.toString());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }



}
