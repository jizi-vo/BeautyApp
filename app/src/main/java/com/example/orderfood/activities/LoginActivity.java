package com.example.orderfood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.orderfood.MainActivity;
import com.example.orderfood.R;
import com.example.orderfood.admin.AdminActivity;
import com.example.orderfood.admin.LoadAppActivity;
import com.example.orderfood.models.Server;
import com.example.orderfood.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText phone,pass;
    TextView reg,forgotPass;
    CheckBox checkBox;

    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login_btn);
        phone = findViewById(R.id.login_phone);
        pass = findViewById(R.id.login_pass);
        reg = findViewById(R.id.login_reg);
        checkBox = findViewById(R.id.checkbox_rememberPass);
        forgotPass = findViewById(R.id.forgotPass);

        progressBar = findViewById(R.id.progressbar_login);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(intent);
            }
        });

    }

    private void rememberPass(String userPhone, String userPass, boolean status,String quyen,String userName) {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_FILE.txt",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!status){
            editor.clear();
        }else {
            editor.putString("userPhone",userPhone);
            editor.putString("userPass",userPass);
            editor.putBoolean("status",status);
            editor.putString("quyen",quyen);
            editor.putString("userName",userName);
        }
        editor.commit();
    }


    private void loginUser() {
        String userPhone,userPass;
        userPhone = phone.getText().toString();
        userPass = pass.getText().toString();

        if (TextUtils.isEmpty(userPhone)){
            Toast.makeText(this, "Số điện thoại không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userPass)){
            Toast.makeText(this, "Bạn chưa nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        ///////Login User
        login.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linklogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("Fail")){
                    login.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Tài khoản mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                }else{
                    List<UserModel> userModelList = new ArrayList<>();
                    int matk;
                    String name;
                    String phone;
                    String pass;
                    String address;
                    String quyen;
                    String img;
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            matk = jsonObject.getInt("matk");
                            name = jsonObject.getString("tentk");
                            phone = jsonObject.getString("sdt");
                            pass = jsonObject.getString("mk");
                            address = jsonObject.getString("diachi");
                            quyen = jsonObject.getString("quyen");
                            img = jsonObject.getString("hinhanh");
                            userModelList.add(new UserModel(matk,name,phone,pass,address,quyen,img));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    login.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    if (!checkBox.isChecked()){
                        if (userModelList.get(0).getQuyen().equals("admin")){
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            intent.putExtra("userName",userModelList.get(0).getTentk());
                            intent.putExtra("userInfo",userPhone);
                            intent.putExtra("userId",String.valueOf(userModelList.get(0).getMatk()));
                            startActivity(intent);
                            finish();
                        }
                        if (userModelList.get(0).getQuyen().equals("khach hang")){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userName",userModelList.get(0).getTentk());
                            intent.putExtra("userInfo",userPhone);
                            intent.putExtra("userId",String.valueOf(userModelList.get(0).getMatk()));
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Intent intent = new Intent(LoginActivity.this, LoadAppActivity.class);
                        rememberPass(userPhone,userPass,checkBox.isChecked(),userModelList.get(0).getQuyen(),userModelList.get(0).getTentk());
                        startActivity(intent);
                        finish();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("sdt",userPhone);
                hashMap.put("mk",userPass);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }
}