package com.example.mohamed.mymedeciene.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.activity.HomeActivity;
import com.example.mohamed.mymedeciene.activity.LoginActivity;
import com.example.mohamed.mymedeciene.activity.MapsLocationActivity;
import com.example.mohamed.mymedeciene.activity.locationActivity;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.presenter.register.RegisterViewPresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.utils;
import com.example.mohamed.mymedeciene.view.RegisterView;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :23:42
 */

@SuppressWarnings({"AccessStaticViaInstance", "unchecked"})
public class RegisterFragment extends Fragment implements RegisterView, View.OnClickListener {
    private View view;
    private EditText phName, phPhone, phLocation, phPassword;
    private ProgressBar progressBar;
    private RegisterViewPresenter presenter;
    private DataManager dataManager;
    private ImageView location;
    private LatLng mLatLng;

    public static RegisterFragment newFragment() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_fragment, container, false);
        init();
        return view;
    }

    @SuppressWarnings("unchecked")
    private void init() {
        //noinspection ConstantConditions
        dataManager = ((MyApp) getActivity().getApplication()).getData();
        presenter = new RegisterViewPresenter(getActivity());
        //noinspection unchecked
        presenter.attachView(this);
        phLocation = view.findViewById(R.id.pharmacyLocation);
        phName = view.findViewById(R.id.pharmacyName);
        phPhone = view.findViewById(R.id.email_register);
        location=view.findViewById(R.id.maps_location);
        phPassword = view.findViewById(R.id.password_register);
        Button register = view.findViewById(R.id.but_register);
        TextView login = view.findViewById(R.id.txt_login);
        progressBar = view.findViewById(R.id.register_progressBar);

        register.setOnClickListener(this);
        login.setOnClickListener(this);

        location.setOnClickListener(e ->{
            MapsLocationActivity.start(getContext(),mLatLng,r->{
                mLatLng=new LatLng(Double.parseDouble(r.getLat()),Double.parseDouble(r.getLang()));
                phLocation.setText(r.getAddress());
            });
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_login:
                login();
                break;
            case R.id.but_register:
                String email = phPhone.getText().toString();
                String pass = phPassword.getText().toString();
                String name = phName.getText().toString();
                String location = phLocation.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    YoYo.with(Techniques.Shake).playOn(phName);
                    presenter.showSnakBar(view, getString(R.string.ph_name_invalid));
                } else if (!utils.isValidMobile(email)) {
                    YoYo.with(Techniques.Shake).playOn(phPhone);
                    presenter.showSnakBar(view, getString(R.string.ph_phone_invalid));
                } else if (TextUtils.isEmpty(pass)) {
                    YoYo.with(Techniques.Shake).playOn(phPassword);
                    presenter.showSnakBar(view, getString(R.string.ph_pass_invalid));

                } else if (TextUtils.isEmpty(location)) {
                    YoYo.with(Techniques.Shake).playOn(phLocation);
                    presenter.showSnakBar(view, getString(R.string.ph_location_invalid));

                } else {
                    showProgress();
                    register(name, email, pass, location);
                }
                break;
        }
    }

    @Override
    public void register(String userName, String email, String password, String location) {
        presenter.register(userName, email, password, location, new AddListener() {
            @Override
            public void onSuccess(String success) {
                hideProgress();
                HomeActivity.newIntentPharmacy(getActivity(), dataManager.getPharmacy());
            }

            @Override
            public void OnError(String error) {
                hideProgress();
                presenter.showSnakBar(view, error);
            }
        });
    }

    @Override
    public void login() {
        LoginActivity.start(getActivity());
        //noinspection ConstantConditions
        getActivity().finish();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }
}
