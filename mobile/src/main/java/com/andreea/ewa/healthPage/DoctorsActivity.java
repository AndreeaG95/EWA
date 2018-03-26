package com.andreea.ewa.healthPage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;

import com.andreea.ewa.doctor.Doctor;
import com.andreea.ewa.doctor.DoctorAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.andreea.ewa.R;

/**
 * Created by andreeagb on 3/20/2018.
 */

public class DoctorsActivity extends AppCompatActivity {

    private ListView listDoctors;
    private List<Doctor> doctors = new ArrayList<>();
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctors);

        listDoctors = (ListView) findViewById(R.id.listDoctors);

        final DoctorAdapter adapter = new DoctorAdapter(this, R.layout.item_doctor, doctors);
        listDoctors.setAdapter(adapter);

        // Setup Firebase.
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        databaseReference.child("Doctors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("UPD", "onChildAdded: " + s + "  " + dataSnapshot.getKey());
                try {
                    Doctor doctor = dataSnapshot.getValue(Doctor.class);
                    if (doctor != null) {
                        doctor.setRequest("not_registered");
                        doctor.key = dataSnapshot.getKey();
                        doctors.add(doctor);
                        Collections.sort(doctors);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                try {
                    Doctor doctor = dataSnapshot.getValue(Doctor.class);
                    if (doctor != null) {
                        //doctor.name = dataSnapshot.getKey();
                        doctors.remove(doctor);
                        doctors.add(doctor);
                        Collections.sort(doctors);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
