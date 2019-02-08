package com.nanowheel.nanoux.nanowheel;

import android.app.Application;
//import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.PowerManager;

//import com.facebook.stetho.Stetho;
import com.nanowheel.nanoux.nanowheel.util.SharedPreferencesUtil;

//import net.kwatts.powtools.database.DBExecutor;
//import net.kwatts.powtools.database.Database;
//import net.kwatts.powtools.util.SharedPreferencesUtil;

import java.util.concurrent.ExecutorService;

//import io.palaima.debugdrawer.timber.data.LumberYard;
//import timber.log.Timber;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app.
 */
public class App extends Application {

    public static App INSTANCE = null;
    private SharedPreferencesUtil sharedPreferencesUtil = null;
    PowerManager.WakeLock wakeLock;
    //public Database db;
    public ExecutorService dbExecutor;


    public App() {
        INSTANCE = this;
    }

    public SharedPreferencesUtil getSharedPreferences() {
        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = new SharedPreferencesUtil(App.this);
        }
        return sharedPreferencesUtil;
    }

    public SharedPreferencesUtil getSharedPreferences(Context context) {
        if (sharedPreferencesUtil == null) {
            sharedPreferencesUtil = new SharedPreferencesUtil(context);
        }
        return sharedPreferencesUtil;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG || getSharedPreferences().isDebugging()) {
            /*Stetho.initializeWithDefaults(this);
            LumberYard lumberYard = LumberYard.getInstance(this);
            lumberYard.cleanUp();
            Timber.plant(lumberYard.tree());
            Timber.plant(new Timber.DebugTree());*/
        }
        initWakeLock();
        initDatabase();
    }

    private void initDatabase() {
        /*db = Room.databaseBuilder(getApplicationContext(),
                Database.class, "database-name-pow")
                .fallbackToDestructiveMigration()
                .build();
        dbExecutor = Executors.newSingleThreadExecutor();*/
    }

    /*public static void dbExecute(DBExecutor dbExecutor) {
        INSTANCE.dbExecutor.execute( () -> dbExecutor.run(INSTANCE.db));
    }*/

    private void initWakeLock() {
        //PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        //wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "pOWToolsWakeLock");
    }

    public void acquireWakeLock() {
        wakeLock.acquire();
    }

    public void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }
}
