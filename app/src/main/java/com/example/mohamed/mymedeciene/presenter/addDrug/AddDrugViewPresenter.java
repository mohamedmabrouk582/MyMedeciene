package com.example.mohamed.mymedeciene.presenter.addDrug;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.fragment.AddDrugCheckFragment;
import com.example.mohamed.mymedeciene.presenter.Home.HomeViewPresenter;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.CheckListener;
import com.example.mohamed.mymedeciene.view.AddDrugView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 19/12/2017.  time :10:26
 */

@SuppressWarnings({"unused", "unchecked"})
public class AddDrugViewPresenter<v extends AddDrugView> extends BasePresenter<v> implements AddDrugPresenter<v> {
    private final Activity activity;
    private HomeViewPresenter presenter;
    private final ProgressBar loadProgressBar;
    private final TextView txtLoad;
    private final StorageReference mStorageReference;
    private final DatabaseReference mDatabaseReference;
    private final FirebaseAuth mAuth;
    private final Map map;
    private String url;
    private boolean found;
    private final Set<String> mSet = new HashSet<>();
    private DataManager dataManager;

    public AddDrugViewPresenter(Activity activity, View container) {
        this.activity = activity;
        mAuth = MyApp.getmAuth();
        map = new HashMap();
        mStorageReference = MyApp.getmStorageReference();
        mDatabaseReference = MyApp.getmDatabaseReference().child("Drugs");
        loadProgressBar = container.findViewById(R.id.img_load_drug);
        txtLoad = container.findViewById(R.id.txt_progress_drug);
        dataManager=MyApp.getData();
        allDrugs();
    }


    @Override
    public void addDrugIMG(final Uri img, final ImageButton imageButton) {
        loadProgressBar.setVisibility(View.VISIBLE);
        txtLoad.setVisibility(View.VISIBLE);
        new Compressor(activity)
                .compressToFileAsFlowable(new File(img.getPath()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        @SuppressWarnings("ConstantConditions") String root = "Drugs/" + mAuth.getCurrentUser().getUid() + "/" + img.getLastPathSegment();
                        mStorageReference.child(root).putFile(Uri.fromFile(file)).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadProgressBar.setVisibility(View.GONE);
                                txtLoad.setVisibility(View.GONE);
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
                                loadProgressBar.setVisibility(View.GONE);
                                txtLoad.setVisibility(View.GONE);
                                url = String.valueOf(taskSnapshot.getDownloadUrl());
                                Glide.with(activity).load(url).into(imageButton);
                            }
                        });

                    }
                });
    }


    @Override
    public void addDrug(String name, String price, String type, String quantity, final AddListener listener) {
        map.put("name", name);
        map.put("type", type);
        map.put("price", price);
        map.put("quantity", quantity);
        map.put("phKey", mAuth.getUid());
        map.put("latlang",dataManager.getPharmacy().getLatLang());
        if (url != null) {
            map.put("img", url);
        }

        DatabaseReference push = FirebaseDatabase.getInstance().getReference().push();
        updateChildren(mAuth.getUid() + name + type, map, listener);
    }

    private void allDrugs() {
        //noinspection ConstantConditions
        mDatabaseReference.child(mAuth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mSet.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void updateChildren(final String key, Map map, final AddListener listener) {
        final Map drugs = new HashMap();
        drugs.put(mAuth.getUid() + "/" + key, map);
        drugs.put("AllDrugs/" + key, map);
        if (mSet.contains(key)) {
            FragmentManager fragmentManager = activity.getFragmentManager();
            AddDrugCheckFragment fragment = AddDrugCheckFragment.newFragment(new CheckListener() {
                @Override
                public void onSuccess() {
                    updateChildren(listener, drugs);
                }
            });
            fragment.show(fragmentManager, "check");
        } else {
            updateChildren(listener, drugs);
        }

    }

    private void updateChildren(final AddListener listener, Map drugs) {
        mDatabaseReference.updateChildren(drugs, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    getView().close();
                    listener.onSuccess(activity.getString(R.string.add_drug_done));
                } else {
                    getView().close();
                    listener.OnError(databaseError.getMessage());
                }
            }
        });
    }

    @Override
    public void editDrug(String drugId, String img, String name, String price, String type, String Quantity, AddListener listener) {
        map.put("name", name);
        map.put("type", type);
        map.put("price", price);
        map.put("quantity", Quantity);
        map.put("phKey", mAuth.getUid());
        map.put("img", img);
        if (url != null) {
            map.put("img", url);
        }
        final Map drugs = new HashMap();
        drugs.put(mAuth.getUid() + "/" + drugId, map);
        drugs.put("AllDrugs/" + drugId, map);
        updateChildren(listener, drugs);
    }


}
