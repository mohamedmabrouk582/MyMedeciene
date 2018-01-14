package com.example.mohamed.mymedeciene.presenter.Home;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.activity.HomeActivity;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.fragment.AddGrugFragment;
import com.example.mohamed.mymedeciene.fragment.EditProfileFragment;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.QueryListener;
import com.example.mohamed.mymedeciene.view.HomeView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 18/12/2017.  time :19:00
 */

@SuppressWarnings("ALL")
public class HomeViewPresenter<v extends HomeView> extends BasePresenter<v> implements HomePresenter<v> {
    private final Activity activity;
    private final StorageReference mStorageReference;
    private final DatabaseReference mDatabaseReference;
    private final FirebaseAuth mAuth;
    private final DataManager dataManager;
    private final View view;
    private final ProgressBar loadProgressBar;
    private final TextView txtLoad;

    public HomeViewPresenter(Activity activity, View view) {
        this.activity = activity;
        mAuth = MyApp.getmAuth();
        dataManager = ((MyApp) activity.getApplication()).getData();
        this.view = view;
        mStorageReference = MyApp.getmStorageReference();

        mDatabaseReference = MyApp.getmDatabaseReference().child("Pharmacy");
        loadProgressBar = view.findViewById(R.id.img_load);
        txtLoad = view.findViewById(R.id.txt_progress);
    }


    @Override
    public void editIMG(Uri uri, final QueryListener listener) {
        loadProgressBar.setVisibility(View.VISIBLE);
        txtLoad.setVisibility(View.VISIBLE);

        new Compressor(activity)
                .compressToFileAsFlowable(new File(uri.getPath()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        Log.d("file", file.getTotalSpace() + "");

                        @SuppressWarnings("ConstantConditions") String root = "Pharmacy/" + mAuth.getCurrentUser().getUid() + "/profile_image";
                        mStorageReference.child(root).putFile(Uri.fromFile(file)).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadProgressBar.setVisibility(View.GONE);
                                txtLoad.setVisibility(View.GONE);
                                listener.onError(e.getMessage());
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                txtLoad.setText(String.valueOf((int) progress));
                                loadProgressBar.setProgress((int) progress);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                //noinspection ConstantConditions
                                mDatabaseReference.child(mAuth.getUid()).child("phImgURL").setValue(String.valueOf(taskSnapshot.getDownloadUrl()))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dataManager.setPhIMG(String.valueOf(taskSnapshot.getDownloadUrl()));
                                                loadProgressBar.setVisibility(View.GONE);
                                                txtLoad.setVisibility(View.GONE);
                                                listener.onSuccess(dataManager.getPharmacy());
                                                //Picasso.with(context).load(taskSnapshot.getDownloadUrl()).into(view);
                                            }
                                        });
                            }
                        });

                    }
                });
    }

    @Override
    public void editProfile(QueryListener listener) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        EditProfileFragment fragment = EditProfileFragment.newFragment(dataManager.getPharmacy(), listener);
        fragment.show(fragmentManager, "");
    }

    @Override
    public void addDrug(AddListener listener) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        AddGrugFragment fragment = AddGrugFragment.newFragment(null, null, listener);
        fragment.show(fragmentManager, "");
    }

    @Override
    public void logout() {
        HomeActivity.newIntentUser(activity);
        dataManager.clear();
        mAuth.signOut();
        activity.finish();
    }


}
