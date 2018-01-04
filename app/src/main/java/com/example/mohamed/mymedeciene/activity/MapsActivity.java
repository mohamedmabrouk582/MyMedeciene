package com.example.mohamed.mymedeciene.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.mapRoute.MakeRequest;
import com.example.mohamed.mymedeciene.mapRoute.data.RouteRepons;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,View.OnClickListener{

    private static final String ADDRESS = "ADDRESS";
    private static final String LOCATIONS = "locations";
    private GoogleMap mMap;
    private String myAddress,myLocation;
    private FrameLayout mLayout;
    private DataManager dataManager;
    private ImageView normal,hybrid,terrain,satellite;
    private MarkerOptions fromMarkerOptions,toMarkerOptions;
    public static void start(Context context,String address,String location){
        Intent intent=new Intent(context,MapsActivity.class);
        intent.putExtra(ADDRESS,address);
        intent.putExtra(LOCATIONS,location);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        dataManager=((MyApp) getApplication()).getData();
        mLayout=findViewById(R.id.mylayout);
        myAddress=getIntent().getStringExtra(ADDRESS);
        myLocation=getIntent().getStringExtra(LOCATIONS);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        toMarkerOptions=new MarkerOptions();
        fromMarkerOptions=new MarkerOptions();
       // new  MapParser.MapParserBuilder(dataManager.getPharmacy().getLatLang(),myAddress).build();
        // Add a marker in Sydney and move the camera
        mMap.setMyLocationEnabled(true);
        final MakeRequest makeRequest=new MakeRequest(dataManager.getPharmacy().getLatLang(),myAddress);

        makeRequest.makeRequest(new Observer<RouteRepons>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RouteRepons routeRepons) {
                drawRout(makeRequest.getLatLangs(routeRepons.getRoutes()));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });



    }

    private void drawRout(List<LatLng> latLngs) {
        String[] dest = myAddress.split(",");
        String[] source = dataManager.getPharmacy().getLatLang().split(",");
        LatLng to = new LatLng(Double.parseDouble(dest[0]), Double.parseDouble(dest[1]));
        LatLng from = new LatLng(Double.parseDouble(source[0]), Double.parseDouble(source[1]));
            if (latLngs.size() > 0) {
                PolylineOptions rectLine = new PolylineOptions().width(7).color(
                        Color.RED);
                for (int i = 0; i < latLngs.size(); i++) {
                    rectLine.add(latLngs.get(i));
                }
                mMap.addPolyline(rectLine);
                toMarkerOptions.position(to);
                toMarkerOptions.draggable(true);
                fromMarkerOptions.position(from);
                fromMarkerOptions.draggable(true);
                mMap.addMarker(toMarkerOptions.title(myLocation));
                mMap.addMarker(fromMarkerOptions.title(dataManager.getPharmacy().getPhLocation()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(to, 15f));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        }
    }


    private void init(View view){
        normal=view.findViewById(R.id.normal);
        hybrid=view.findViewById(R.id.hybrid);
        terrain=view.findViewById(R.id.terrain);
        satellite=view.findViewById(R.id.satellite);

        normal.setOnClickListener(this);
        hybrid.setOnClickListener(this);
        terrain.setOnClickListener(this);
        satellite.setOnClickListener(this);
    }


    @SuppressLint("ResourceAsColor")
    public void MapType(View view) {
        final Snackbar snackbar = Snackbar.make(mLayout, "", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView =layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        View snackView = LayoutInflater.from(this).inflate(R.layout.map_type_view, null);
        init(snackView);
        layout.addView(snackView, 0);
        snackbar.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                break;
            case R.id.terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                break;
            case R.id.satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
        }
    }
}
