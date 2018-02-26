package com.example.mohamed.mymedeciene.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.presenter.addPost.AddPostViewPresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.CheckListener;
import com.example.mohamed.mymedeciene.view.AddPostView;
import com.google.firebase.auth.FirebaseAuth;
import com.theartofdev.edmodo.cropper.CropImage;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 30/01/2018.  time :23:27
 */

public class AddPostFragment extends DialogFragment implements AddPostView,View.OnClickListener, AddListener {
    private AlertDialog dialog;
    private View view;
    private AlertDialog.Builder builder;
    private AddPostViewPresenter presenter;
    private ImageButton postImageButton;
    private TextView txt_load;
    private ProgressBar pro_load;
    private EditText postContent;
    private Button addPost,close;
    private static  AddListener mAddListener;

    public static AddPostFragment newFragment(AddListener listener){
         mAddListener=listener;
        return new AddPostFragment();
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();
        builder = new AlertDialog.Builder(getActivity()).setView(view);
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
        view = LayoutInflater.from(getActivity()).inflate(R.layout.add_post, null);
    }

    private void init(){
        presenter=new AddPostViewPresenter(getActivity());
        presenter.attachView(this);
        postImageButton=view.findViewById(R.id.ib_drug_img);
        pro_load=view.findViewById(R.id.img_load_drug);
        txt_load=view.findViewById(R.id.txt_progress_drug);
        postContent=view.findViewById(R.id.txt_post_content);
        addPost=view.findViewById(R.id.add_post);
        close=view.findViewById(R.id.post_close);
        pro_load.setMax(100);

        close.setOnClickListener(this);
        addPost.setOnClickListener(this);
        postImageButton.setOnClickListener(this);

    }



    @Override
    public void close() {
        dialog.dismiss();
    }

    @Override
    public void setIMG(String uri) {
        Glide.with(getActivity()).load(uri).into(postImageButton);
    }

    @Override
    public void showProgress() {
        pro_load.setVisibility(View.VISIBLE);
        txt_load.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
    pro_load.setVisibility(View.GONE);
    txt_load.setVisibility(View.GONE);
    }

    @Override
    public void setProgress(int por) {
      pro_load.setProgress(por);
      txt_load.setText(String.valueOf(por));
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.post_close:
                close();
                break;
            case R.id.ib_drug_img:
                CropImage.activity()
                        .start(getContext(), this);
                break;
            case R.id.add_post:
                String s=postContent.getText().toString();
                if (TextUtils.isEmpty(s)){
                    YoYo.with(Techniques.Shake).playOn(postContent);
                    presenter.showSnakBar(view,"Post Content not be Empty");
                }else {
                    presenter.Post(s,this);

                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("resultCode", requestCode + " : "+ CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.d("resultCode", resultUri + "");
                presenter.PostImg(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onSuccess(String success) {
        mAddListener.onSuccess("Post Added");
      close();
    }

    @Override
    public void OnError(String error) {
        mAddListener.onSuccess(error);
        presenter.showSnakBar(view,error);
    }
}
