package com.example.mohamed.mymedeciene.appliction;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.example.mohamed.mymedeciene.data.Pharmacy;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 23/12/2017.  time :14:54
 */

public class MyShared {
    private static final String NAME = "phname";
    private static final String PHONE = "PHPHONE";
    private static final String IMG = "PHIMG";
    private static final String LOCATION ="LOCATION" ;
    private static final String LATLANG ="LATLANG" ;
    private SharedPreferences mSharedPreferences;
private SharedPreferences.Editor editor;
public MyShared(Context context){
    mSharedPreferences=context.getSharedPreferences("pharmacy",Context.MODE_PRIVATE);
    editor=mSharedPreferences.edit();
}

public void putPhName(String name){
    editor.remove(NAME);
    editor.putString(NAME,name).apply();
}

public void putPhPhone(String phone){
    editor.remove(PHONE);
    editor.putString(PHONE,phone).apply();
}

public void putPhImg(String img){
    editor.remove(IMG);
    editor.putString(IMG,img).apply();
}

public void putPhLocation(String location){
    editor.remove(LOCATION);
    editor.putString(LOCATION,location).apply();
}

public  void putLatLang(String latLang){
    editor.remove(LATLANG);
    latLang=latLang.replace("(","");
    latLang=latLang.replace(")","");
    editor.putString(LATLANG,latLang).apply();
}


public void putPharmacy(String name,String phone,String img,String location,String latLang){
    clear();
    editor.putString(NAME,name);
    editor.putString(PHONE,phone);
    editor.putString(IMG,img);
    editor.putString(LOCATION,location);
    editor.putString(LATLANG,latLang);
    editor.apply();
}

public void clear(){
    editor.clear().apply();
}

public Pharmacy getPharmacy(){
    String name;
    String phone;
    String img;
    String location;
    String latLang;
    name=mSharedPreferences.getString(NAME,"null");
    phone=mSharedPreferences.getString(PHONE,"null");
    img=mSharedPreferences.getString(IMG,"null");
    location= mSharedPreferences.getString(LOCATION, null);
    latLang=mSharedPreferences.getString(LATLANG,null);

    return new Pharmacy(name,phone,img,location,latLang);
}


}
