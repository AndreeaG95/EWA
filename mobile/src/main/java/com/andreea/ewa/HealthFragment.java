package com.andreea.ewa;


import android.app.AlertDialog;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class HealthFragment extends Fragment implements View.OnClickListener{

    private CardView cTemperature, heartRate;
    private Button okTemp, cancelTemp;
    private EditText temperatureVal;

    // Ignore that is desprecated.
    private Camera mCamera;

    public HealthFragment() {
        // Required empty public constructor
        // Inflate the layout for this fragment
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_health, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        cTemperature = getView().findViewById(R.id.temperature);
        heartRate = getView().findViewById(R.id.heartRate);
        okTemp = getView().findViewById(R.id.okTemp);

        cTemperature.setOnClickListener(this);

        mCamera = getCameraInstance(); // Create an instance of Camera
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

    public void getTemperature(View v){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
        final View temperatureView = getLayoutInflater().inflate(R.layout.temperature_layout, null);
        mBuilder.setView(temperatureView);
        final AlertDialog dialog = mBuilder.create();

        cancelTemp = temperatureView.findViewById(R.id.cancelTemp);
        temperatureVal = temperatureView.findViewById(R.id.eTemp);
        okTemp = temperatureView.findViewById(R.id.okTemp);
        dialog.show();

        cancelTemp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        okTemp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String user = AppState.get().getUserId();
                double temp = Double.valueOf(temperatureVal.getText().toString());
                //Adding values
                DatabaseReference newRef = AppState.get().getDatabaseReference().child("Patients").child(user).child("Temperature").child(getCurrentTimeDate());
                newRef.setValue(temp);
                dialog.cancel();
            }
        });
    }

    public static String getCurrentTimeDate() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdfDate.format(now);
    }


    private void getDoctor(View view){
        if(!MainActivity.haveNetworkConnection()){
            Toast.makeText(view.getContext(), "Internet connection required", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.temperature:
                getTemperature(v);
                break;
            case R.id.heartRate:
                break;
            case R.id.doctor:
                getDoctor(v);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //releaseCamera();
    }

}
