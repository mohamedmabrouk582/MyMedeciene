package com.example.mohamed.mymedeciene.utils;

import android.annotation.SuppressLint;
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

@SuppressWarnings({"unused", "UnnecessaryLocalVariable", "deprecation"})
public class NetworkChangeReceiver extends BroadcastReceiver {
    public static ConnectivityReceiverListener connectivityReceiverListener;

    public NetworkChangeReceiver() {
        super();
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, isonline(intent) + "", Toast.LENGTH_SHORT).show();
        if (connectivityReceiverListener != null) {
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
                final boolean isConnected = isAbleToConnect();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (connectivityReceiverListener != null) {
                            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
                        }
                    }
                });

            }
        }).start();

    }

    private boolean isonline(Intent intent) {
        boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager
                .EXTRA_NO_CONNECTIVITY, false);
        NetworkInfo info1 = intent.getParcelableExtra(ConnectivityManager
                .EXTRA_NETWORK_INFO);
        NetworkInfo info2 = intent.getParcelableExtra(ConnectivityManager
                .EXTRA_OTHER_NETWORK_INFO);
        String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
        boolean failOver = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
        return failOver;
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        //noinspection ConstantConditions
        Toast.makeText(context, activeNetwork.isAvailable() + "", Toast.LENGTH_SHORT).show();

        //noinspection ConstantConditions
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private boolean isAbleToConnect() {
        try {
            URL myUrl = new URL("http://www.google.com");
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(1000);
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
