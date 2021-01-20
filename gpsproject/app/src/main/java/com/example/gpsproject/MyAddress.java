package com.example.gpsproject;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class MyAddress extends Application {

    private static MyAddress singleton;

    //유지 관리할 위치 주소소
   private List<Location> mylocations;

    public MyAddress getInstance(){
        return singleton;
    }

    public void onCreate(){
        super.onCreate();
        singleton = this;
        mylocations = new ArrayList<>();
    }

    public List<Location> getMylocations() {
        return mylocations;
    }

    public void setMylocations(List<Location> mylocations) {
        this.mylocations = mylocations;
    }
}

