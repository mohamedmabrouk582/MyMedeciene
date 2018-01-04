package com.example.mohamed.mymedeciene.fragment;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.mohamed.mymedeciene.Holder.DrugHolder;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.activity.MapsActivity;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.presenter.allDrugs.AllDrugsViewPresenter;
import com.example.mohamed.mymedeciene.presenter.myDrugs.DrugsViewPresenter;
import com.example.mohamed.mymedeciene.utils.ZoomIMG;
import com.example.mohamed.mymedeciene.view.AllDrugsView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 24/12/2017.  time :22:08
 */

public class AllDrugsFragment extends Fragment implements AllDrugsView{
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
    private String querys;
    private TextView errorView;
    private ProgressDialog mProgressDialog;
    private Paint p = new Paint();
    private List<String> phones=new ArrayList<>();
    private List<String> locations=new ArrayList<>();


    public static AllDrugsFragment newFragment(String query){
        Bundle bundle=new Bundle();
        bundle.putSerializable(QUERY,query);
        AllDrugsFragment fragment=new AllDrugsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.all_drugs,container,false);
        init();
        iniRecyl();
        iniSwipe();
        showDrugs();
        mProgressDialog=new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("wait for Open map ......");
        return view;
    }

    private void init(){
        querys=getArguments().getString(QUERY);
        dataManager=((MyApp) getActivity().getApplication()).getData();
        mPhmacyReference=MyApp.getmDatabaseReference().child("Pharmacy");
        presenter=new AllDrugsViewPresenter(getActivity());
        presenter.attachView(this);
        errorView=view.findViewById(R.id.error);
        drug_preview=view.findViewById(R.id.all_drug_preview);
        drugContainer=view.findViewById(R.id.all_drugs_container);
        mDatabaseReference= MyApp.getmDatabaseReference();
        if (querys!=null ){
            Log.d("query", querys + "");
            query = mDatabaseReference.child("Drugs").child("AllDrugs").orderByChild("name").equalTo(querys);
        }else {
            Log.d("query", querys + "");
            query = mDatabaseReference.child("Drugs").child("AllDrugs").orderByChild("name").limitToFirst(10);
        }
        query.keepSynced(true);
        options=new FirebaseRecyclerOptions.Builder<Drug>().setQuery(query,Drug.class).build();

        zoomIMG=new ZoomIMG();

    }
    private void iniRecyl(){
        mRecyclerView=view.findViewById(R.id.all_drugs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void iniSwipe(){
        mSwipeRefreshLayout=view.findViewById(R.id.all_drugs_stl);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query = mDatabaseReference.child("Drugs").child("AllDrugs").limitToFirst(10);
                options=new FirebaseRecyclerOptions.Builder<Drug>().setQuery(query,Drug.class).build();

                showDrugs();
                adapter.startListening();
            }
        });
    }
    @Override
    public void searchDrugs(String drugName) {

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        },1000l);
        errorView.setVisibility(View.VISIBLE);


        adapter=new FirebaseRecyclerAdapter<Drug,DrugHolder>(options) {
            @Override
            public DrugHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(getActivity()).inflate(R.layout.drug_item,parent,false);
                return new DrugHolder(view,getActivity());
            }

            @Override
            protected void onBindViewHolder(@NonNull final DrugHolder holder, int position, @NonNull final Drug model) {
                errorView.setVisibility(View.GONE);


                mPhmacyReference.child(model.getPhKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Pharmacy value = dataSnapshot.getValue(Pharmacy.class);
                        holder.bindData(model,value);
                        holder.phLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mProgressDialog.show();
                                MapsActivity.start(getActivity(),value.getLatLang(),value.getPhLocation());
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
                        zoomIMG.zoomImageFromThumb(getActivity(),holder.imageView,model.getImg(),mCurrentAnimator,drug_preview,drugContainer
                                ,mShortAnimationDuration
                        );
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
        initSwipe();
    }


    private void initSwipe(){
        Log.d("swipe", "rr" + "");
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, final int direction) {
                final int position = viewHolder.getAdapterPosition();
                DataSnapshot sna= (DataSnapshot) adapter.getSnapshots().getSnapshot(position);
                Drug drug=sna.getValue(Drug.class);
                mPhmacyReference.child(drug.getPhKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Pharmacy pharmacy=dataSnapshot.getValue(Pharmacy.class);
                        if (direction == ItemTouchHelper.LEFT){

                            presenter.call(pharmacy.getPhPhone());
                        }else {
                            MapsActivity.start(getActivity(),pharmacy.getLatLang(),pharmacy.getPhLocation());
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
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;


                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(),android.R.drawable.ic_dialog_map);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_call);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }

                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

}
