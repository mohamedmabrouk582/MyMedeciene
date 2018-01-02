package com.example.mohamed.mymedeciene.activity;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.fragment.AllDrugsFragment;
import com.example.mohamed.mymedeciene.fragment.DrugsFragment;
import com.example.mohamed.mymedeciene.presenter.Home.HomePresenter;
import com.example.mohamed.mymedeciene.presenter.Home.HomeViewPresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.QueryListener;
import com.example.mohamed.mymedeciene.utils.ZoomIMG;
import com.example.mohamed.mymedeciene.view.HomeView;
import com.google.firebase.database.DatabaseReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,HomeView,View.OnClickListener {
    private static final String PHARMACY = "pharmacy";
    private static final int PERMISSION = 100;
    private CircleImageView phIMG;
    private ImageView edt_img,img_preview;
    private TextView phName,phPhone,phLocation,login,register;
    private Pharmacy mPharmacy;
    private LinearLayout login_register;
    private HomeViewPresenter presenter;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private FrameLayout zoomContainer;
    private DrawerLayout drawer;
    private ZoomIMG zoomIMG;
    private Menu menu;
    private SearchView mSearchView;
    private MenuItem editProfile,myDrugs,addDrugs,logout,search;
    private String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_NETWORK_STATE
            ,Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS};
    private DatabaseReference mDatabaseReference;


    public static void newIntentPharmacy(Context context,Pharmacy pharmacy){
        Intent intent=new Intent(context,HomeActivity.class);
        intent.putExtra(PHARMACY,pharmacy);
         context.startActivity(intent);
    }

    public static  void newIntentUser(Context context){
        context.startActivity(new Intent(context,HomeActivity.class));
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }
        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu=navigationView.getMenu();
        Log.d("menu", menu.size()+ "");
        initView(navigationView.getHeaderView(0));
        presenter=new HomeViewPresenter(this,navigationView.getHeaderView(0));
        presenter.attachView(this);
        setFragment(AllDrugsFragment.newFragment(null));
    }

    @SuppressLint("WrongViewCast")
    private void initView(View view){
        zoomIMG=new ZoomIMG();
        mPharmacy=getIntent().getParcelableExtra(PHARMACY);
       phIMG=view.findViewById(R.id.pharmacy_img);
       phName=view.findViewById(R.id.pharmacy_name);
       phPhone=view.findViewById(R.id.pharmacy_phone);
       edt_img=view.findViewById(R.id.edit_img);
       phLocation=view.findViewById(R.id.pharmacy_location);
       login_register=view.findViewById(R.id.login_register);
       login=view.findViewById(R.id.txt_login);
       register=view.findViewById(R.id.txt_register);
       zoomContainer=findViewById(R.id.zoomContainer);
       img_preview=findViewById(R.id.img_preview);
       edt_img.setOnClickListener(this);
       login.setOnClickListener(this);
       register.setOnClickListener(this);
       phIMG.setOnClickListener(this);
       phLocation.setOnClickListener(this);
       if (mPharmacy!=null){
           isPharmacy(true);

           if (mPharmacy.getPhImgURL() != null ){
               Glide.with(this).load(Uri.parse(mPharmacy.getPhImgURL())).
                    //   error(R.drawable.logo)
                       into(phIMG);
           }

          setData(mPharmacy);
       }else {
           isPharmacy(false);
       }
    }

    private void setData(Pharmacy mData){
        try {
            phName.setText(mData.getPhName());
            phLocation.setText(String.valueOf(mData.getPhLocation().toString()));
            phPhone.setText(mData.getPhPhone());
        }catch (Exception data){

        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        Log.d("menus", menu.size() + "");
        search=menu.findItem(R.id.app_bar_search);
        mSearchView= (SearchView) search.getActionView();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setFragment(AllDrugsFragment.newFragment(query));
                mSearchView.onActionViewCollapsed();
                mSearchView.setQuery("", false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_addDrug) {
            // Handle the camera action
            presenter.addDrug(new AddListener() {
                @Override
                public void onSuccess(String success) {
                  presenter.showSnakBar(drawer,success);
                }

                @Override
                public void OnError(String error) {
                 presenter.showSnakBar(drawer,error);
                }
            });


        } else if (id == R.id.nav_edit) {
            presenter.editProfile(new QueryListener() {
                @Override
                public void onSuccess(Pharmacy pharmacy) {
                    setData(pharmacy);
                    mPharmacy=pharmacy;
                }

                @Override
                public void onError(String error) {

                }
            });
        } else if (id == R.id.nav_drugs) {
               search.setVisible(false);
               setFragment(DrugsFragment.newFragment());
          //  invalidateOptionsMenu();

        } else if (id == R.id.nav_logout) {
            if (mPharmacy!=null) {
               presenter.logout();
            }else {
                presenter.showSnakBar(drawer,"not allow as you are not a Pharmacy . ");
            }
        }else if (id==R.id.nav_home){
            search.setVisible(true);
            setFragment(AllDrugsFragment.newFragment(null));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @SuppressLint("ResourceType")
    @Override
    public void isPharmacy(boolean b) {
        addDrugs=menu.getItem(2);
        editProfile=menu.getItem(0);
        myDrugs=menu.getItem(1);
       logout=menu.getItem(3);
        editProfile.setEnabled(b);
        addDrugs.setEnabled(b);
        myDrugs.setEnabled(b);
        logout.setEnabled(b);
        if (b){
           //   show all items

            login_register.setVisibility(View.GONE);
            phName.setVisibility(View.VISIBLE);
            phPhone.setVisibility(View.VISIBLE);
            phLocation.setVisibility(View.VISIBLE);
        }else {
          // not show show drugs and add drug
            login_register.setVisibility(View.VISIBLE);
            phName.setVisibility(View.GONE);
            phPhone.setVisibility(View.GONE);
            phLocation.setVisibility(View.GONE);


        }
    }

    @Override
    public void login() {
      LoginActivity.start(this);
      finish();
    }

    @Override
    public void register() {
     RegisterActivity.start(this);
     finish();
    }

    @Override
    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Home_Container,fragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pharmacy_location:
                MapsActivity.start(this,mPharmacy.getPhLocation());
                break;
            case R.id.pharmacy_img:
                drawer.closeDrawers();

                mShortAnimationDuration = getResources().getInteger(
                        android.R.integer.config_shortAnimTime);
                zoomIMG.zoomImageFromThumb(this,phIMG,mPharmacy!=null?mPharmacy.getPhImgURL():null,mCurrentAnimator,img_preview,zoomContainer
                        ,mShortAnimationDuration
                );
                break;
            case R.id.edit_img:
                if (mPharmacy!=null) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(this);
                }else {
                 presenter.showSnakBar(drawer,"not allow as you are not a Pharmacy . ");
                }

                break;
            case R.id.txt_login:
                  login();
                break;
            case R.id.txt_register:
                 register();
                break;
        }
    }

    private boolean hasPermission(String permission){
        return ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void  checkPermissions(){
        try {
            for (int i = 0; i <permissions.length ; i++) {
                if (hasPermission(permissions[i])) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[i])) {
//
//                    } else {
//                        requestPermissions(new String[]{permissions[i]}, PERMISSION);
//                    }
                    requestPermissions(new String[]{permissions[i]}, PERMISSION);
                }
            }
        }catch (Exception e){}
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.d("uri", resultUri + "");
                presenter.editIMG(resultUri, new QueryListener() {
                    @Override
                    public void onSuccess(Pharmacy pharmacy) {
                        mPharmacy=pharmacy;
                        Glide.with(HomeActivity.this).load(pharmacy.getPhImgURL())
                               // .error(R.drawable.logo)
                                .into(phIMG);

                    }

                    @Override
                    public void onError(String error) {
                      presenter.showSnakBar(drawer,error);
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                presenter.showSnakBar(drawer,error.getMessage());
            }
        }
    }



}
