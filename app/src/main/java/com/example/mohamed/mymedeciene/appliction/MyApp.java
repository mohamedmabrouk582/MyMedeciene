package com.example.mohamed.mymedeciene.appliction;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.mohamed.mymedeciene.utils.NetworkChangeReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 23/12/2017.  time :14:53
 */

public class MyApp extends Application {
    private static FirebaseAuth mAuth;
    private static DatabaseReference mDatabaseReference;
    private static StorageReference mStorageReference;
    private static DataManager dataManager;


    @Override
    public void onCreate() {
        super.onCreate();
        dataManager = new DataManager(new MyShared(this));
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static DatabaseReference getmDatabaseReference() {
        return mDatabaseReference;
    }

    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static StorageReference getmStorageReference() {
        return mStorageReference;
    }

    public static DataManager getData() {
        return dataManager;
    }

    public static void setConnectivityListener(NetworkChangeReceiver.ConnectivityReceiverListener listener) {
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }
}
