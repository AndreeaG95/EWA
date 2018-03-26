package com.andreea.ewa.doctor;

import android.support.annotation.NonNull;

/**
 * Created by andreeagb on 3/20/2018.
 */

public class Doctor implements Comparable<Doctor>{

    public String name, email, phone, key, request;

    public Doctor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Doctor(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getKey() {
        return key;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRequest(){
        return this.request;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Doctor))
            return false;

        return ((Doctor) o).name.equals(this.name);
    }

    @Override
    public int compareTo(@NonNull Doctor doctor) {
        return ( (this.getName()).compareToIgnoreCase(doctor.getName()));
    }
}
