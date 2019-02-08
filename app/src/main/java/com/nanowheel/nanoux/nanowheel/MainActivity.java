package com.nanowheel.nanoux.nanowheel;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
//import io.reactivex.Single;
//import io.reactivex.observers.DisposableSingleObserver;

import com.nanowheel.nanoux.nanowheel.util.BluetoothService;
import com.nanowheel.nanoux.nanowheel.util.BluetoothUtilImpl;
import com.nanowheel.nanoux.nanowheel.util.SharedPreferencesUtil;
//import com.tbruyelle.rxpermissions2.RxPermissions;


import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,SharedPreferences.OnSharedPreferenceChangeListener{


    //BluetoothUtil bluetoothUtil;
    public static Context mContext;
    int frag;
    //com.nanowheel.nanoux.nanowheel.databinding.ActivityMainBinding mBinding;
    //public static OWDevice mOWDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (App.INSTANCE == null) {
            App.INSTANCE = new App();
        }
        mContext = this;
        SharedPreferencesUtil test = App.INSTANCE.getSharedPreferences(this);
        test.registerListener(this);

        if (BuildConfig.FLAVOR == "pro"){//this makes loads map fragment faster no touchy!
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MapView mv = new MapView(getApplicationContext());
                        mv.onCreate(null);
                        mv.onPause();
                        mv.onDestroy();
                    }catch (Exception ignored){

                    }
                }
            }).start();
        }

        boolean isDark = App.INSTANCE.getSharedPreferences().isDarkNightMode();
        updateTheme(isDark);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);

        if (savedInstanceState != null && App.INSTANCE.getSharedPreferences(this).getDialogAccepted() && App.INSTANCE.getSharedPreferences(this).getGeminiDialogAccepted()){
            if (savedInstanceState.getBoolean("should_scan")){
                StartService();
            }
            frag = savedInstanceState.getInt("active_frag");
            switch (frag){
                case 0:
                    loadFragment(new FragmentOverview(),false);
                    navigation.setSelectedItemId(R.id.overview_button);
                    break;
                case 1:
                    loadFragment(new FragmentDiagnotics(),false);
                    navigation.setSelectedItemId(R.id.diagnotics_button);
                    break;
                case 2:
                    loadFragment(new FragmentMap(),false);
                    navigation.setSelectedItemId(R.id.map_button);
                    break;
                case 3:
                    loadFragment(new FragmentSettings(),false);
                    navigation.setSelectedItemId(R.id.settings_button);
                    break;
            }

        }else {
            if (intent.getBooleanExtra("should_scan",true)) {
                StartService();
                loadFragment(new FragmentOverview(),false);
                navigation.setSelectedItemId(R.id.overview_button);
                frag = 0;
            }else{
                loadFragment(new FragmentSettings(),false);
                navigation.setSelectedItemId(R.id.settings_button);
                frag = 3;
            }
        }

        navigation.setOnNavigationItemSelectedListener(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !isDark) {
            View view = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR | SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(uiOptions);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mContext = null;
        SharedPreferencesUtil test = App.INSTANCE.getSharedPreferences(this);
        test.removeListener(this);
    }

    private boolean loadFragment(Fragment fragment, boolean animate){
        if (fragment != null){
            androidx.fragment.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (animate) {
                ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            }

            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
            return true;
        }

        return false;
    }

    FragmentMap mapFrag;
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Fragment fragment = null;
        switch(menuItem.getItemId()){
            case R.id.overview_button:
                if (frag != 0) {
                    fragment = new FragmentOverview();
                    frag = 0;
                    mapFrag = null;
                }
                break;

            case R.id.diagnotics_button:
                if (frag != 1) {
                    fragment = new FragmentDiagnotics();
                    frag = 1;
                    mapFrag = null;
                }
                break;

            case R.id.map_button:
                if (frag != 2) {
                    mapFrag = new FragmentMap();
                    fragment = mapFrag;
                    frag = 2;

                }
                break;

            case R.id.settings_button:
                if (frag != 3) {
                    fragment = new FragmentSettings();
                    frag = 3;
                    mapFrag = null;
                }
                break;
        }
        return loadFragment(fragment, true);
    }


    final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 2442;
    final int MY_PERMISSIONS_SYSTEM_ALERT_WINDOW = 2443;
    public void StartService(){
        if (!App.INSTANCE.getSharedPreferences(this).getGeminiDialogAccepted()){
            final RelativeLayout layout = findViewById(R.id.geminidisclaimer);
            layout.setVisibility(View.VISIBLE);
            Button button = findViewById(R.id.gemini_disclaimer_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation out = AnimationUtils.loadAnimation(MainActivity.mContext, R.anim.slide_out);
                    out.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    layout.startAnimation(out);
                    App.INSTANCE.getSharedPreferences().setGeminiDialogAccepted(true);
                    StartService();
                }
            });
        }else if (!App.INSTANCE.getSharedPreferences(this).getDialogAccepted()){
            final RelativeLayout layout = findViewById(R.id.disclaimer);
            layout.setVisibility(View.VISIBLE);
            Button button = findViewById(R.id.disclaimer_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation out = AnimationUtils.loadAnimation(MainActivity.mContext, R.anim.slide_out);
                    out.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    layout.startAnimation(out);
                    App.INSTANCE.getSharedPreferences().setDialogAccepted(true);
                    StartService();
                }
            });
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    BluetoothService.createService(this);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
                }
            } else {
                BluetoothService.createService(this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    BluetoothService.createService(mContext);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("active_frag",frag);
        outState.putBoolean("should_scan",false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case SharedPreferencesUtil.METRIC_UNITS:
                if (BluetoothService.isExist) {
                    BluetoothService.mOWDevice.refreshUnits(this);
                }
                WidgetUpdater.updateSpeed(getApplication());
                WidgetUpdater.updateRange(getApplication());
                break;

            case SharedPreferencesUtil.DARK_NIGHT_MODE:
                Intent intent = getIntent();
                intent.putExtra("active_frag",frag);
                intent.putExtra("should_scan",false);
                finish();

                startActivity(intent);
                break;

            case SharedPreferencesUtil.LOG_TRIPS:
                WidgetUpdater.updateLog(getApplication());
                if (!App.INSTANCE.getSharedPreferences().isMap()) {
                    WidgetUpdater.endLog();
                }
                if (BuildConfig.FLAVOR == "pro") {
                    if (frag == 2) {
                        WidgetUpdater.checkLogWarning(frag,mapFrag);
                    }
                    if (frag == 3){
                        //loadFragment(new FragmentSettings(),false);
                    }
                }
                break;

            case SharedPreferencesUtil.FLOAT_STAT:
                if (App.INSTANCE.getSharedPreferences().isFloatStat()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(this)) {
                            //blyat
                        } else {
                            Intent intentSet = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intentSet, MY_PERMISSIONS_SYSTEM_ALERT_WINDOW);
                        }
                    }
                }
                break;

        }

    }
    void updateTheme(boolean isDark){
        if (isDark){
            setTheme(R.style.AppTheme_Dark);
        }else{
            setTheme(R.style.AppTheme_Light);
        }
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        if (BuildConfig.FLAVOR == "pro") {
            Boolean bool = WidgetUpdater.backProcess(frag,mapFrag);
            if (!bool){
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //mContext = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_PERMISSIONS_SYSTEM_ALERT_WINDOW) {

            //Check if the permission is granted or not.
            if (Settings.canDrawOverlays(this)) {
                //blyat
            } else { //Permission is not available
                App.INSTANCE.getSharedPreferences().setFloatStat(false);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
