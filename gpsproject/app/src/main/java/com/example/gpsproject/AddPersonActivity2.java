package com.example.gpsproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AddPersonActivity2 extends AppCompatActivity {
    ImageView myQrImg; // 내 QR 코드
    TextView myNum, personNum; // 내 번호, 상대방번호
    Button btnQrAdd, btnPersonAdd; // 상대방 QR코드, 추가하기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person2);


        myQrImg = findViewById(R.id.myQrImg);
        myNum = findViewById(R.id.myNum);
        personNum = findViewById(R.id.personNum);
        btnQrAdd = findViewById(R.id.btnQrAdd);
        btnPersonAdd = findViewById(R.id.btnPersonAdd);

        btnQrAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLink();
            }
        });
    }

    public void createLink(){
        Log.e("add_person", "create link");
    }
}