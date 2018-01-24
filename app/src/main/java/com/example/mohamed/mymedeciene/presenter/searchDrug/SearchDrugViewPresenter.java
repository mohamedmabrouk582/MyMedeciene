package com.example.mohamed.mymedeciene.presenter.searchDrug;

import android.app.Activity;
import android.util.Log;

import com.example.mohamed.mymedeciene.activity.HomeActivity;
import com.example.mohamed.mymedeciene.adapter.SearchDrugAdapter;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.FullDrug;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.utils.SortPlaces;
import com.example.mohamed.mymedeciene.view.SearchDrugView;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 24/01/2018.  time :19:15
 */

public class SearchDrugViewPresenter<v extends SearchDrugView> extends BasePresenter<v> implements SearchDrugPresenter<v> {
    private Activity activity;
    private SearchDrugAdapter adapter;
    private List<FullDrug> mFullDrugs;
    private DataManager dataManager;
    public SearchDrugViewPresenter(Activity activity){
        this.activity=activity;
        this.mFullDrugs=new ArrayList<>();
        dataManager= MyApp.getData();
    }

    @Override
    public void searchDrug(List<FullDrug> fullDrugs, String query) {
        clear();
        for (FullDrug fullDrug:fullDrugs) {
            if (fullDrug.getDrug().getName().startsWith(query)){
               mFullDrugs.add(fullDrug);
            }
        }
          mFullDrugs=SortDrugs(mFullDrugs);
         adapter=new SearchDrugAdapter(activity,mFullDrugs);

         getView().showDrugs(adapter);
    }

    public SearchDrugAdapter getAdapter(){
        return adapter;
    }

    public List<FullDrug> getmFullDrugs(){
        return mFullDrugs;
    }

    private List<FullDrug> SortDrugs(List<FullDrug> resultFullDrugs){

        String[] split =null;
        if (HomeActivity.myCurrentLocation==null){
           split=dataManager==null?new String[]{"30.0886576","31.2799054"}:dataManager.getPharmacy().getLatLang().split(",");
        }else {
            split = HomeActivity.myCurrentLocation.split(",");
        }
        Double lat=Double.parseDouble(split[0]);
        Double lang=Double.parseDouble(split[1]);
        Collections.sort(resultFullDrugs,new SortPlaces(new LatLng(lat,lang)));
        for (FullDrug fullDrug:resultFullDrugs) {
            Log.d("sortedDrug", fullDrug.getPharmacy().toString() + "");
        }
        return resultFullDrugs;
    }



    @Override
    public void clickDrug(FullDrug drug) {

    }

    @Override
    public void clear() {
        mFullDrugs.clear();
    }


    public void call(String phPhone) {

    }
}
