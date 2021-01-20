package com.example.gpsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class GpsTrackActivity extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSIONS_FINE_LOCATION = 99;

    TextView tv_lat, tv_lon, tv_accuracy, tv_speed, tv_sensor, tv_altitude, tv_updates, tv_address, tv_wayPointCounts;
    Switch sw_locationsupdates, sw_gps;
    Button btnNewWayPoint, btnShowWayPointList, btnShowMap;
    //추적하는지 안하는지 기억하는 변수수
    boolean updateOn = false;

    Location currentLocation;//현재 위치
    List<Location> savedLocations;//저장된 위치

    //구글 위치기반 서비스!!
    FusedLocationProviderClient fusedLocationProviderClient;

    LocationRequest locationRequest;
    LocationCallback locationCallBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_track);

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);
        btnShowWayPointList = findViewById(R.id.btnShowWayPointList);
        btnNewWayPoint = findViewById(R.id.btnNewWayPoint);
        tv_wayPointCounts = findViewById(R.id.tv_wayPointCounts);
        btnShowMap = findViewById(R.id.btnShowMap);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);//위치 체크 주기
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);//위치 체크 주기를 얼마나 빠르게 업데이트 할거냐?

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //위치 저장
                Location location = locationResult.getLastLocation();
                updateUIValues(location);
            }
        };

        btnNewWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the gps location

                // add the new location to the global list;
                MyAddress myAddress = (MyAddress) getApplicationContext();
                savedLocations = myAddress.getMylocations();
                savedLocations.add(currentLocation);
            }
        });

        btnShowWayPointList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GpsTrackActivity.this, ShowSavedLocationsList.class);
                startActivity(intent);
            }
        });

        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GpsTrackActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_gps.isChecked()) {
                    //most accuurate - use GPS
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("GPS 센서 사용");
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("기지국 + WIFI 사용");
                }
            }
        });

        sw_locationsupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_locationsupdates.isChecked()) {
                    //위치 추적 시작
                    startLocationUpdates();
                } else {
                    //위치 추적 끔
                    stopLocationUpdates();
                }
            }
        });


        updateGPS();

    }//onCreate

    private void stopLocationUpdates() {
        tv_updates.setText("추적 중지");
        tv_lat.setText("추정 중지");
        tv_lon.setText("추정 중지");
        tv_speed.setText("추정 중지");
        tv_address.setText("추정 중지");
        tv_accuracy.setText("추정 중지");
        tv_altitude.setText("추정 중지");
        tv_sensor.setText("추정 중지");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }

    private void startLocationUpdates() {
        tv_updates.setText("추적 시작");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                updateGPS();
            }else {
                Toast.makeText(this, "권한 허가좀요", Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
        }
    }

    private void updateGPS(){
        //get Permissions from the user to track GPS
        //get the current location from the fused client
        //update the UI - i.e. set all properties in their associated text view items
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GpsTrackActivity.this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //we got permissions. put the values of location.
                    updateUIValues(location);
                    currentLocation = location;
                }
            });
        } else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private void updateUIValues(Location location) {
        //update all of the text view objects with a new location
        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));

        if(location.hasAltitude()){
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        }else {
            tv_altitude.setText("사용 불가");
        }

        if(location.hasSpeed()){
            tv_speed.setText(String.valueOf(location.getSpeed()));
        }else {
            tv_speed.setText("사용 불가");
        }

        Geocoder geocoder = new Geocoder(GpsTrackActivity.this);

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            tv_address.setText(addresses.get(0).getAddressLine(0));
        } catch (Exception e) {
            e.printStackTrace();
            tv_address.setText("주소정보를 얻어오지 못했습니다.");

        }

        MyAddress myAddress = (MyAddress) getApplicationContext();
        savedLocations = myAddress.getMylocations();

        //show the number of waypoints saved.
        tv_wayPointCounts.setText(Integer.toString(savedLocations.size()));




    }


}