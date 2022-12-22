package com.example.BeautyApp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.BeautyApp.R;
import com.example.BeautyApp.Retrofit2.APIUtils;
import com.example.BeautyApp.Retrofit2.DataClient;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button sentOTP;
    EditText Phone,Pass,PassAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        progressBar = findViewById(R.id.progressbar_forgot);
        sentOTP = findViewById(R.id.forgotPass_sentOTP);
        Phone = findViewById(R.id.forgotPass_Phone);
        Pass = findViewById(R.id.forgotPass_Pass);
        PassAgain = findViewById(R.id.forgotPass_PassAgain);

        sentOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Phone.getText().toString().trim().isEmpty() || Pass.getText().toString().trim().isEmpty() || PassAgain.getText().toString().trim().isEmpty()){
                    Toast.makeText(ForgotPassActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Pass.getText().toString().trim().equals(PassAgain.getText().toString().trim())){
                    Toast.makeText(ForgotPassActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //////// Check account
                DataClient dataClient = APIUtils.getData();
                Call<String> call = dataClient.checkaccount(Phone.getText().toString().trim());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().equals("1")){
                            sendOTP(Phone.getText().toString().trim(),Pass.getText().toString().trim());
                        }else {
                            Toast.makeText(ForgotPassActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

    }

    private void sendOTP(String Phone,String Pass) {
        sentOTP.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+84" + Phone, 60, TimeUnit.SECONDS, ForgotPassActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        sentOTP.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        sentOTP.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ForgotPassActivity.this, "VerificationFailed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(final String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        sentOTP.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(ForgotPassActivity.this,ForgotPassVerificationActivity.class);
                        intent.putExtra("Phone",Phone);
                        intent.putExtra("Pass",Pass);
                        intent.putExtra("VerificationId",verificationId);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }
}