package com.example.mohamed.mymedeciene.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 24/01/2018.  time :23:22
 */

public class AllFullDrug {
   private static   volatile AllFullDrug allFullDrug;
   public List<FullDrug> fullDrugs=new ArrayList<>();
   public LatLng myLatLang;
   public String MyLocation;

   public static AllFullDrug getAllFullDrug(){
       if (allFullDrug==null){
           synchronized (AllFullDrug.class){
               allFullDrug=new AllFullDrug();
           }
       }
       return allFullDrug;
   }

   private AllFullDrug(){}

   public void setLatLang(LatLng myLatLang){
       this.myLatLang=myLatLang;
   }

   public void setMyLocation(String myLocation){
       this.MyLocation=myLocation;
   }

   public String getMyLocation(){
       return this.MyLocation;
   }

   public LatLng getMyLatLang(){
       return this.myLatLang;
   }
   public void setAllFullDrug(FullDrug fullDrug){
       this.fullDrugs.add(fullDrug);
   }

   public List<FullDrug> getFullDrugs(){
       Set<FullDrug> set=new HashSet<>();
       set.addAll(this.fullDrugs);
       clear();
       this.fullDrugs.addAll(set);
       return this.fullDrugs;
   }

   public void clear(){
       this.fullDrugs.clear();
   }

}
