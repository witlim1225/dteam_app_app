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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "main:";

    Button btnGroupTalk, btnAddPerson;
    Button btnLogout;
    Button btnMyLoc, btnSetLoc;
    Button btnAddPerson1, btnAddPerson2, btnAddPerson3;
    Button btnGpsTrack;

    //구글 로그인 결과값 가져오기
    TextView tv_result;
    ImageView tv_profile;

    //구글 맵
    SupportMapFragment mapfragment;
    GoogleMap map;
    MarkerOptions myMarker;

    List<Location> savedLocations;

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

        btnGpsTrack = findViewById(R.id.btnGpsTrack);
        btnGpsTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GpsTrackActivity.class);
                startActivity(intent);
            }
        });

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
                Intent intent = new Intent(MainActivity.this, GtalkActivity1.class);
                startActivity(intent);
            }
        });

        //친구추가창으로 이동
        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPersonActivity1.class);
                startActivity(intent);
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

        //마커 찍기
        LatLng lastLocationPlaced = new LatLng(35.153817, 126.8889);

        for(Location loc : savedLocations){
            LatLng latLng =  new LatLng(loc.getLatitude(), loc.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Lat : " + loc.getLatitude() +" Lon : " + loc.getLongitude());
            map.addMarker(markerOptions);
            lastLocationPlaced = latLng;
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocationPlaced, 15));

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //마커가 클릭된 횟수 구하기
                Integer clicks = (Integer) marker.getTag();
                if(clicks == null){
                    clicks = 0;
                }
                clicks++;
                marker.setTag(clicks);
                Toast.makeText(MainActivity.this, "Marker : " +marker.getTitle() + " 클릭됨 " + marker.getTag() + "번", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }
}//MainActivity