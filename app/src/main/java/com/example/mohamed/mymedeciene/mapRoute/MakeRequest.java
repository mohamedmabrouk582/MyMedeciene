package com.example.mohamed.mymedeciene.mapRoute;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.activity.HomeActivity;
import com.example.mohamed.mymedeciene.activity.MapsActivity;
import com.example.mohamed.mymedeciene.mapRoute.data.Location;
import com.example.mohamed.mymedeciene.mapRoute.data.Route;
import com.example.mohamed.mymedeciene.mapRoute.data.RouteRepons;
import com.example.mohamed.mymedeciene.mapRoute.data.Steps;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 04/01/2018.  time :02:17
 */

public class MakeRequest {
    private String to;
    private Activity activity;
    private ProgressDialog mProgressDialog;
    private BubblesManager bubblesManager;

    public MakeRequest(Activity activity) {
        this.activity=activity;
        mProgressDialog=new ProgressDialog(activity);
        mProgressDialog.setMessage("Map loading .......");
        initializeBubblesManager();

    }

    public void go(String to){
        this.to=to;
       MapRoute mapRoute=new MapRoute();
       List<String> list=new ArrayList<>();
       list.add(HomeActivity.myCurrentLocation);
       list.add(to);
       mapRoute.execute(list);
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


    public class MapRoute extends AsyncTask<List<String>,Void,List<Route>>{
        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
        }

        @Override
        protected List<Route> doInBackground(List<String>[] lists) {
            List<String> fromTo=lists[0];
          String  url = Uri
                    .parse("http://maps.google.com/maps/api/directions/json")
                    .buildUpon()
                    .appendQueryParameter("origin",fromTo.get(0))
                    .appendQueryParameter("destination",fromTo.get(1))
                    .appendQueryParameter("method","GET")
                    .build().buildUpon().toString();


            return  getRoute(url);
        }

        @Override
        protected void onPostExecute(List<Route> routes) {
            MapsActivity.start(activity,to,getLatLangs(routes));
            mProgressDialog.dismiss();
        }

        private List<Route> getRoute(String url){
            List<Route> routes=new ArrayList<>();
            try {
                String json=getUrlString(url);
                Gson gson=new Gson();
                RouteRepons route=gson.fromJson(json,RouteRepons.class);
                routes=route.getRoutes();
                Log.d("asynresp", routes.size() + "");
            }catch (Exception  e){

            }

            return routes;
        }

        public String getUrlString(String urlSepc) throws IOException {
            return new String(getUrlBytes(urlSepc));
        }

        public byte[] getUrlBytes(String urlSpec) throws IOException {
            URL url = new URL(urlSpec);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = connection.getInputStream();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException(connection.getResponseMessage() +
                            ": with " +
                            urlSpec);
                }
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }

                in.close();
                return out.toByteArray();
            } catch (Exception e) {
                return null;

            } finally {
                connection.disconnect();
            }

        }


    }

    @SuppressLint("NewApi")
    public void addNewBubble() {
        final BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(activity).inflate(R.layout.bubble_layout, null);
        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) { }
        });
        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Intent i = activity.getPackageManager()
                        .getLaunchIntentForPackage(
                                activity.getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(i);
                bubbleView.cancelDragAndDrop();

            }
        });
        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView, 60, 20);
    }



    public void initializeBubblesManager() {
        bubblesManager = new BubblesManager.Builder(activity)
                .setTrashLayout(R.layout.bubble_trash)
                .setInitializationCallback(new OnInitializedCallback() {
                    @Override
                    public void onInitialized() {

                    }
                })
                .build();
        bubblesManager.initialize();
    }

}
