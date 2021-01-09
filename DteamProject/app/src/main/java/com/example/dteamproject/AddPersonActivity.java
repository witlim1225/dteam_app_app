package com.example.dteamproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddPersonActivity extends AppCompatActivity {

    Button btnParents, btnKid, connWear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        btnParents = findViewById(R.id.btnParents);//부모 추가버튼
        btnKid = findViewById(R.id.btnKid);//아이 추가버튼
        connWear = findViewById(R.id.connWear);//웨어러블 연결


        //부모 추가 버튼 클릭시
        btnParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //아이 추가 버튼 클릭시
        btnKid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //웨어러블 연결 클릭시
        connWear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}