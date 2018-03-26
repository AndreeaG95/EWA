package com.andreea.ewa.doctor;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreea.ewa.AppState;
import com.andreea.ewa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by andreeagb on 3/20/2018.
 */

public class DoctorAdapter extends ArrayAdapter<Doctor> {

    private final Context context;
    private final List<Doctor> doctors;
    private int layoutResID;

    private String current_state;
    private DatabaseReference doctor_request;

    public DoctorAdapter(Context context, int layoutResourceID, List<Doctor> doctors) {
        super(context, layoutResourceID, doctors);

        this.context = context;
        this.doctors = doctors;
        this.layoutResID = layoutResourceID;
    }

    private static class ItemHolder {
        CircleImageView docImage;
        TextView dName;
        TextView dEmail;
        TextView dNumber;
        RelativeLayout lHeader;
        Button bRegister;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ItemHolder itemHolder;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new ItemHolder();

            view = inflater.inflate(layoutResID, parent, false);

            itemHolder.docImage = (CircleImageView) view.findViewById(R.id.doc_image);
            itemHolder.dName = (TextView) view.findViewById(R.id.dName);
            itemHolder.dEmail = (TextView) view.findViewById(R.id.dEmail);
            itemHolder.dNumber = (TextView) view.findViewById(R.id.dNumber);
            //itemHolder.lHeader = (RelativeLayout) view.findViewById(R.id.lHeader);
            itemHolder.bRegister = view.findViewById(R.id.dRegister);

            view.setTag(itemHolder);

        } else {
            itemHolder = (ItemHolder) view.getTag();
        }


        final Doctor hItem = doctors.get(position);

        itemHolder.dName.setText(hItem.getName());
        itemHolder.dEmail.setText("Email: " + hItem.getEmail());
        itemHolder.dNumber.setText("Phone:" + hItem.getPhone());

        //current_state = hItem.getRequest();

        // Database with doctor requests.
        doctor_request = AppState.get().getDatabaseReference().child("Doctor_requests");

        // Get doctor reference.
        final DatabaseReference user_reference = AppState.get().getDatabaseReference().child("Patients").child(AppState.getUserId());


        //TODO(): handle error instead on on success listener put on complete listener.
        itemHolder.bRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                itemHolder.bRegister.setEnabled(false);

                /************* NOT REGISTERED STATE ***********************/
                if(hItem.getRequest().equals("not_registered")){
                    Log.d("REQUEST", "Register");
                    doctor_request.child(AppState.getUserId()).child(hItem.getKey()).child("request_type").setValue("sent")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        doctor_request.child(hItem.getKey()).child(AppState.getUserId()).child("request_type").setValue("received")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        itemHolder.bRegister.setEnabled(true);
                                                        //current_state = "req_sent";

                                                        hItem.setRequest("req_sent");
                                                        itemHolder.bRegister.setText("Cancel request");

                                                        Toast.makeText(context, "Request sent", Toast.LENGTH_SHORT);

                                                        // TODO: This will be moved to the accept part after we have the doctors confirmation!
                                                        user_reference.child("doctor").setValue(hItem.getKey());
                                                    }
                                                });
                                    }else
                                        Toast.makeText(context, "Failed sending request", Toast.LENGTH_SHORT);
                                }
                            });
                }

                /************* CANCEL REQUEST STATE ***************/
                if(hItem.getRequest().equals("req_sent")){
                    Log.d("REQUEST", "SENT");
                    doctor_request.child(AppState.getUserId()).child(hItem.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            doctor_request.child(hItem.getKey()).child(AppState.getUserId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    itemHolder.bRegister.setEnabled(true);
                                    //current_state = "not_registered";

                                    hItem.setRequest("not_registered");
                                    itemHolder.bRegister.setText("Register");
                                    Toast.makeText(context, "Request canceled", Toast.LENGTH_SHORT);
                                }
                            });
                        }
                    });
                }

                /*********** REGISTERED TO DOCTOR STATE****************/
                if(hItem.getRequest().equals("registered")){
                    itemHolder.bRegister.setText("Unregister");
                }

            }
        });



        return view;
    }

}
