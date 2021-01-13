package com.example.dteamproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//loginDTO가져온다.
import static com.example.dteamproject.LoginActivity.loginDTO;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button btnGroupTalk, btnAddPerson;
    Button btnLogout, btnRevoke;
    FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRevoke = findViewById(R.id.btnRevoke);
        btnLogout = findViewById(R.id.btnLogout);
        btnGroupTalk = findViewById(R.id.btnGroupTalk);
        btnAddPerson = findViewById(R.id.btnAddPerson);
        mAuth = FirebaseAuth.getInstance();

        btnGroupTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GtalkActivity2.class);
                startActivity(intent);
            }
        });

        btnAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPersonActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        btnRevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               revokeAccess();
            }
        });
        /*//네비게이션 바
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navi_drawer_open, R.string.navi_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);//onNavigationItemSelected()로 가게 만듬.

        //헤드드로어에 로그인정보 표시하기
        int userlevel = 1; // 0:일반유저, 1:관리자
        View headerView = navigationView.getHeaderView(0);
        ImageView imageView = headerView.findViewById(R.id.loginImage);
        //imageView.setImageResource(R.drawable.su);
        //Glide를 이용해서 imageview를 동그랗게(circleCrop()) 만듬.
        Glide.with(this).load(R.drawable.ic_launcher_background).circleCrop().into(imageView);

        if(userlevel == 1){
            navigationView.getMenu().findItem(R.id.communi).setVisible(true);
        }

        TextView navLoginId = headerView.findViewById(R.id.loginId);
        navLoginId.setText("반갑습니다. : " + loginDTO.getId() + "님");

        TextView navLoginStr = headerView.findViewById(R.id.loginStr);
        navLoginStr.setText(loginDTO.getEmail());
*/



    }

    //구글 로그아웃
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        // Google sign out
        //mGoogleSignInClient.signOut();
    }

    //구글 회원탈퇴
    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        // Google revoke access
        //mGoogleSignInClient.revokeAccess();
    }


}
    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_notice){
            Toast.makeText(this, "첫번째 메뉴 선택", Toast.LENGTH_SHORT).show();
            onFragmentSelected(0, null);
        }else if(id == R.id.nav_FAQ){
            Toast.makeText(this, "두번째 메뉴 선택", Toast.LENGTH_SHORT).show();
            onFragmentSelected(1, null);
        }else if(id == R.id.nav_placeAlarm){
            Toast.makeText(this, "세번째 메뉴 선택", Toast.LENGTH_SHORT).show();
            onFragmentSelected(2, null);
        }else if(id == R.id.nav_EquestActivity){
            Toast.makeText(this, "네번째 메뉴 선택", Toast.LENGTH_SHORT).show();
            onFragmentSelected(3, null);
        }else if(id == R.id.nav_settings){
            Toast.makeText(this, "다섯번째 메뉴 선택", Toast.LENGTH_SHORT).show();
            onFragmentSelected(4, null);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }*//*

    public void onFragmentSelected(int position, Bundle bundle){
        *//*Fragment curFragment = null;
        if(position == 0){
            curFragment = fragment1;
            toolbar.setTitle("첫번째 화면");
        }else if(position == 1){
            curFragment = fragment2;
            toolbar.setTitle("두번째 화면");
        }else if(position == 2){
            curFragment = fragment3;
            toolbar.setTitle("세번째 화면");
        }else if(position == 3){
            curFragment = fragment1;
            toolbar.setTitle("네번째 화면");
        }else if(position == 4){
            curFragment = fragment2;
            toolbar.setTitle("다섯번째 화면");
        }else if(position == 5){
            curFragment = fragment2;
            toolbar.setTitle("여섯번째 화면");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container, curFragment).commit();*//*
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }


    }*/
