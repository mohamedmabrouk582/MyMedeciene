package com.example.mohamed.mymedeciene.activity;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.data.dataBase.DBoperations;
import com.example.mohamed.mymedeciene.fragment.AddDrugCheckFragment;
import com.example.mohamed.mymedeciene.fragment.AllDrugsFragment;
import com.example.mohamed.mymedeciene.fragment.DrugsFragment;
import com.example.mohamed.mymedeciene.mapRoute.MakeRequest;
import com.example.mohamed.mymedeciene.presenter.Home.HomeViewPresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.CheckListener;
import com.example.mohamed.mymedeciene.utils.QueryListener;
import com.example.mohamed.mymedeciene.utils.ZoomIMG;
import com.example.mohamed.mymedeciene.view.HomeView;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ALL")
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeView, View.OnClickListener, LocationListener {
    private static final String PHARMACY = "pharmacy";
    private static final int PERMISSION = 100;
    private CircleImageView phIMG;
    private ImageView edt_img, img_preview;
    private TextView phName, phPhone, phLocation, login, register;
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
    public static volatile String myCurrentLocation = null;
    private MenuItem editProfile, myDrugs, addDrugs, logout, search;
    private final String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE
            , Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS};
    private DatabaseReference mDatabaseReference;
    private MakeRequest makeRequest;
    private DataManager dataManager;


    public static void newIntentPharmacy(Context context, Pharmacy pharmacy) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(PHARMACY, pharmacy);
        context.startActivity(intent);
    }

    public static void newIntentUser(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        overLayPermission();
        Toolbar toolbar = findViewById(R.id.toolbar);
        makeRequest = new MakeRequest(this);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }
        location();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();
        Log.d("menu", menu.size() + "");
        initView(navigationView.getHeaderView(0));
        dataManager=((MyApp) getApplication()).getData();
        presenter = new HomeViewPresenter(this, navigationView.getHeaderView(0));
        presenter.attachView(this);
        setFragment(AllDrugsFragment.newFragment(null));
    }

    private void location() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        }else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //noinspection ConstantConditions
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 50, this);
        }

    }

    private void overLayPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            }
        } else {
            Intent intent = new Intent(this, Service.class);
            startService(intent);
        }
    }

    @SuppressLint("WrongViewCast")
    private void initView(View view) {
        zoomIMG = new ZoomIMG();
        mPharmacy = getIntent().getParcelableExtra(PHARMACY);
        phIMG = view.findViewById(R.id.pharmacy_img);
        phName = view.findViewById(R.id.pharmacy_name);
        phPhone = view.findViewById(R.id.pharmacy_phone);
        edt_img = view.findViewById(R.id.edit_img);
        phLocation = view.findViewById(R.id.pharmacy_location);
        login_register = view.findViewById(R.id.login_register);
        login = view.findViewById(R.id.txt_login);
        register = view.findViewById(R.id.txt_register);
        zoomContainer = findViewById(R.id.zoomContainer);
        img_preview = findViewById(R.id.img_preview);
        edt_img.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        phIMG.setOnClickListener(this);
        phLocation.setOnClickListener(this);


        if (mPharmacy != null) {
            isPharmacy(true);

            if (mPharmacy.getPhImgURL() != null) {
                Glide.with(this).load(Uri.parse(mPharmacy.getPhImgURL())).
                                into(phIMG);
            }

            setData(mPharmacy);
        } else {
            isPharmacy(false);
        }
    }

    private void setData(Pharmacy mData) {
        try {
            phName.setText(mData.getPhName());
            phLocation.setText(String.valueOf(mData.getPhLocation()));
            phPhone.setText(mData.getPhPhone());
        } catch (Exception ignored) {

        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        Log.d("menus", menu.size() + "");
        search = menu.findItem(R.id.app_bar_search);
        mSearchView = (SearchView) search.getActionView();

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_addDrug) {
            presenter.addDrug(new AddListener() {
                @Override
                public void onSuccess(String success) {
                    presenter.showSnakBar(drawer, success);
                }

                @Override
                public void OnError(String error) {
                    presenter.showSnakBar(drawer, error);
                }
            });


        } else if (id == R.id.nav_edit) {
            presenter.editProfile(new QueryListener() {
                @Override
                public void onSuccess(Pharmacy pharmacy) {
                    setData(pharmacy);
                    mPharmacy = pharmacy;
                }

                @Override
                public void onError(String error) {

                }
            });
        } else if (id == R.id.nav_drugs) {
            search.setVisible(false);
            setFragment(DrugsFragment.newFragment());
        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, DBoperations.getInstance(this).getDrugs().size() + "", Toast.LENGTH_SHORT).show();
            if (mPharmacy != null) {
                presenter.logout();
            } else {
                presenter.showSnakBar(drawer, getString(R.string.not_allow));
            }
        } else if (id == R.id.nav_home) {
            search.setVisible(true);
            setFragment(AllDrugsFragment.newFragment(null));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @SuppressLint("ResourceType")
    @Override
    public void isPharmacy(boolean b) {
        addDrugs = menu.getItem(2);
        editProfile = menu.getItem(0);
        myDrugs = menu.getItem(1);
        logout = menu.getItem(3);
        editProfile.setEnabled(b);
        addDrugs.setEnabled(b);
        myDrugs.setEnabled(b);
        logout.setEnabled(b);
        if (b) {
            //   show all items

            login_register.setVisibility(View.GONE);
            phName.setVisibility(View.VISIBLE);
            phPhone.setVisibility(View.VISIBLE);
            phLocation.setVisibility(View.VISIBLE);
        } else {
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Home_Container, fragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pharmacy_location:
                try {
                    if (!dataManager.getIsBubbleShow()) {
                        makeRequest.initializeBubblesManager();
                        makeRequest.addNewBubble();
                    }
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" +
                            "saddr=" + myCurrentLocation + "&daddr=" + mPharmacy.getLatLang() + "&sensor=false&units=metric&mode=driving"));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                } catch (Exception e) {
                    makeRequest.go(mPharmacy.getLatLang());

                }
                break;
            case R.id.pharmacy_img:
                drawer.closeDrawers();

                mShortAnimationDuration = getResources().getInteger(
                        android.R.integer.config_shortAnimTime);
                zoomIMG.zoomImageFromThumb(this, phIMG, mPharmacy != null ? mPharmacy.getPhImgURL() : null, mCurrentAnimator, img_preview, zoomContainer
                        , mShortAnimationDuration
                );
                break;
            case R.id.edit_img:
                if (mPharmacy != null) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(this);
                } else {
                    presenter.showSnakBar(drawer, getString(R.string.not_allow));
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

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        try {
            for (int i = 0; i < permissions.length; i++) {
                if (hasPermission(permissions[i])) {
                    requestPermissions(permissions, PERMISSION);
                }
            }
        } catch (Exception ignored) {
        }
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
                        mPharmacy = pharmacy;
                        Glide.with(HomeActivity.this).load(pharmacy.getPhImgURL())
                                .into(phIMG);

                    }

                    @Override
                    public void onError(String error) {
                        presenter.showSnakBar(drawer, error);
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                presenter.showSnakBar(drawer, error.getMessage());
            }
        }
    }









    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        myCurrentLocation = latLng.toString().replace("(", "").replace(")", "").replace("lat/lng:", "");
        Toast.makeText(this, myCurrentLocation, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
