package com.example.mohamed.mymedeciene.fragment;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohamed.mymedeciene.Holder.DrugHolder;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.presenter.myDrugs.DrugsViewPresenter;
import com.example.mohamed.mymedeciene.utils.ZoomIMG;
import com.example.mohamed.mymedeciene.view.DrugsView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 23/12/2017.  time :23:52
 */

public class DrugsFragment extends Fragment implements View.OnClickListener,DrugsView{
    private FloatingActionsMenu actionMenu;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View view;
    private FirebaseRecyclerAdapter adapter;
    private DatabaseReference mDatabaseReference;
    private Query query;
    private DrugsViewPresenter presenter;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerOptions options;
    private DataManager dataManager;
    private FrameLayout drugContainer;
    private ImageView   drug_preview;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private ZoomIMG zoomIMG;
    private TextView errorView;


    public static DrugsFragment newFragment(){

        return new DrugsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.my_drugs_list,container,false);
         init();
         iniRecyl();
         iniSwipe();
        showDrugs();
        return view;
    }


    private void init(){
        dataManager=((MyApp) getActivity().getApplication()).getData();
        presenter=new DrugsViewPresenter(getActivity());
        presenter.attachView(this);
        errorView=view.findViewById(R.id.errors);
        drug_preview=view.findViewById(R.id.drug_preview);
        drugContainer=view.findViewById(R.id.drug_container);
        actionMenu=view.findViewById(R.id.menu);
        mAuth=MyApp.getmAuth();
        mDatabaseReference= MyApp.getmDatabaseReference();
        query=mDatabaseReference.child("Drugs").child(mAuth.getUid()).limitToFirst(10);
        options=new FirebaseRecyclerOptions.Builder<Drug>().setQuery(query,Drug.class).build();
        FloatingActionButton add=new FloatingActionButton(getActivity());
        add.setTitle("Add new Drug");
        add.setColorDisabled(R.color.white_pressed);
        add.setIcon(R.drawable.ic_add);
        add.setSize(FloatingActionButton.SIZE_MINI);
        add.setColorDisabled(R.color.white_pressed);
        actionMenu.addButton(add);
        add.setOnClickListener(this);
        zoomIMG=new ZoomIMG();

    }

    @Override
    public void onStart() {
        super.onStart();
       adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void iniRecyl(){
        mRecyclerView=view.findViewById(R.id.my_drugs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void iniSwipe(){
        mSwipeRefreshLayout=view.findViewById(R.id.my_drugs_stl);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showDrugs();
                adapter.startListening();
            }
        });
    }

    @Override
    public void onClick(View view) {
       presenter.addNewDrugs();
       actionMenu.collapse();
    }

    @Override
    public void showProgress() {
        if (!mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void hideProgress() {
        if (mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void DrugClickedMessage(Drug drug) {

    }

    @Override
    public void showDrugs() {
        showProgress();
        errorView.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        },1000l);
        adapter=new FirebaseRecyclerAdapter<Drug,DrugHolder>(options) {
            @Override
            public DrugHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(getActivity()).inflate(R.layout.drug_item,parent,false);
                return new DrugHolder(view,getActivity());
            }

            @Override
            protected void onBindViewHolder(@NonNull final DrugHolder holder, int position, @NonNull final Drug model) {
                errorView.setVisibility(View.GONE);

                holder.bindData(model,dataManager.getPharmacy());
                 holder.imageView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         mShortAnimationDuration = getResources().getInteger(
                                 android.R.integer.config_shortAnimTime);
                         zoomIMG.zoomImageFromThumb(getActivity(),holder.imageView,model.getImg(),mCurrentAnimator,drug_preview,drugContainer
                                 ,mShortAnimationDuration
                         );
                     }
                 });
            }
        };
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }



}
