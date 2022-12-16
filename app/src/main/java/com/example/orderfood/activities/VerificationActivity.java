package com.example.orderfood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.example.orderfood.R;
import com.example.orderfood.models.Server;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    private EditText inputCode1,inputCode2,inputCode3,inputCode4,inputCode5,inputCode6;
    Button btnVerify;
    ProgressBar progressBar;
    String userName,userPhone,userPass,userAddress,verificationId;
    TextView resendOTP,countDown;
    CountDownTimer countDownTimer;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);

        btnVerify = findViewById(R.id.btnVerify);
        progressBar = findViewById(R.id.verification_progressBar);
        resendOTP = findViewById(R.id.resendOTP);
        countDown = findViewById(R.id.countDown);

        setupOTPInput();

        Intent intent = getIntent();
        userName =  intent.getStringExtra("Name");
        userPhone = intent.getStringExtra("Phone");
        userPass = intent.getStringExtra("Pass");
        userAddress = intent.getStringExtra("Address");
        verificationId = intent.getStringExtra("VerificationId");

        loadCountDown();

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputCode1.getText().toString().trim().isEmpty()
                ||inputCode2.getText().toString().trim().isEmpty()
                ||inputCode3.getText().toString().trim().isEmpty()
                ||inputCode4.getText().toString().trim().isEmpty()
                ||inputCode5.getText().toString().trim().isEmpty()
                ||inputCode6.getText().toString().trim().isEmpty()){
                    Toast.makeText(VerificationActivity.this, "Mã xác thực gồm 6 số!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = inputCode1.getText().toString()+
                        inputCode2.getText().toString()+
                        inputCode3.getText().toString()+
                        inputCode4.getText().toString()+
                        inputCode5.getText().toString()+
                        inputCode6.getText().toString();

                if (verificationId != null){
                    btnVerify.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        createUser();
                                    }else {
                                        progressBar.setVisibility(View.GONE);
                                        btnVerify.setVisibility(View.VISIBLE);
                                        Toast.makeText(VerificationActivity.this, "Mã xác thực không chính xác!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTP.setVisibility(View.GONE);
                countDown.setVisibility(View.VISIBLE);
                loadCountDown();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+84" + userPhone, 60, TimeUnit.SECONDS, VerificationActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Toast.makeText(VerificationActivity.this, "VerificationFailed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(final String newVerificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationId = newVerificationId;
                                Toast.makeText(VerificationActivity.this, "Mã OTP mới đã được gửi", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }

    private void loadCountDown() {
        countDownTimer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                count = (int)millisUntilFinished/1000;
                countDown.setText(count+"");
            }

            @Override
            public void onFinish() {
                countDown.setVisibility(View.GONE);
                resendOTP.setVisibility(View.VISIBLE);
                return;
            }
        }.start();
    }

    private void createUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linktaikhoan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("1")){
                    progressBar.setVisibility(View.GONE);
                    loadDialog(Gravity.CENTER);
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(VerificationActivity.this, "Tài khoản đã tồn tại!"+response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VerificationActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("tentk",userName);
                hashMap.put("sdt",userPhone);
                hashMap.put("mk",userPass);
                hashMap.put("diachi",userAddress);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_registration);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == gravity){
            dialog.setCancelable(false);
        }else {
            dialog.setCancelable(false);
        }

        TextView dialogHoanTat = dialog.findViewById(R.id.dialog_registration_hoantat);

        dialogHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(VerificationActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    private void setupOTPInput(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }else {
                    inputCode1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }else {
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }else {
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode6.requestFocus();
                }else {
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}