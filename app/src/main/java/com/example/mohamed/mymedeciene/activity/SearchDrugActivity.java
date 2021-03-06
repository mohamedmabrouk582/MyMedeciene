package com.example.mohamed.mymedeciene.activity;

import android.content.Context;
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
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.adapter.SearchDrugAdapter;
import com.example.mohamed.mymedeciene.data.AllFullDrug;
import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.data.FullDrug;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.mapRoute.MakeRequest;
import com.example.mohamed.mymedeciene.presenter.searchDrug.SearchDrugViewPresenter;
import com.example.mohamed.mymedeciene.utils.FloatingViewService;
import com.example.mohamed.mymedeciene.view.SearchDrugView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 24/01/2018.  time :22:38
 */

//Todo  fix search errors 

public class SearchDrugActivity extends AppCompatActivity implements SearchDrugView{
    private static final String FULLDRUG = "fullDrug";
    private static final String QUERY = "myQuery";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SearchDrugViewPresenter presenter;
    private MakeRequest makeRequest;
    private TextView errorView;
    private List<FullDrug> mFullDrugs=new ArrayList<>();
    private Paint p=new Paint();
    private SearchView mSearchView;
    private  String mQuery,searchBy="drug";
    private MenuItem byDrug,byPharmacy,byBoth;

    public static void start(Context context,String mQuery){
        Intent intent=new Intent(context,SearchDrugActivity.class);
         intent.putExtra(QUERY,mQuery);
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_drugs);
        setData();
        Log.d("AllFullDrug", mFullDrugs.size() + "");
        iniRecyl();
        iniSwipe();
        init();
    }

    private void setData(){
        mFullDrugs=AllFullDrug.getAllFullDrug().getFullDrugs();
        Log.d("mFullDrugsssss", mFullDrugs.size() + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem search=menu.findItem(R.id.app_bar_search_menu);
         byDrug=menu.findItem(R.id.by_drug_name);
         byPharmacy=menu.findItem(R.id.by_pharmacy_name);
         byBoth=menu.findItem(R.id.by_both);
        mSearchView= (SearchView) search.getActionView();
        byDrug.setChecked(true);
//        mSearchView.setQuery(mQuery,true);
//        mSearchView.setIconified(false);
        searchBy="drug";
        searchAction();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.by_drug_name:
                byDrug.setChecked(true);
                searchBy="drug";
                selectSearch();
                break;
            case R.id.by_pharmacy_name:
                byPharmacy.setChecked(true);
                searchBy="pharmacy";
                selectSearch();

                break;
            case R.id.by_both:
                byBoth.setChecked(true);
                searchBy="both";
                selectSearch();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectSearch(){
        Log.d("searchBy", searchBy + "");
        switch (searchBy){
            case "both":
                presenter.searchDrug(mFullDrugs,mQuery);
                break;
            case "drug":
                presenter.searchByDrug(mFullDrugs,mQuery);
                break;
            case "pharmacy":
                presenter.searchByPharmacy(mFullDrugs,mQuery);
                break;
        }
    }

    private void searchAction(){
        Log.d("searchBy", searchBy + "");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                switch (searchBy){
                    case "both":
                        presenter.searchDrug(mFullDrugs,query);
                        break;
                    case "drug":
                        presenter.searchByDrug(mFullDrugs,query);
                        break;
                    case "pharmacy":
                        presenter.searchByPharmacy(mFullDrugs,query);
                        break;
                }
                mQuery=query;
                mSearchView.onActionViewCollapsed();
                mSearchView.setQuery("", false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    private void init(){
        presenter=new SearchDrugViewPresenter(this);
        presenter.attachView(this);
        mQuery=getIntent().getStringExtra(QUERY);
        errorView=findViewById(R.id.error);
        makeRequest=new MakeRequest(this);
        selectSearch();
        Log.d("moahmed", mFullDrugs.size() + "");
    }

    private void iniRecyl() {
        mRecyclerView = findViewById(R.id.all_drugs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void iniSwipe() {
        mSwipeRefreshLayout = findViewById(R.id.all_drugs_stl);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                selectSearch();
            }
        });
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
    public void showDrugs(SearchDrugAdapter adapter) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        },1000);
        if (adapter.getItemCount()==0){
            errorView.setVisibility(View.VISIBLE);
        }else {
            errorView.setVisibility(View.GONE);
        }
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initSwipe();
    }

    @Override
    public void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, final int direction) {
                final int position = viewHolder.getAdapterPosition();
                Pharmacy pharmacy=((FullDrug)presenter.getmFullDrugs().get(position)).getPharmacy();
                if (direction == ItemTouchHelper.LEFT) {

                            //noinspection ConstantConditions
                            showBubble();
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
        startService(new Intent(this, FloatingViewService.class));
    }
}
