package com.example.mohamed.mymedeciene.mapRoute;

import android.app.Activity;
import android.util.Log;

import com.example.mohamed.mymedeciene.activity.HomeActivity;
import com.example.mohamed.mymedeciene.activity.MapsActivity;
import com.example.mohamed.mymedeciene.mapRoute.api.ClientApi;
import com.example.mohamed.mymedeciene.mapRoute.api.MapApi;
import com.example.mohamed.mymedeciene.mapRoute.data.Location;
import com.example.mohamed.mymedeciene.mapRoute.data.Route;
import com.example.mohamed.mymedeciene.mapRoute.data.RouteRepons;
import com.example.mohamed.mymedeciene.mapRoute.data.Steps;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 04/01/2018.  time :02:17
 */

public class MakeRequest {
//    private String from ;
//    private String to;
    private MapApi mapApi;
    private Activity activity;

    public MakeRequest(Activity activity) {
//        this.from = from;
//        this.to = to;
        this.activity=activity;
        mapApi= ClientApi.getRetrofit().create(MapApi.class);

    }


//    public  void makeRequest(Observer<RouteRepons> routeReponsObserver){
//        Observable<RouteRepons> routeReponsObservable = mapApi.getRouteReponsObservable(from, to);
//        routeReponsObservable.subscribeOn(Schedulers.io()).
//                observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(routeReponsObserver);
//    }

    public void OpenMap( final String to){
        Observable<RouteRepons> routeReponsObservable = mapApi.getRouteReponsObservable(HomeActivity.myCurrentLocation, to);
        routeReponsObservable.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<RouteRepons>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RouteRepons routeRepons) {
                        MapsActivity.start(activity,to,getLatLangs(routeRepons.getRoutes()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    public List<LatLng> getLatLangs(List<Route> routes){
        List<LatLng> routList=new ArrayList<>();

        if (routes.size()>0){
            ArrayList<LatLng> decodelist;
            Route routeA = routes.get(0);
            if (routeA.getLegs().size()>0){
                List<Steps> steps=routeA.getLegs().get(0).getSteps();
                Steps step;
                Location location;
                String polyline;

                for (int i = 0; i <steps.size() ; i++) {
                    step=steps.get(i);
                    location=step.getStart_location();
                    routList.add(new LatLng(location.getLat(),location.getLng()));
                    polyline=step.getPolyline().getPoints();
                    decodelist = decodePoly(polyline);

                    routList.addAll(decodelist);
                    location=step.getEnd_location();
                    routList.add(new LatLng(location.getLat(),location.getLng()));
                }
            }
        }

        return routList;
    }

    private  ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }
}
