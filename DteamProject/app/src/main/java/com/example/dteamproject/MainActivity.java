package com.example.dteamproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;


//loginDTO가져온다.
import static com.example.dteamproject.LoginActivity.loginDTO;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "main:";

    //구글맵
    SupportMapFragment mapFragment;
    GoogleMap map;
    MarkerOptions myMarker;

    Button btnGroupTalk, btnAddPerson;
    Button btnLogout;
    Button btnMyLoc, btnSetLoc;
    Button btnAddPerson1, btnAddPerson2, btnAddPerson3;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btnLogout);
        btnGroupTalk = findViewById(R.id.btnGroupTalk);
        btnAddPerson = findViewById(R.id.btnAddPerson);

        btnMyLoc = findViewById(R.id.btnMyLoc);
        btnSetLoc = findViewById(R.id.btnSetLoc);

        btnAddPerson1 = findViewById(R.id.btnAddPerson1);
        btnAddPerson2 = findViewById(R.id.btnAddPerson2);
        btnAddPerson3 = findViewById(R.id.btnAddPerson3);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: 구글 맵");

                map = googleMap;

                try {
                   map.setMyLocationEnabled(true);
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }
        });

        //지도 초기화
        MapsInitializer.initialize(this);

        btnMyLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMyLocation();
            }
        });

        //구글 로그아웃을 위한 인스턴스 받아오기
        mAuth = FirebaseAuth.getInstance();

        //그룹톡창으로 이동
        btnGroupTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GtalkActivity2.class);
                startActivity(intent);
            }
        });

        //친구추가창으로 이동
        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPersonActivity.class);
                startActivity(intent);
            }
        });


        //구글 로그아웃
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { signOut(); }
        });

    }

    //구글 로그아웃-이건 왜 되는것처럼 보이는가?(구글 뿐만 아니라 일반 로그인도 로그아웃 시킴)
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        // Google sign out
        //mGoogleSignInClient.signOut();
    }

    //구글 맵 내 위치 찾기
    private void requestMyLocation() {
        LocationManager manager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            long minTime = 10000;
            float minDistance = 0;

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            showCurrentLocation(location);
                        }
                    }
            );

           /*필요한거 쓰면됨 (wifi는 network, lte는 gps)
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            showCurrentLocation(location);
                        }
                    }
            );*/

            Location lastLocation =
                    manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(lastLocation != null){
                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();

                String msg = "Latitude : " + latitude
                        + "\nLongitude : " + longitude;
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        }catch (SecurityException e){
            e.getMessage();
        }

    }

    private void showCurrentLocation(Location location) {
        //지도에 표시할 때 필요한 객체
        LatLng curPoint =
                new LatLng(location.getLatitude(), location.getLongitude());
        String msg = "Latitude : " + curPoint.latitude
                + "\nLongitude : " + curPoint.longitude;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        //지도에 표시할 때 필요함
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        //마커 찍기
        Location targetLocation = new Location("");
        targetLocation.setLatitude(35.153817);
        targetLocation.setLongitude(126.8889);
        showMyMarker(targetLocation);

    }

    private void showMyMarker(Location location){
        if(myMarker == null){
            myMarker = new MarkerOptions();
            myMarker.position(
                    new LatLng(location.getLatitude(), location.getLongitude()));
            myMarker.title("♨ 내 위치\n");
            myMarker.snippet("여기가 어디인가");//간단하게 설명해주는 것
            myMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));
            map.addMarker(myMarker);
        }
    }

}//MainActivity