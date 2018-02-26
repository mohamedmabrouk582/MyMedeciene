package com.example.mohamed.mymedeciene.fragment;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.activity.RegisterActivity;
import com.example.mohamed.mymedeciene.presenter.addDrugCheck.AddDrugCheckViewPresenter;
import com.example.mohamed.mymedeciene.utils.CheckListener;
import com.example.mohamed.mymedeciene.view.AddDrugCheckView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 14/01/2018.  time :00:03
 */

@SuppressWarnings("unchecked")
public class AddDrugCheckFragment extends DialogFragment implements AddDrugCheckView {

    private static final String TYPE = "type";
    private AddDrugCheckViewPresenter presenter;
    private AlertDialog dialog;
    private View view;
    private static CheckListener listener;
    private String type;
    private TextView massage;
    Button update,decline;
    public static AddDrugCheckFragment newFragment(String type,CheckListener mListener) {
        listener = mListener;
        Bundle bundle=new Bundle();
        bundle.putString(TYPE,type);
        AddDrugCheckFragment fragment=new AddDrugCheckFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static AddDrugCheckFragment newFragment(String type) {
        Bundle bundle=new Bundle();
        bundle.putString(TYPE,type);
        AddDrugCheckFragment fragment=new AddDrugCheckFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(view);

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        //noinspection ConstantConditions
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        init();
        return view;
    }


    @SuppressLint("InflateParams")
    private void initView() {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.add_drug_check, null);
    }

    @SuppressWarnings("unchecked")
    private void init() {
        type=getArguments().getString(TYPE);
        presenter = new AddDrugCheckViewPresenter();
        //noinspection unchecked
        presenter.attachView(this);
         decline = view.findViewById(R.id.decline);
        update = view.findViewById(R.id.drug_update);
        massage=view.findViewById(R.id.textCheck);
         check();
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decline();
            }
        });
    }

    private void check(){
        if (type.equals("register")){
            massage.setText(getString(R.string.regist));
            update.setText(getString(R.string.register));
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RegisterActivity.start(getActivity());
                    getActivity().finish();
                }
            });
        }else {
            massage.setText(getString(R.string.checkDrug));
            update.setText(getString(R.string.update));
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.update(listener);
                }
            });
        }
    }

    @Override
    public void decline() {
        dialog.dismiss();
    }


}
