package com.carsharing.antisergiu.main;

import java.util.ArrayList;


public class User {
    private String mName;
    private String mPhoneNumber;
    private String mPassword;
    private static final String URL= "http://10.0.2.2/ANDROID/insertPerson.php";
    public User(String name, String phoneNumber, String password){
        mName = name;
        mPhoneNumber = phoneNumber;
        mPassword = password;
    }

    public String getName() {
        return mName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getPassword() {
        return mPassword;
    }


}
