package com.example.mohamed.mymedeciene.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 06/01/2018.  time :21:42
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    public static ConnectivityReceiverListener connectivityReceiverListener;

    public NetworkChangeReceiver(){
        super();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, isonline(intent)+"", Toast.LENGTH_SHORT).show();
        if (connectivityReceiverListener!=null){
            connectivityReceiverListener.onNetworkConnectionChanged(isonline(intent));
        }
    }



    private void checkConnectivity(final Context context) {
        if (!isOnline(context)) {
            return;
        }

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean isConnected = isAbleToConnect("http://www.google.com", 1000);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (connectivityReceiverListener!=null){
                            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
                        }
                    }
                });

            }
        }).start();

    }

    private boolean isonline(Intent intent){
        boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager
                .EXTRA_NO_CONNECTIVITY, false);
        NetworkInfo info1 = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager
                .EXTRA_NETWORK_INFO);
        NetworkInfo info2 = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager
                .EXTRA_OTHER_NETWORK_INFO);
        String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
        boolean failOver = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
        return failOver;
    }
    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        Toast.makeText(context, activeNetwork.isAvailable()+"", Toast.LENGTH_SHORT).show();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private boolean isAbleToConnect(String url, int timeout) {
        try {
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

}
