package com.andreea.ewa;


/**
 * Created by andreeagb on 1/10/2018.
 */

public class Temperature {

    private String date;
    private Double value;

    public Temperature(String date, Double value){
        this.date = date;
        this.value = value;
    }

    public String getDate(){
        return date;
    }

    public double getValue(){
        return value;
    }
}
