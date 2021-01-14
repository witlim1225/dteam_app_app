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

    Button btnGroupTalk, btnAddPerson;
    Button btnLogout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        /*btnRevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               revokeAccess();
            }
        }); 왜 작동되는지 모름*/
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

   /* //구글 회원탈퇴-이건 무슨작동인지 모르겠음.
    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        // Google revoke access
        //mGoogleSignInClient.revokeAccess();
    }*/


}//MainActivity