package com.andreea.ewa;


import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class HealthFragment extends Fragment implements View.OnClickListener{

    private CardView temperature, heartRate;

    // Ignore that is desprecated.
    private Camera mCamera;

    public HealthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // temperature = getView().findViewById(R.id.temperature);
        //heartRate = getView().findViewById(R.id.heartRate);

        mCamera = getCameraInstance(); // Create an instance of Camera
        return inflater.inflate(R.layout.fragment_health, container, false);
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private void getTemperature(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.temperature:
                getTemperature();
                break;
            case R.id.heartRate:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //releaseCamera();
    }

}
