package com.example.mohamed.mymedeciene.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.activity.HomeActivity;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.presenter.addDrug.AddDrugViewPresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.QueryListener;
import com.example.mohamed.mymedeciene.view.AddDrugView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 19/12/2017.  time :10:27
 */

public class AddGrugFragment extends DialogFragment implements AddDrugView,View.OnClickListener{
    private View view;
    private EditText name,price;
    private Spinner type,quantitySpinner;
    private ImageButton drugIMG;
    private Button close,add;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private AddDrugViewPresenter presenter;
    private static AddListener listener;
    private Uri mUri;


    public static AddGrugFragment newFragment(AddListener listeners){
        listener=listeners;

        return new AddGrugFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
         initView();
         builder=new AlertDialog.Builder(getActivity()).setView(view);

         dialog=builder.create();
         dialog.setCanceledOnTouchOutside(false);
         dialog.show();
        return dialog;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        init();
        return view;
    }
    private void initView(){
        view= LayoutInflater.from(getActivity()).inflate(R.layout.add_drug,null);
    }
    private void init(){
        presenter=new AddDrugViewPresenter(getActivity(),view);
        presenter.attachView(this);
        quantitySpinner=view.findViewById(R.id.sp_drug_quantity);
        name=view.findViewById(R.id.edt_drug_name);
        price=view.findViewById(R.id.edt_drug_price);
        type=view.findViewById(R.id.sp_drug_type);
        drugIMG=view.findViewById(R.id.ib_drug_img);
        close=view.findViewById(R.id.close_drug);
        add=view.findViewById(R.id.add);

        close.setOnClickListener(this);
        add.setOnClickListener(this);
        drugIMG.setOnClickListener(this);
        setDATATOsPINNER();

    }

    @Override
    public void close() {
    dialog.dismiss();
    }

    @Override
    public void addDrugIMG() {

    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close_drug:
                close();
                break;
            case R.id.add:
                String dName=name.getText().toString();
                String dPrice=price.getText().toString();
                String dType=type.getSelectedItem().toString();

                if (TextUtils.isEmpty(dName)){
                    YoYo.with(Techniques.Shake).playOn(name);
                    presenter.showSnakBar(view,"Drug name not be Empty ");
                }else if (TextUtils.isEmpty(dPrice)){
                    YoYo.with(Techniques.Shake).playOn(name);
                    presenter.showSnakBar(view,"Drug price not be Empty ");
                }else{
                   presenter.addDrug(dName, dPrice, dType, quantitySpinner.getSelectedItem().toString(), new AddListener() {
                       @Override
                       public void onSuccess(String success) {
                           dialog.dismiss();
                       }

                       @Override
                       public void OnError(String error) {

                       }
                   });
                }
                break;
            case R.id.ib_drug_img:
                CropImage.activity()
                        .start(getContext(), this);
                break;
        }
    }

    private void setDATATOsPINNER(){
        String[] stringArray = getActivity().getResources().getStringArray(R.array.drug_type);
        List<String> list=new ArrayList<>();
        List<String> quantity=new ArrayList<>();
        for (String s:stringArray) {
            list.add(s);
        }

        for (int i = 0; i <100 ; i++) {
            quantity.add(String.valueOf((i+1)));
        }
        ArrayAdapter<String > adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,list);
        ArrayAdapter<String > adapterQuantity=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,quantity);
        type.setAdapter(adapter);
        quantitySpinner.setAdapter(adapterQuantity);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                mUri=resultUri;
                presenter.addDrugIMG(resultUri,drugIMG);
                Log.d("eee", mUri + "");

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
