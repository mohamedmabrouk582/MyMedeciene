package com.example.mohamed.mymedeciene.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.presenter.editProfile.EditProfilePresenter;
import com.example.mohamed.mymedeciene.presenter.editProfile.EditProfileViewPresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.QueryListener;
import com.example.mohamed.mymedeciene.utils.utils;
import com.example.mohamed.mymedeciene.view.EditProfileView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :21:57
 */

public class EditProfileFragment extends DialogFragment implements EditProfileView,View.OnClickListener{
    private static final String PHARMCY = "pharmacy";
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    public View view;
    private EditText phName,phPhone,phLocation;
    private Button save,close;
    private Pharmacy mPharmacy;
    private DataManager dataManager;
    private EditProfileViewPresenter presenter;
    private static QueryListener listener;


    public static EditProfileFragment newFragment(Pharmacy pharmacy, QueryListener listeners){
        Bundle bundle=new Bundle();
        listener=listeners;
        bundle.putParcelable(PHARMCY,pharmacy);
        EditProfileFragment fragment=new EditProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dataManager=((MyApp) getActivity().getApplication()).getData();
        view= LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile,null);
        presenter=new EditProfileViewPresenter(getActivity());
        presenter.attachView(this);
        builder=new AlertDialog.Builder(getActivity())
                .setView(view);
        dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile,null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        init();
        return view;
    }

     private void init(){
        mPharmacy=getArguments().getParcelable(PHARMCY);
        phName=view.findViewById(R.id.edt_pharmacy_name);
        phPhone=view.findViewById(R.id.edt_pharmacy_phone);
        phLocation=view.findViewById(R.id.edt_pharmacy_location);
        save=view.findViewById(R.id.save);
        close=view.findViewById(R.id.close);
        if (mPharmacy!=null){
            phLocation.setText(String.valueOf(mPharmacy.getPhLocation()));
            phPhone.setText(mPharmacy.getPhPhone());
            phName.setText(mPharmacy.getPhName());
        }
        close.setOnClickListener(this);
        save.setOnClickListener(this);
     }




    @Override
    public void close() {
     dialog.dismiss();
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()){
            case R.id.close:
                close();
                break;
            case R.id.save:
                final String phone=phPhone.getText().toString();
                final String name=phName.getText().toString();
                final String location=phLocation.getText().toString();

                if (TextUtils.isEmpty(name)){
                    YoYo.with(Techniques.Shake).playOn(phName);
                    presenter.showSnakBar(view,"name not be Empty");
                }else if (!utils.isValidMobile(phone)){
                    YoYo.with(Techniques.Shake).playOn(phPhone);
                    presenter.showSnakBar(view,"phone not invalid ");
                }else if (TextUtils.isEmpty(location)){
                    YoYo.with(Techniques.Shake).playOn(phLocation);
                    presenter.showSnakBar(view,"location not be Empty");
                }else{
                   presenter.save(name, phone, location, new EditProfilePresenter.editListner() {
                       @Override
                       public void onSuccess(String latLang) {
                           dialog.dismiss();
                           dataManager.setPhLocation(location);
                           dataManager.setPhName(name);
                           dataManager.setPhPhone(phone);
                           dataManager.setLatLang(latLang);
                           listener.onSuccess(dataManager.getPharmacy());

                           Log.d("dd", dataManager.getPharmacy().getPhName() + "");
                       }

                       @Override
                       public void onError(String error) {
                           listener.onError(error);
                         presenter.showSnakBar(view,error);
                       }
                   });
                }
                break;
        }
    }
}
