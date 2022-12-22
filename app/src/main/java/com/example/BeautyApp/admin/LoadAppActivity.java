package com.example.BeautyApp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.BeautyApp.MainActivity;
import com.example.BeautyApp.R;
import com.example.BeautyApp.activities.LoginActivity;

public class LoadAppActivity extends AppCompatActivity {

    String userPhone,userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_app);

        if (checkLoginRemember() == -1){
            Intent intent = new Intent(LoadAppActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if (checkLoginRemember() == 1){
            Intent intent = new Intent(LoadAppActivity.this, AdminActivity.class);
            intent.putExtra("userName",userInfo);
            intent.putExtra("userInfo",userPhone);
            startActivity(intent);
            finish();
        }
        if (checkLoginRemember() == 2){
            Intent intent = new Intent(LoadAppActivity.this, MainActivity.class);
            intent.putExtra("userName",userInfo);
            intent.putExtra("userInfo",userPhone);
            startActivity(intent);
            finish();
        }


    }
    public int checkLoginRemember(){
        SharedPreferences sharedPreferences = getSharedPreferences("USER_FILE.txt",MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("status",false);
        if (check){
            userPhone = sharedPreferences.getString("userPhone","");
            userInfo = sharedPreferences.getString("userName","");
            if (sharedPreferences.getString("quyen","").equals("admin")){
                return 1;
            }
            if (sharedPreferences.getString("quyen","").equals("khach hang")){
                return 2;
            }
        }
        return -1;
    }
}