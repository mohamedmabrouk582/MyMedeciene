package com.example.mohamed.mymedeciene.fragment;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.mymedeciene.Holder.DrugHolder;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.activity.HomeActivity;
import com.example.mohamed.mymedeciene.activity.SearchDrugActivity;
import com.example.mohamed.mymedeciene.adapter.SearchDrugAdapter;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.AllFullDrug;
import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.data.FullDrug;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.data.dataBase.DBoperations;
import com.example.mohamed.mymedeciene.mapRoute.MakeRequest;
import com.example.mohamed.mymedeciene.presenter.allDrugs.AllDrugsViewPresenter;
import com.example.mohamed.mymedeciene.utils.FloatingViewService;
import com.example.mohamed.mymedeciene.utils.NetworkChangeReceiver;
import com.example.mohamed.mymedeciene.utils.SortPlaces;
import com.example.mohamed.mymedeciene.utils.ZoomIMG;
import com.example.mohamed.mymedeciene.view.AllDrugsView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 24/12/2017.  time :22:08
 */

@SuppressWarnings("ALL")
public class AllDrugsFragment extends Fragment implements AllDrugsView, NetworkChangeReceiver.ConnectivityReceiverListener,HomeActivity.SearchQueryListener {
    private static final String QUERY = "query";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View view;
    private FirebaseRecyclerAdapter adapter;
    private DatabaseReference mDatabaseReference;
    private Query query;
    private AllDrugsViewPresenter presenter;
    private FirebaseRecyclerOptions options;
    private DataManager dataManager;
    private FrameLayout drugContainer;
    private ImageView drug_preview;
    private Animator mCurrentAnimator;
    private DatabaseReference mPhmacyReference;
    private int mShortAnimationDuration;
    private ZoomIMG zoomIMG;
    private TextView errorView;
    private ProgressDialog mProgressDialog;
    private final Paint p = new Paint();
    private MakeRequest makeRequest;
    private List<FullDrug> fullDrugs=new ArrayList<>();
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;


