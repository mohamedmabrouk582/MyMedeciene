package com.example.mohamed.mymedeciene.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.activity.RegisterActivity;
import com.example.mohamed.mymedeciene.presenter.login.LoginViewPresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.utils;
import com.example.mohamed.mymedeciene.view.LoginView;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :23:00
 */

@SuppressWarnings("unchecked")
public class LoginFragment extends Fragment implements LoginView, View.OnClickListener {
    private View view;
    private EditText email, password;
    private LoginViewPresenter presenter;
    private ProgressBar progressBar;


    public static LoginFragment newFragment() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);
        init();
        return view;
    }

    @SuppressWarnings("unchecked")
    private void init() {
        presenter = new LoginViewPresenter(getActivity());
        //noinspection unchecked
        presenter.attachView(this);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        Button login = view.findViewById(R.id.but_login);
        TextView register = view.findViewById(R.id.create_account);
        progressBar = view.findViewById(R.id.login_progressBar);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_login:
                showProgress();

                login();
                break;
            case R.id.create_account:
                createAccount();
                break;
        }
    }

    @Override
    public void login() {
        String em = email.getText().toString();
        String pass = password.getText().toString();

        if (!utils.isValidMobile(em)) {
            YoYo.with(Techniques.Shake).playOn(email);
            presenter.showSnakBar(view, getString(R.string.phone_invalid));
        } else if (TextUtils.isEmpty(pass) || pass.length() < 6) {
            YoYo.with(Techniques.Shake).playOn(password);
            presenter.showSnakBar(view, getString(R.string.password_invalid));
        } else {
            presenter.login(em, pass, new AddListener() {
                @Override
                public void onSuccess(String success) {
                    hideProgress();

                    presenter.showSnakBar(view, success);
                }

                @Override
                public void OnError(String error) {
                    hideProgress();
                    presenter.showSnakBar(view, error);

                }
            });
        }
    }

    @Override
    public void createAccount() {
        RegisterActivity.start(getActivity());
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
