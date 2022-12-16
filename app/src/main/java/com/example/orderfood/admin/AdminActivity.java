package com.example.orderfood.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.orderfood.MainActivity;
import com.example.orderfood.R;
import com.example.orderfood.activities.LoginActivity;

public class AdminActivity extends AppCompatActivity {

    TextView mon,cuahang,donhang,loaimon,khachhang,doanhthu,dangxuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mon = findViewById(R.id.admin_mon);
        cuahang = findViewById(R.id.admin_cuahang);
        donhang = findViewById(R.id.admin_donhang);
        loaimon = findViewById(R.id.admin_loaimon);
        khachhang = findViewById(R.id.admin_khach_hang);
        doanhthu = findViewById(R.id.admin_doanhthu);
        dangxuat = findViewById(R.id.admin_dangxuat);

        Intent intent = this.getIntent();
        String userInfo = intent.getStringExtra("userName");
        String userPhone = intent.getStringExtra("userInfo");

        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,MonActivity.class);
                startActivity(intent);
            }
        });

        donhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,DonHangActivity.class);
                startActivity(intent);
            }
        });

        cuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                intent.putExtra("userInfo",userPhone);
                intent.putExtra("userName",userInfo);
                startActivity(intent);
            }
        });

        loaimon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,LoaiMonActivity.class);
                startActivity(intent);
            }
        });
        khachhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,KhachHangActivity.class);
                startActivity(intent);
            }
        });

        doanhthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,DoanhThuActivity.class);
                startActivity(intent);
            }
        });

        dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,LoginActivity.class);
                SharedPreferences sharedPreferences = getSharedPreferences("USER_FILE.txt",MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                startActivity(intent);
                finish();
            }
        });
    }
}