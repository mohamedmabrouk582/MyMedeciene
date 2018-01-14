package com.example.mohamed.mymedeciene.presenter.myDrugs;

import android.app.Activity;
import android.app.FragmentManager;

import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.fragment.AddGrugFragment;
import com.example.mohamed.mymedeciene.presenter.base.BasePresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.view.DrugsView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 23/12/2017.  time :23:44
 */

@SuppressWarnings("unchecked")
public class DrugsViewPresenter<v extends DrugsView> extends BasePresenter<v> implements DrugsPresenter<v> {
    private final Activity activity;
    private final DatabaseReference mDatabaseReference;
    private final FirebaseAuth mAuth;

    public DrugsViewPresenter(Activity activity) {
        this.activity = activity;
        mAuth = MyApp.getmAuth();
        mDatabaseReference = MyApp.getmDatabaseReference().child("Drugs");
    }

    @Override
    public void addNewDrugs(AddListener listener) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        AddGrugFragment fragment = AddGrugFragment.newFragment(null, null, listener);
        fragment.show(fragmentManager, "");

    }


    @SuppressWarnings("unchecked")
    @Override
    public void deleteDrug(String drugId, final AddListener listener) {
        Map map = new HashMap();
        //noinspection unchecked
        map.put("AllDrugs/" + drugId, null);
        //noinspection unchecked
        map.put(mAuth.getUid() + "/" + drugId, null);

        //noinspection unchecked
        mDatabaseReference.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    listener.onSuccess("");
                } else {
                    listener.OnError(databaseError.getMessage());
                }
            }
        });
    }

    @Override
    public void editDrug(Drug drug, String id, AddListener listener) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        AddGrugFragment fragment = AddGrugFragment.newFragment(drug, id, listener);
        fragment.show(fragmentManager, "");
    }


}
