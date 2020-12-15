package edu.sjsu.android.accelerometer;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

// ######################### Given #########################
public class MainActivity extends Activity {
    private static final String TAG = "edu.sjsu.android.accelerometer:MainActivity";
    private PowerManager.WakeLock mWakeLock;
    // The view
    private SimulationView mSimulationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, TAG);
        mSimulationView = new SimulationView(this);
        // Set to the simulation view instead of layout file.
        setContentView(mSimulationView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // acquire wakelock
        mWakeLock.acquire();
        // start simulation to register the listener
        mSimulationView.startSimulation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Release wakelock
        mWakeLock.release();
        // stop simulation to unregister the listener
        mSimulationView.stopSimulation();
    }
}

// Ball image: https://bloximages.newyork1.vip.townnews.com/swnewsmedia.com/content/tncms/assets/v3/editorial/0/f6/0f69af9a-968e-5723-b868-ac1412842cf3/58b4414eb0a84.image.png?resize=400%2C319
// Field: https://image.freepik.com/free-vector/basketball-court-floor-with-line-pattern-wood-background_64749-1783.jpg
// Basket: https://www.spalding.com/dw/image/v2/ABAH_PRD/on/demandware.static/-/Sites-masterCatalog_SPALDING/default/dwdf6cba47/images/hi-res/411527_FRONT.jpg?sw=555&sh=689&sm=cut&sfrm=jpg
