package com.example.mohamed.mymedeciene.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.presenter.addDrugCheck.AddDrugCheckViewPresenter;
import com.example.mohamed.mymedeciene.utils.CheckListener;
import com.example.mohamed.mymedeciene.view.AddDrugCheckView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 14/01/2018.  time :00:03
 */

public class AddDrugCheckFragment extends DialogFragment implements AddDrugCheckView{

    private AddDrugCheckViewPresenter presenter;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private View view;
    private static CheckListener listener;
    private Button decline,update;

    public static AddDrugCheckFragment newFragment(CheckListener mListener){
         listener=mListener;
        return new AddDrugCheckFragment();
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
        view= LayoutInflater.from(getActivity()).inflate(R.layout.add_drug_check,null);
    }
    private void init(){
        presenter=new AddDrugCheckViewPresenter();
        presenter.attachView(this);
        decline=view.findViewById(R.id.decline);
        update=view.findViewById(R.id.drug_update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              presenter.update(listener);
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decline();
            }
        });
    }
    @Override
    public void decline() {
        dialog.dismiss();
    }


}
