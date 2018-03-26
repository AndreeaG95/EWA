package com.andreea.ewa;


import java.io.Serializable;

/**
 * Created by andreeagb on 1/11/2018.
 */

public class UserAccount implements Serializable{
    private String name, birthday, gender;
    private int phone, weight, height;

    public UserAccount(){

    }

    public UserAccount(String name, String birthday, String gender, int phone, int weight, int height) {
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.phone = phone;
        this.weight = weight;
        this.height = height;
    }


    public int getHeight() {
        return height;
    }

    public int getPhone() {
        return phone;
    }

    public int getWeight() {
        return weight;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

}
