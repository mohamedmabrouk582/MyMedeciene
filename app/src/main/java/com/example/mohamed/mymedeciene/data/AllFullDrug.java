package com.example.mohamed.mymedeciene.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 24/01/2018.  time :23:22
 */

public class AllFullDrug {
   private static   volatile AllFullDrug allFullDrug;
   public List<FullDrug> fullDrugs=new ArrayList<>();

   public static AllFullDrug getAllFullDrug(){
       if (allFullDrug==null){
           synchronized (AllFullDrug.class){
               allFullDrug=new AllFullDrug();
           }
       }
       return allFullDrug;
   }

   private AllFullDrug(){}

   public void setAllFullDrug(FullDrug fullDrug){
       this.fullDrugs.add(fullDrug);
   }

   public List<FullDrug> getFullDrugs(){
       return this.fullDrugs;
   }

   public void clear(){
       this.fullDrugs.clear();
   }

}
