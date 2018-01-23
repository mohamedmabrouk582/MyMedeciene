package com.example.mohamed.mymedeciene.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mohamed.mymedeciene.Holder.DrugHolder;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.data.FullDrug;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 23/01/2018.  time :22:07
 */

public class SearchDrugAdapter extends RecyclerView.Adapter<DrugHolder> {
    private Activity context;
    private List<FullDrug> drugs=new ArrayList<>();

    public SearchDrugAdapter(Activity context,List<FullDrug> fullDrugs){
     this.context=context;
     this.drugs=fullDrugs;
    }
    @Override
    public DrugHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.drug_item,parent,false);
        return new DrugHolder(view,context);
    }

    @Override
    public void onBindViewHolder(DrugHolder holder, int position) {
        FullDrug fullDrug = drugs.get(position);
        holder.bindData(fullDrug.getDrug(),fullDrug.getPharmacy());
    }

    @Override
    public int getItemCount() {
        return drugs.size();
    }
}
