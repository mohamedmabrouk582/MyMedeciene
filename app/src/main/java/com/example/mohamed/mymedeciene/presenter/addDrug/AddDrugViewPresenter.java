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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.fragment.AddDrugCheckFragment;
import com.example.mohamed.mymedeciene.presenter.Home.HomeViewPresenter;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.CheckListener;
import com.example.mohamed.mymedeciene.utils.QueryListener;
import com.example.mohamed.mymedeciene.view.AddDrugView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

public class AddDrugViewPresenter<v extends AddDrugView> extends BasePresenter<v> implements AddDrugPresenter<v> {
    private Activity activity;
    private HomeViewPresenter presenter;
    private ProgressBar loadProgressBar;
    private TextView txtLoad;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private Map map;
    private String url;
    private boolean found;
    private Set<String> mSet=new HashSet<>();

    public AddDrugViewPresenter(Activity activity,View container){
        this.activity=activity;
        mAuth= MyApp.getmAuth();
        map=new HashMap();
        mStorageReference=MyApp.getmStorageReference();
        mDatabaseReference=MyApp.getmDatabaseReference().child("Drugs");
        loadProgressBar=container.findViewById(R.id.img_load_drug);
        txtLoad=container.findViewById(R.id.txt_progress_drug);
        allDrugs();
    }


    @Override
    public void addDrugIMG(final Uri img, final ImageButton imageButton) {
        loadProgressBar.setVisibility(View.VISIBLE);
        txtLoad.setVisibility(View.VISIBLE);
         Log.d("ererer", img + "");
        new Compressor(activity)
                .compressToFileAsFlowable(new File(img.getPath()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        String root="Drugs/"+mAuth.getCurrentUser().getUid()+"/"+img.getLastPathSegment();
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
                                txtLoad.setText(String.valueOf((int)progress));
                                loadProgressBar.setProgress((int) progress);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                loadProgressBar.setVisibility(View.GONE);
                                txtLoad.setVisibility(View.GONE);
                                url=String.valueOf(taskSnapshot.getDownloadUrl());
                                Glide.with(activity).load(url).into(imageButton);
                            }
                        });

                    }
                });
    }



    @Override
    public void addDrug(String name, String price, String type, String quantity, final AddListener listener) {
          map.put("name",name);
          map.put("type",type);
          map.put("price",price);
          map.put("quantity",quantity);
          map.put("phKey",mAuth.getUid());
          if (url!=null){
              map.put("img",url);
          }

        DatabaseReference push = FirebaseDatabase.getInstance().getReference().push();
        updateChildren(mAuth.getUid()+name+type,map,listener);
    }

    private void allDrugs(){
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



    private void updateChildren(final String key, Map map, final AddListener listener){
        final Map drugs=new HashMap();
        drugs.put(mAuth.getUid()+"/"+key,map);
        drugs.put("AllDrugs/"+key,map);
           Log.d("allme",  mSet.toString()+ "");
         if (mSet.contains(key)){
             FragmentManager fragmentManager=activity.getFragmentManager();
        AddDrugCheckFragment fragment=AddDrugCheckFragment.newFragment(new CheckListener() {
            @Override
            public void onSuccess() {
                updateChildren(listener,drugs);
            }
        });
        fragment.show(fragmentManager,"check");
         }else {
             updateChildren(listener,drugs);
          }

    }

    private void updateChildren(final AddListener listener,Map drugs){
        mDatabaseReference.updateChildren(drugs, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError==null){
                    getView().close();
                    listener.onSuccess("add Drug Done");
                }    else {
                    getView().close();
                    listener.OnError(databaseError.getMessage());
                }
            }
        });
    }

    @Override
    public void editDrug(String drugId,String img, String name, String price, String type, String Quantity, AddListener listener) {
        map.put("name",name);
        map.put("type",type);
        map.put("price",price);
        map.put("quantity",Quantity);
        map.put("phKey",mAuth.getUid());
        map.put("img",img);
        if (url!=null){
            map.put("img",url);
        }
        final Map drugs=new HashMap();
        drugs.put(mAuth.getUid()+"/"+drugId,map);
        drugs.put("AllDrugs/"+drugId,map);
        updateChildren(listener,drugs);
    }


}
