package com.andreea.ewa.healthPage;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.andreea.ewa.AppState;
import com.andreea.ewa.MainActivity;
import com.andreea.ewa.R;
import com.andreea.ewa.medicine.Medicine;
import com.andreea.ewa.medicine.MedicineAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicineActivity extends AppCompatActivity {

    private FloatingActionButton fabAdd;
    private ListView listMedicine;
    private List<Medicine> medicines = new ArrayList<>();
    private DatabaseReference databaseReference;

    private List<AlarmManager> alarmManagers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_medicine);

        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        listMedicine = (ListView) findViewById(R.id.listMedicine);

        final MedicineAdapter adapter = new MedicineAdapter(this, R.layout.item_medicine, medicines);
        listMedicine.setAdapter(adapter);

        // setup firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        databaseReference.child("Patients").child(AppState.getUserId()).child("Medicine").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("UPD", "onChildAdded: "+ s +"  " + dataSnapshot.getKey());
                try {
                    Medicine medicine = dataSnapshot.getValue(Medicine.class);
                    if (medicine != null) {
                        medicine.name = dataSnapshot.getKey();
                        medicines.add(medicine);
                        Collections.sort(medicines);
                        adapter.notifyDataSetChanged();

                    }
                }
                catch (Exception e) {

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                try {
                    Medicine medicine = dataSnapshot.getValue(Medicine.class);
                    if (medicine != null) {
                        medicine.name = dataSnapshot.getKey();
                        medicines.remove(medicine);
                        medicines.add(medicine);
                        Collections.sort(medicines);
                        adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e) {

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

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public void getMedicine(View v){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
        final View temperatureView = getLayoutInflater().inflate(R.layout.add_medicine_layout, null);
        mBuilder.setView(temperatureView);
        final AlertDialog dialog = mBuilder.create();

        Button cancelTemp = temperatureView.findViewById(R.id.cancelTemp);
        final EditText eName = temperatureView.findViewById(R.id.eName);
        final EditText eLast = temperatureView.findViewById(R.id.eTime);
        final EditText eInterval = temperatureView.findViewById(R.id.eInterval);
        Button okTemp = temperatureView.findViewById(R.id.okTemp);
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
                double interval = Double.valueOf(eInterval.getText().toString());
                String name = eName.getText().toString();
                long now = new Date().getTime();

                //Adding values
                Map<String, Object> values = new HashMap<>();
                values.put("last_ts", now);
                values.put("interval", interval);
                values.put("type", "default");

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/Patients/" + AppState.getUserId() +"/Medicine/" + name, values);

                alarmMgr = (AlarmManager)v.getContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(v.getContext(), AlarmReceiver.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                alarmIntent = PendingIntent.getActivity(v.getContext(), 0, intent, 0);

                int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
                final int FIFTEEN_SEC_MILLIS = 30000;

                alarmMgr.setRepeating(alarmType, SystemClock.elapsedRealtime() + FIFTEEN_SEC_MILLIS,
                        FIFTEEN_SEC_MILLIS, alarmIntent);

                Log.i("RepeatingAlarmFragment", "Alarm set.");

                databaseReference.updateChildren(childUpdates);
                dialog.cancel();

            }
        });

    }

    // Place where we build our notification.
    public class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Log.i("RepeatingAlarmFragment", "Alarm received.");

            Intent notificationClicked = new Intent(context, MedicineActivity.class);
            notificationClicked.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

    }

    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.fabAdd:
                getMedicine(view);
                break;
        }
    }

}
