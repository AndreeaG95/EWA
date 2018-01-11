package com.andreea.ewa.medicine;

/**
 * Created by Andrei on 1/11/2018.
 */

public class Medicine implements Comparable<Medicine>{

    public long last_ts;
    public double interval;
    public String name;
    public String type;

    public Medicine() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Medicine(long last_ts, String name, double interval, String type) {
        this.last_ts = last_ts;
        this.name = name;
        this.interval = interval;
        this.type = type;
    }

    public long getLast() { return last_ts;}

    public long getNext() {
        return last_ts + (long)(interval * 3600 * 1000);
    }

    public String getName() {return name;}

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Medicine))
            return false;

        if (((Medicine) o).name.equals(this.name))
            return true;
        return false;
    }

    @Override
    public int compareTo(Medicine medicine) {
        return (int)(this.getNext()-medicine.getNext());
    }

}
