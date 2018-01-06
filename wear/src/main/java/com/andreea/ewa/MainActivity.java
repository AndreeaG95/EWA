package com.andreea.ewa;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.wearable.Wearable;

import java.nio.ByteBuffer;
import java.util.Random;

public class MainActivity extends WearableActivity implements SensorEventListener {

    // UI.
    private TextView mTextView;
    private Button bStart;
    
    // Heart rate sensors.
    SensorManager mSensorManager;
    private Sensor mHeartRateSensor;
    private Integer computedHeartRate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_square);

        mTextView = (TextView) findViewById(R.id.tHeartRate);
        bStart = (Button) findViewById(R.id.bStart);

        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( bStart.getText().equals("Start")) {
                    bStart.setText(R.string.stop);
                    mTextView.setText("Monitor starting...");
                    startHeartMonitor();
                }else{
                    bStart.setText(R.string.start);
                    mTextView.setText("Monitor shutdown...");
                    stopHeartMonitor();
                }
            }
        });

        // Enables Always-on
        setAmbientEnabled();
    }
    
    private void startHeartMonitor(){
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        // Load the same sensor as in Samsung Gear Live that produces an accuracy of 3.
        //mHeartRateSensor = mSensorManager.getDefaultSensor(65562);
        boolean sensorRegistered = false;

        if (mSensorManager != null) {
            if (mHeartRateSensor != null) {
                sensorRegistered = mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                computedHeartRate = (new Random()).nextInt(240);
                mTextView.setText("Mock value:" + computedHeartRate);
               // sendHeartRateData(computedHeartRate.intValue());
            }
            //sendHeartRateStatus(true);
        }

        Log.d("SensorStatus", " Sensor registered: " + (sensorRegistered ? "yes" : "no"));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float mHeartRateFloat = sensorEvent.values[0];

        int mHeartRate = Math.round(mHeartRateFloat);

        mTextView.setText(Integer.toString(mHeartRate));
        // Send data to phone.
      //  sendHeartRateData(computedHeartRate.intValue());

        Log.d("SensorChaged", "sensor event: " + sensorEvent.accuracy + " = " + mHeartRate);
    }

    // Do something if sensor accuracy changes.
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("onAccuracyChanged", "accuracy changed: " + accuracy);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void stopHeartMonitor(){
        mSensorManager.unregisterListener(this);
    }


    /*
        Send the heart rate values to phone.
     */
    /*
    private void sendHeartRateData(int data) {
        if (hostNodeId != null) {
            Log.d("Send","Data: "+data);
            ByteBuffer b = ByteBuffer.allocate(4);
            b.putInt(data);
            Wearable.MessageApi.sendMessage(mGoogleApiClient, hostNodeId,
                    MESSAGE_PATH_SHOW_HEART_RATE_DATA, b.array());
        }
    }

    private void sendHeartRateStatus(boolean data) {
        if (hostNodeId != null) {
            Log.d("Send","Status: "+data);
            ByteBuffer b = ByteBuffer.allocate(1);
            b.put((byte) ((data)?1:0));
            Wearable.MessageApi.sendMessage(mGoogleApiClient, hostNodeId,
                    MESSAGE_PATH_SHOW_HEART_RATE_STATUS, b.array());
        }
    }
    */

}
