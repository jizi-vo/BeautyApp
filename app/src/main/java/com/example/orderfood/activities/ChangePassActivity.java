package com.example.orderfood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.orderfood.MainActivity;
import com.example.orderfood.R;
import com.example.orderfood.Retrofit2.APIUtils;
import com.example.orderfood.Retrofit2.DataClient;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassActivity extends AppCompatActivity {

    EditText passOld,passNew,passAgain;
    TextView save,userName;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        passOld = findViewById(R.id.change_pass_old);
        passNew = findViewById(R.id.change_pass_new);
        passAgain = findViewById(R.id.change_pass_again);
        save = findViewById(R.id.change_pass_save);
        userName = findViewById(R.id.change_pass_userName);
        circleImageView = findViewById(R.id.change_pass_img);

        Glide.with(getApplicationContext())
                .load(MainActivity.userModelList.get(0).getHinhanh())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(circleImageView);
        userName.setText(MainActivity.userModelList.get(0).getTentk());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPassOld = passOld.getText().toString();
                String userPassNew = passNew.getText().toString();
                String userPassAgain = passAgain.getText().toString();

                if (TextUtils.isEmpty(userPassOld)){
                    Toast.makeText(ChangePassActivity.this, "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userPassNew)){
                    Toast.makeText(ChangePassActivity.this, "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userPassAgain)){
                    Toast.makeText(ChangePassActivity.this, "Vui lòng điền đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userPassNew.length()<6){
                    Toast.makeText(ChangePassActivity.this, "Mật khẩu quá yếu!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!userPassNew.equals(userPassAgain)){
                    Toast.makeText(ChangePassActivity.this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userPassOld.equals(MainActivity.userModelList.get(0).getMk()+"")){
                    DataClient changePass = APIUtils.getData();
                    Call<String> callBack = changePass.changePass(MainActivity.userModelList.get(0).getMatk()+"",userPassNew);
                    callBack.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String result = response.body();
                            if (result.equals("Success")){
                                Toast.makeText(ChangePassActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(ChangePassActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }else {
                    Toast.makeText(ChangePassActivity.this, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}