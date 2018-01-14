package com.example.mohamed.mymedeciene.Holder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.data.Pharmacy;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 24/12/2017.  time :00:08
 */

public class DrugHolder extends RecyclerView.ViewHolder {
    private final Activity activity;
    private final View view;
    public ImageView imageView;
    private TextView drugName;
    private TextView drugPrice;
    private TextView drugQuantity;
    private TextView drugType;
    private TextView phName;
    private TextView phPhone;
    public TextView phLocation;


    public DrugHolder(View itemView, Activity activity) {
        super(itemView);
        view = itemView;
        init();
        this.activity = activity;
    }

    private void init() {
        imageView = view.findViewById(R.id.drug_img);
        drugName = view.findViewById(R.id.drug_name);
        drugType = view.findViewById(R.id.drug_type);
        drugPrice = view.findViewById(R.id.drug_price);
        drugQuantity = view.findViewById(R.id.drug_quantity);
        phName = view.findViewById(R.id.ph_name);
        phLocation = view.findViewById(R.id.ph_location);
        phPhone = view.findViewById(R.id.ph_phone);

    }

    public void bindData(Drug drug, Pharmacy pharmacy) {
        if (activity != null && drug != null && pharmacy != null) {
            Glide.with(activity).load(drug.getImg()).into(imageView);
            drugName.setText(drug.getName());
            drugPrice.setText(String.valueOf(drug.getPrice()));
            drugType.setText(drug.getType());
            drugQuantity.setText(drug.getQuantity());
            phName.setText(pharmacy.getPhName());
            phPhone.setText(pharmacy.getPhPhone());
            phLocation.setText(pharmacy.getPhLocation());
        }
    }


}
