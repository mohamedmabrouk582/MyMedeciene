package com.example.mohamed.mymedeciene.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.data.LocationModel;
import com.example.mohamed.mymedeciene.utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class MapsLocationActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {
    public interface MapLisenter{
        void map(LocationModel model);
    }
    private static MapLisenter lisenter;
    private GoogleMap mMap;
    private TextView txtLocation;
    SupportMapFragment mapFragment;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private LocationManager mLocationManager;
    private LocationModel model;
    private LatLng mLatLng;


    public static void start(Context context, LatLng latLng, MapLisenter mapLisenter){
        Intent intent=new Intent(context,MapsLocationActivity.class);
        lisenter=mapLisenter;
        intent.putExtra("latlang",latLng);
        context.startActivity(intent);
    }
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        txtLocation=findViewById(R.id.txt_location);
        mLatLng= (LatLng) getIntent().getExtras().get("latlang");
       // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapss);
        mapFragment.getMapAsync(this);
        configureCameraIdle();
    }

    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(MapsLocationActivity.this);

                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                            model=new LocationModel();
                            model.setLat(latLng.latitude+"");
                            model.setLang(latLng.longitude+"");
                          //  model=new LocationModel(cityName,stateName,country,latLng.latitude+"",latLng.longitude+"");
                             if (addressList.get(0).getCountryName()!=null)
                                 model.setCountryName(addressList.get(0).getCountryName());
                            if (addressList.get(0).getAddressLine(0) != null)
                                model.setAddress(addressList.get(0).getAddressLine(0));
                            if (addressList.get(0).getAdminArea() != null){
                                model.setAdminArea(addressList.get(0).getAdminArea());
                                model.setCityName(addressList.get(0).getAdminArea());
                            }
                            if (addressList.get(0).getFeatureName() != null)
                                model.setFeatureName(addressList.get(0).getFeatureName());
                            if (addressList.get(0).getSubLocality() != null)
                                model.setSubLocality(addressList.get(0).getSubLocality());
                            if (addressList.get(0).getPhone() != null)
                                model.setPhone(addressList.get(0).getPhone());
                            if (addressList.get(0).getPostalCode() != null)
                                model.setPostalCode(addressList.get(0).getPostalCode());
                            if (addressList.get(0).getLocality() != null)
                                model.setLocality(addressList.get(0).getLocality());
                            if (addressList.get(0).getSubAdminArea() != null){
                                model.setSubAdminArea(addressList.get(0).getSubAdminArea());
                                model.setCityState(addressList.get(0).getSubAdminArea());
                            }
                            txtLocation.setText(model.getAddress());
                        // resutText.setText(locality + "  " + country);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }



    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnCameraIdleListener(onCameraIdleListener);
        GPSTracker gps = new GPSTracker(this);
        if (mLatLng==null) {
//            if (gps.canGetLocation()) {
//                setLocation(gps.getLatitude(), gps.getLongitude());
//            } else {
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50, 500, this);
           // }
        }else {
            setLocation(mLatLng.latitude,mLatLng.longitude);
        }
    }

    private void setLocation(double lat,double lang){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lang))      // Sets the center of the map to location user
                .zoom(18)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
    @Override
    public void onLocationChanged(Location location) {
     setLocation(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void confirm(View view) {
        if (model == null) {
            Toast.makeText(this, "Please Select Location", Toast.LENGTH_SHORT).show();
        } else {
            lisenter.map(model);
            finish();
        }
    }
}
