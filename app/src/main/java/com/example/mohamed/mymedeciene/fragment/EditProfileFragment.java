package com.example.mohamed.mymedeciene.fragment;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.activity.MapsLocationActivity;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.presenter.editProfile.EditProfilePresenter;
import com.example.mohamed.mymedeciene.presenter.editProfile.EditProfileViewPresenter;
import com.example.mohamed.mymedeciene.utils.QueryListener;
import com.example.mohamed.mymedeciene.utils.utils;
import com.example.mohamed.mymedeciene.view.EditProfileView;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :21:57
 */

@SuppressWarnings({"AccessStaticViaInstance", "unchecked"})
public class EditProfileFragment extends DialogFragment implements EditProfileView, View.OnClickListener {
    private static final String PHARMCY = "pharmacy";
    private AlertDialog dialog;
    private View view;
    private EditText phName, phPhone, phLocation;
    private DataManager dataManager;
    private EditProfileViewPresenter presenter;
    private static QueryListener listener;
    private ImageView location;
    private LatLng mLatLng;


    public static EditProfileFragment newFragment(Pharmacy pharmacy, QueryListener listeners) {
        Bundle bundle = new Bundle();
        listener = listeners;
        bundle.putParcelable(PHARMCY, pharmacy);
        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dataManager = ((MyApp) getActivity().getApplication()).getData();
        view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile, null);
        presenter = new EditProfileViewPresenter(getActivity());
        //noinspection unchecked
        presenter.attachView(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile, null);
        //noinspection ConstantConditions
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        init();
        return view;
    }

    private void init() {
        Pharmacy mPharmacy = getArguments().getParcelable(PHARMCY);
        mLatLng= presenter.getmLatLang(mPharmacy.getPhLocation());

        phName = view.findViewById(R.id.edt_pharmacy_name);
        phPhone = view.findViewById(R.id.edt_pharmacy_phone);
        phLocation = view.findViewById(R.id.edt_pharmacy_location);
        location=view.findViewById(R.id.maps_location);
        Button save = view.findViewById(R.id.save);
        Button close = view.findViewById(R.id.close);
        if (mPharmacy != null) {
            phLocation.setText(String.valueOf(mPharmacy.getPhLocation()));
            phPhone.setText(mPharmacy.getPhPhone());
            phName.setText(mPharmacy.getPhName());
        }
        close.setOnClickListener(this);
        save.setOnClickListener(this);

        location.setOnClickListener(e ->{
            MapsLocationActivity.start(getActivity(),mLatLng,r->{
                phLocation.setText(r.getAddress());
            });
        });

    }




    @Override
    public void close() {
        dialog.dismiss();
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.close:
                close();
                break;
            case R.id.save:
                final String phone = phPhone.getText().toString();
                final String name = phName.getText().toString();
                final String location = phLocation.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    YoYo.with(Techniques.Shake).playOn(phName);
                    presenter.showSnakBar(view, getString(R.string.name_empty));
                } else if (!utils.isValidMobile(phone)) {
                    YoYo.with(Techniques.Shake).playOn(phPhone);
                    presenter.showSnakBar(view, getString(R.string.phone_empty));
                } else if (TextUtils.isEmpty(location)) {
                    YoYo.with(Techniques.Shake).playOn(phLocation);
                    presenter.showSnakBar(view, getString(R.string.location_empty));
                } else {
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
                            presenter.showSnakBar(view, error);
                        }
                    });
                }
                break;
        }
    }
}
