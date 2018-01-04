package com.andreea.ewa;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    // UI.
    private TextView mTextView;
    
    // Heart rate sensors.
    SensorManager mSensorManager;
    private Sensor mHeartRateSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();
        
        startHeartMonitor();
    }
    
    private void startHeartMonitor(){
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        // Load the same sensor as in Samsung Gear Live that produces an accuracy of 3.
        mHeartRateSensor = mSensorManager.getDefaultSensor(65562);

    }

    protected void onStart() {
        super.onStart();

        // Register a listener for sensor data.
        mSensorManager.registerListener((SensorEventListener) this, this.mHeartRateSensor, 3);
    }

    //@Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("55", "sensor event: " + sensorEvent.accuracy + " = " + sensorEvent.values[0]);
    }

    //@Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d("66", "accuracy changed: " + i);
    }

}
