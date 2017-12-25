package com.example.mohamed.mymedeciene.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mohamed.mymedeciene.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String ADDRESS = "ADDRESS";
    private GoogleMap mMap;
    private String myAddress;
    double latitude;
    double longitude;
    public static void start(Context context,String address){
        Intent intent=new Intent(context,MapsActivity.class);
        intent.putExtra(ADDRESS,address);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        myAddress=getIntent().getStringExtra(ADDRESS);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.setMyLocationEnabled(true);
        LatLng sydney =reverseGeocoding(this,myAddress);
        try {
            mMap.addMarker(new MarkerOptions().position(sydney).title(myAddress));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }catch (Exception e){
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
        }


    }


    public static LatLng reverseGeocoding(Context context, String locationName){
        if(!Geocoder.isPresent()){
            Log.w("zebia", "Geocoder implementation not present !");
        }
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geoCoder.getFromLocationName(locationName, 5);
            int tentatives = 0;
            while (addresses.size()==0 && (tentatives < 10)) {
                addresses = geoCoder.getFromLocationName("<address goes here>", 1);
                tentatives ++;
            }


            if(addresses.size() > 0){
                Log.d("zebia", "reverse Geocoding : locationName " + locationName + "Latitude " + addresses.get(0).getLatitude() );
                return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            }else{
                //use http api
            }

        } catch (IOException e) {
           // Log.d(Geocoding.class.getName(), "not possible finding LatLng for Address : " + locationName);
        }
        return null;
    }


}
