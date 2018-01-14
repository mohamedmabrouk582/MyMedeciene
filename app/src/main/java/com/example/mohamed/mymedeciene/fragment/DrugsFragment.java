package com.example.mohamed.mymedeciene.fragment;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

import com.example.mohamed.mymedeciene.Holder.DrugHolder;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.presenter.myDrugs.DrugsViewPresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.NetworkChangeReceiver;
import com.example.mohamed.mymedeciene.utils.ZoomIMG;
import com.example.mohamed.mymedeciene.view.DrugsView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamed mabrouk
 * 0201152644726
 * on 23/12/2017.  time :23:52
 */

@SuppressWarnings("ALL")
public class DrugsFragment extends Fragment implements View.OnClickListener, DrugsView, NetworkChangeReceiver.ConnectivityReceiverListener {
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
    private ImageView drug_preview;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private ZoomIMG zoomIMG;
    private TextView errorView;
    private final List<String> drugsId = new ArrayList<>();
    private final List<Drug> mDrugs = new ArrayList<>();
    private final Paint p = new Paint();

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    public static DrugsFragment newFragment() {

        return new DrugsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_drugs_list, container, false);
        init();
        iniRecyl();
        iniSwipe();
        showDrugs();
        return view;
    }


    private void init() {
        //noinspection ConstantConditions
        dataManager = ((MyApp) getActivity().getApplication()).getData();
        presenter = new DrugsViewPresenter(getActivity());
        presenter.attachView(this);
        errorView = view.findViewById(R.id.errors);
        drug_preview = view.findViewById(R.id.drug_preview);
        drugContainer = view.findViewById(R.id.drug_container);
        actionMenu = view.findViewById(R.id.menu);
        mAuth = MyApp.getmAuth();
        mDatabaseReference = MyApp.getmDatabaseReference();
        //noinspection ConstantConditions
        query = mDatabaseReference.child("Drugs").child(mAuth.getUid()).limitToFirst(10);
        options = new FirebaseRecyclerOptions.Builder<Drug>().setQuery(query, Drug.class).build();
        FloatingActionButton add = new FloatingActionButton(getActivity());
        add.setTitle("Add new Drug");
        add.setColorDisabled(R.color.white_pressed);
        add.setIcon(R.drawable.ic_add);
        add.setSize(FloatingActionButton.SIZE_MINI);
        add.setColorDisabled(R.color.white_pressed);
        actionMenu.addButton(add);
        add.setOnClickListener(this);
        zoomIMG = new ZoomIMG();

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

    private void iniRecyl() {
        mRecyclerView = view.findViewById(R.id.my_drugs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void iniSwipe() {
        mSwipeRefreshLayout = view.findViewById(R.id.my_drugs_stl);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                showDrugs();
                adapter.startListening();
            }
        });
    }

    @Override
    public void onClick(final View view) {
        presenter.addNewDrugs(new AddListener() {
            @Override
            public void onSuccess(String success) {
                presenter.showSnakBar(view, getString(R.string.drug_added));
            }

            @Override
            public void OnError(String error) {
                presenter.showSnakBar(view, error);

            }
        });
        actionMenu.collapse();
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
        errorView.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, 1000L);
        adapter = new FirebaseRecyclerAdapter<Drug, DrugHolder>(options) {
            @Override
            public DrugHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.drug_item, parent, false);
                return new DrugHolder(view, getActivity());
            }

            @Override
            protected void onBindViewHolder(@NonNull final DrugHolder holder, int position, @NonNull final Drug model) {
                errorView.setVisibility(View.GONE);
                DataSnapshot snapshot = (DataSnapshot) options.getSnapshots().getSnapshot(position);
                drugsId.add(snapshot.getKey());
                mDrugs.add(model);
                Log.d("dddd", mDrugs.size() + "");
                holder.bindData(model, dataManager.getPharmacy());
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
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    removeItem(position);
                } else {
                    Log.d("len", mDrugs.size() + "");
                    editDrug(mDrugs.get(position), drugsId.get(position));
                }
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
                        icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_edit);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_delete);
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

    private void editDrug(Drug drug, String id) {
        mDrugs.clear();
        drugsId.clear();
        adapter.notifyDataSetChanged();
        presenter.editDrug(drug, id, new AddListener() {
            @Override
            public void onSuccess(String success) {
                mDrugs.clear();
                drugsId.clear();
                adapter.notifyDataSetChanged();
                presenter.showSnakBar(view, getString(R.string.drug_update));
            }

            @Override
            public void OnError(String error) {
                presenter.showSnakBar(view, error);
            }
        });
    }

    private void removeItem(final int position) {
        presenter.deleteDrug(drugsId.get(position), new AddListener() {
            @Override
            public void onSuccess(String success) {
                drugsId.remove(position);
                mDrugs.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemChanged(position, drugsId.size());

            }

            @Override
            public void OnError(String error) {
                presenter.showSnakBar(view, error);
            }
        });
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
        presenter.showSnakBar(view, isConnected ? getString(R.string.connected) : getString(R.string.no_connected));
    }

}
