package edu.sjsu.android.accelerometer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

public class SimulationView extends View implements SensorEventListener {

    private static final int BALL_SIZE = 64; // Given
    private static final int BASKET_SIZE = 80; // Given
    Particle mBall = new Particle();
    private Bitmap mField; // Given
    private Bitmap mBasket; // Given
    private float mXOrigin = 0; // Given: initialize with 0
    private float mYOrigin = 0; // Given: initialize with 0
    private float mHorizontalBound = 0; // Given: initialize with 0
    private float mVerticalBound = 0; // Given: initialize with 0
    private SensorManager sensorManager; // Define SensorManager, Sensor, and Display
    private Display mDisplay; // Define SensorManager, Sensor, and Display
    private Sensor sensor; // Define SensorManager, Sensor, and Display
    private float mSensorX; // Implied from given code
    private float mSensorY; // Implied from given code
    private float mSensorZ; // Implied from given code
    private long mSensorTimeStamp; // Implied from given code
    private Bitmap mBitMAP; // Implied

    public SimulationView(Context context) {
        super(context);
        // Initialize images from drawable
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        mBitMAP = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);
        Bitmap basket = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
        mBasket = Bitmap.createScaledBitmap(basket, BASKET_SIZE, BASKET_SIZE, true);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        mField = BitmapFactory.decodeResource(getResources(), R.drawable.field, opts);

        // Add the following lines of code in the SimulationView constructor to initialize display.
        WindowManager mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        // Also add appropriate code to initialize sensor as you learned in the lab assignment.
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        startSimulation(); // Registers listener
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mXOrigin = w * 0.5f;
        mYOrigin = h * 0.5f;
        mHorizontalBound = (w - BALL_SIZE) * 0.5f;
        mVerticalBound = (h - BALL_SIZE) * 0.5f;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Implement onSensorChanged() method of SensorEventListener in the SimulationView class
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) { // Check if correct Sensor
            // To do so, get the acceleration values along the three axis from event.values array and timestamp of the data from event.timestamp
            mSensorZ = event.values[2]; // Get the acceleration values along the z-axis from event.values array
            mSensorTimeStamp = event.timestamp; // Get timestamp of the data from event.timestamp
            // Get the acceleration values along the x-axis and y-axis from event.values array (depending on rotation)
            switch (mDisplay.getRotation()) {
                case Surface.ROTATION_0:
                    mSensorX = event.values[0];
                    mSensorY = event.values[1];
                    break;
                case Surface.ROTATION_90:
                    mSensorX = -event.values[1];
                    mSensorY = event.values[0];
                    break;
                case Surface.ROTATION_180:
                    mSensorX = -event.values[0];
                    mSensorY = -event.values[1];
                    break;
                case Surface.ROTATION_270:
                    mSensorX = event.values[1];
                    mSensorY = -event.values[0];
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void startSimulation() {
        // Rate suitable for games: SENSOR_DELAY_GAME: https://developer.android.com/reference/android/hardware/SensorManager#SENSOR_DELAY_FASTEST
        // Can use: SENSOR_DELAY_UI, SENSOR_DELAY_NORMAL, SENSOR_DELAY_GAME (I prefer to use game because it's faster)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_GAME);
        // sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopSimulation() {
        sensorManager.unregisterListener(this);
    }

    // ######################### Given #########################
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mField, 0, 0, null);
        canvas.drawBitmap(mBasket, mXOrigin - BASKET_SIZE / 2, mYOrigin - BASKET_SIZE / 2, null);
        mBall.updatePosition(mSensorX, mSensorY, mSensorZ, mSensorTimeStamp);
        mBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);
        canvas.drawBitmap(mBitMAP,
                (mXOrigin - BALL_SIZE / 2) + mBall.mPosX,
                (mYOrigin - BALL_SIZE / 2) - mBall.mPosY, null);
        invalidate();
    }
}