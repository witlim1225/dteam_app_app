package com.example.gpsproject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "main:";

    Button btnGroupTalk, btnAddPerson;
    Button btnLogout;
    Button btnMyLoc, btnSetLoc;
    Button btnAddPerson1, btnAddPerson2, btnAddPerson3;

    //구글 로그인 결과값 가져오기
    TextView tv_result;
    ImageView tv_profile;

    //구글 맵
    SupportMapFragment mapfragment;
    GoogleMap map;
    MarkerOptions myMarker;


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

        //구글 맵
        mapfragment = (SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.googleMap);
        mapfragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                try {
                    map.setMyLocationEnabled(true);
                } catch (SecurityException e){
                    e.printStackTrace();
                    Log.d(TAG, "setMyLocationEnable exception");
                }
            }
        });

        MapsInitializer.initialize(this);

        //내위치 찾기.
        btnMyLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { requestMyLocation(); }
        });


        //구글 putExtra로 받은 것들 받을 준비 예시
        Intent intent = getIntent();
        String nickName = intent.getStringExtra("nickName");    //LoginActivity로부터 닉네임 전달받음.
        String photoUrl = intent.getStringExtra("photoUrl");    //LoginActivity로부터 프로필 사진 Url 전달받음.

        tv_result = findViewById(R.id.tv_result);
        tv_result.setText(nickName);    //닉네임 text를 텍스트 뷰에 세팅

        tv_profile = findViewById(R.id.tv_profile);
        Glide.with(this).load(photoUrl).into(tv_profile); //프로필 url을 이미지 뷰에 세팅

        //로그아웃
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });




        //그룹톡창으로 이동
        btnGroupTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //친구추가창으로 이동
        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }//onCreate()

    private void requestMyLocation() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            long minTime = 5000;
            float mindistance = 0;

            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    mindistance,
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