    public static AllDrugsFragment newFragment() {
        Bundle bundle = new Bundle();
        AllDrugsFragment fragment = new AllDrugsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.all_drugs, container, false);
        init();
        iniRecyl();
        iniSwipe();
        showDrugs();
        DBoperations.getInstance(getActivity()).deleteAll();
        makeRequest = new MakeRequest(getActivity());
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.wait_dailog));
        return view;
    }

    private void init() {
        HomeActivity.setQueryListener(this);
        //noinspection ConstantConditions
        dataManager = ((MyApp) getActivity().getApplication()).getData();
        mPhmacyReference = MyApp.getmDatabaseReference().child("Pharmacy");
        presenter = new AllDrugsViewPresenter(getActivity());
        presenter.attachView(this);
        errorView = view.findViewById(R.id.error);
        drug_preview = view.findViewById(R.id.all_drug_preview);
        drugContainer = view.findViewById(R.id.all_drugs_container);
        mDatabaseReference = MyApp.getmDatabaseReference();

            //noinspection ConstantConditions
        query = mDatabaseReference.child("Drugs").child("AllDrugs").orderByChild("name").limitToFirst(10);
        options = new FirebaseRecyclerOptions.Builder<Drug>().setQuery(query, Drug.class).build();
        query.keepSynced(true);

        zoomIMG = new ZoomIMG();
        //getAllDrugs();
    }



    private void iniRecyl() {
        mRecyclerView = view.findViewById(R.id.all_drugs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void iniSwipe() {
        mSwipeRefreshLayout = view.findViewById(R.id.all_drugs_stl);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query = mDatabaseReference.child("Drugs").child("AllDrugs").orderByChild("name").limitToFirst(10);
                options = new FirebaseRecyclerOptions.Builder<Drug>().setQuery(query, Drug.class).build();
                Log.d("adapter", options.getSnapshots().size() + "");

                showDrugs();
                adapter.startListening();
            }
        });
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

    @Override
    public void showProgress() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
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
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showDrugs() {
        showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, 1000L);
        errorView.setVisibility(View.VISIBLE);


        adapter = new FirebaseRecyclerAdapter<Drug, DrugHolder>(options) {
            @Override
            public DrugHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.drug_item, parent, false);
                return new DrugHolder(view, getActivity());
            }

            @Override
            protected void onBindViewHolder(@NonNull final DrugHolder holder, int position, @NonNull final Drug model) {
                errorView.setVisibility(View.GONE);


                mPhmacyReference.child(model.getPhKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Pharmacy value = dataSnapshot.getValue(Pharmacy.class);
                        DBoperations.getInstance(getActivity()).insertDrug(model);
                        holder.bindData(model, value);
                        Log.d("llll", value.getLatLang() + "");

                        holder.phLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mProgressDialog.show();

                                getActivity().startService(new Intent(getActivity(), FloatingViewService.class));

                                try {
                                    @SuppressWarnings("ConstantConditions") final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" +
                                            "saddr=" + AllFullDrug.getAllFullDrug().getMyLocation() + "&daddr=" + value.getLatLang() + "&sensor=false&units=metric&mode=driving"));
                                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                                    startActivity(intent);
                                } catch (Exception e) {
                                    //noinspection ConstantConditions
                                    makeRequest.go(value.getLatLang());

                                }
                                mProgressDialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mShortAnimationDuration = getResources().getInteger(
                                android.R.integer.config_shortAnimTime);
                        zoomIMG.zoomImageFromThumb(getActivity(), holder.imageView, model.getImg(), mCurrentAnimator, drug_preview, drugContainer
                                , mShortAnimationDuration
                        );
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
        initSwipe();
    }
    private void initSwipe() {
        Log.d("swipe", "rr" + "");
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, final int direction) {
                final int position = viewHolder.getAdapterPosition();
                DataSnapshot sna = (DataSnapshot) adapter.getSnapshots().getSnapshot(position);
                Drug drug = sna.getValue(Drug.class);
                //noinspection ConstantConditions
                mPhmacyReference.child(drug.getPhKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Pharmacy pharmacy = dataSnapshot.getValue(Pharmacy.class);
                        if (direction == ItemTouchHelper.LEFT) {
                              showBubble();
                            //noinspection ConstantConditions
                            presenter.call(pharmacy.getPhPhone());
                        } else {

                                showBubble();
                            try {
                                @SuppressWarnings("ConstantConditions") final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" +
                                        "saddr=" + AllFullDrug.getAllFullDrug().getMyLocation() + "&daddr=" + pharmacy.getLatLang() + "&sensor=false&units=metric&mode=driving"));
                                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                                startActivity(intent);
                            } catch (Exception e) {
                                //noinspection ConstantConditions
                                makeRequest.go(pharmacy.getLatLang());


                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;


                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_dialog_map);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_call);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }

                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    private void showBubble(){
        getActivity().startService(new Intent(getActivity(), FloatingViewService.class));

    }

    @Override
    public void onPause() {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApp.setConnectivityListener(this);

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showDrugs();
        adapter.startListening();
        presenter.showSnakBar(view, isConnected ? getString(R.string.connected) : getString(R.string.no_connected));
    }


    @Override
    public void onQuery(String querys) {
        Log.d("onQuery", adapter.getSnapshots().size() + "");
//        fullDrugs.clear();
//        query = mDatabaseReference.child("Drugs").child("AllDrugs").orderByChild("name").equalTo(querys);
//        options = new FirebaseRecyclerOptions.Builder<Drug>().setQuery(query, Drug.class).build();
//        showDrugs();
//        adapter.startListening();
        SearchDrugActivity.start(getActivity(),querys);

        Log.d("adapter",  options.getSnapshots().size()+"sss");
    }
}
