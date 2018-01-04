package com.example.mohamed.mymedeciene.appliction;

import android.location.Location;

import com.example.mohamed.mymedeciene.data.Pharmacy;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 23/12/2017.  time :15:15
 */

public class DataManager {
    private MyShared myShared;

    public DataManager(MyShared myShared){
        this.myShared=myShared;
    }

    public void setPhName(String name){
        myShared.putPhName(name);
    }
    public void setPhPhone(String phone){
        myShared.putPhPhone(phone);
    }
    public void setPhIMG(String img){
        myShared.putPhImg(img);
    }
    public void setPhLocation(String location){
        myShared.putPhLocation(location);
    }

    public void setPharmacy(String name,String phone, String img,String location,String latLang){
        myShared.putPharmacy(name,phone,img,location,latLang);
    }

    public void setLatLang(String latLang){
        myShared.putLatLang(latLang);
    }

    public void clear(){
        myShared.clear();
    }

    public Pharmacy getPharmacy(){
      return myShared.getPharmacy();
    }

}
