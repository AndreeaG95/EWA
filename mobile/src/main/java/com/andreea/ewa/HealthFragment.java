package com.andreea.ewa;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.andreea.ewa.healthPage.DoctorsActivity;
import com.andreea.ewa.healthPage.MedicineActivity;
import com.andreea.ewa.heartRate.HeartRateMenu;
import com.andreea.ewa.heartRate.HeartRateMonitor;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthFragment extends Fragment implements View.OnClickListener{

    private CardView cTemperature, cHeartRate, cMedicine, cDoctors;
    private Button okTemp, cancelTemp;
    private EditText temperatureVal;
    private View mLayout;

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
        cMedicine = getView().findViewById(R.id.medicine);
        cDoctors = getView().findViewById(R.id.doctorCard);
        cHeartRate = getView().findViewById(R.id.heartRate);

        okTemp = getView().findViewById(R.id.okTemp);

        cTemperature.setOnClickListener(this);
        cMedicine.setOnClickListener(this);
        cDoctors.setOnClickListener(this);
        cHeartRate.setOnClickListener(this);

        mCamera = getCameraInstance(); // Create an instance of Camera
        mLayout =  getView().findViewById(R.id.health_layout);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.temperature:
                getTemperature(v);
                break;
            case R.id.heartRate:
                getCameraPermissions();
                Intent intentHeart = new Intent(getContext(), HeartRateMenu.class);
                this.startActivity(intentHeart);
                break;
            case R.id.medicine:
                Intent intent = new Intent(getContext(), MedicineActivity.class);
                this.startActivity(intent);
                break;
            case R.id.doctorCard:
                Intent intentDoctor = new Intent(getContext(), DoctorsActivity.class);
                this.startActivity(intentDoctor);
                break;
        }
    }

    // Get camera permissions.
    private void getCameraPermissions(){
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Snackbar.make(mLayout,
                    "Camera permission available",
                    Snackbar.LENGTH_SHORT).show();
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }
    }

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    /**
     * Requests the {@link android.Manifest.permission#CAMERA} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
            Snackbar.make(mLayout, "Camera access required",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CAMERA);
                }
            }).show();

        } else {
            Snackbar.make(mLayout, "Camera unavailable", Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //releaseCamera();
    }

}
