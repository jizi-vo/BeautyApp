package com.example.BeautyApp.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.BeautyApp.MainActivity;
import com.example.BeautyApp.R;
import com.example.BeautyApp.Retrofit2.APIUtils;
import com.example.BeautyApp.Retrofit2.DataClient;
import com.example.BeautyApp.Retrofit2.RealPathUtil;
import com.example.BeautyApp.models.Server;
import com.example.BeautyApp.models.UserModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    CircleImageView imageView;
    TextView userName,name,address,update,phone;
    int RequestCodeImg = 123;
    Uri mUri;
    ProgressDialog progressDialog;
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()==Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data == null){
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            imageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    //////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait...");

        anhXa();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClickRequestPermission();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUri != null){
                    updateUser();
                }else{
                    updateUserNoImg();
                }
            }
        });
    }

    private void updateUserNoImg() {
        progressDialog.show();
        String userMatk = MainActivity.userModelList.get(0).getMatk()+"";
        String userName = name.getText().toString();
        String userAddress = address.getText().toString();
        if (userName.length() > 0 && userAddress.length() >0){
            DataClient updateUser = APIUtils.getData();
            Call<String> callBack = updateUser.updateUserNoImg(userMatk,userName,userAddress);
            callBack.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String result = response.body();
                    if (result.equals("Success")){
                        Toast.makeText(UpdateProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        //////////loadUserAgain
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkuserinfo, new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response!=null){
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
                                            MainActivity.userModelList.clear();
                                            MainActivity.userModelList.add(new UserModel(matk,name,phone,pass,address,quyen,img));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Error"+error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> hashMap = new HashMap<String, String>();
                                hashMap.put("sdt",MainActivity.user);
                                return hashMap;
                            }
                        };
                        requestQueue.add(stringRequest);
                        progressDialog.dismiss();
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateProfileActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }else{
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private void updateUser() {
        try {
            progressDialog.show();
            String userMatk = MainActivity.userModelList.get(0).getMatk()+"";
            String userName = name.getText().toString();
            String userAddress = address.getText().toString();
            if (userName.length() > 0 && userAddress.length() >0){
                String strRealPath = RealPathUtil.getRealPath(this,mUri);
                File file = new File(strRealPath);
                String file_path = file.getAbsolutePath();
                String[] nameFileArray = file_path.split("\\.");

                file_path = nameFileArray[0]+System.currentTimeMillis()+"."+nameFileArray[1];
                RequestBody requestBodyImg = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                MultipartBody.Part multipartBodyImg = MultipartBody.Part.createFormData("uploadimguser",file_path,requestBodyImg);

                DataClient dataClient = APIUtils.getData();
                retrofit2.Call<String> callBack = dataClient.upLoadImg(multipartBodyImg);
                callBack.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response!= null){
                            String message = response.body();
                            if (message.length()>0){
                                DataClient updateUser = APIUtils.getData();
                                Call<String> callBack = updateUser.updateUser(userMatk,userName,userAddress,APIUtils.BaseUrl+"imgUsers/"+message);
                                callBack.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String result = response.body();
                                        if (result.equals("Success")){
                                            Toast.makeText(UpdateProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                            //////////loadUserAgain
                                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkuserinfo, new com.android.volley.Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    if (response!=null){
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
                                                                MainActivity.userModelList.clear();
                                                                MainActivity.userModelList.add(new UserModel(matk,name,phone,pass,address,quyen,img));
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }, new com.android.volley.Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getApplicationContext(), "Error"+error.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    HashMap<String,String> hashMap = new HashMap<String, String>();
                                                    hashMap.put("sdt",MainActivity.user);
                                                    return hashMap;
                                                }
                                            };
                                            requestQueue.add(stringRequest);
                                            progressDialog.dismiss();
                                            finish();
                                        }else {
                                            progressDialog.dismiss();
                                            Toast.makeText(UpdateProfileActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {

                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }else{
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }catch (Exception e){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        
    }


    private void OnClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallery();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission,RequestCodeImg);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCodeImg){
            if (grantResults.length >0 &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }
    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    private void anhXa() {
        imageView = findViewById(R.id.update_profile_img);
        userName = findViewById(R.id.update_profile_userName);
        name = findViewById(R.id.update_profile_name);
        address = findViewById(R.id.update_profile_address);
        update = findViewById(R.id.update_profile_update);
        phone = findViewById(R.id.update_profile_phone);

        userName.setText(MainActivity.userModelList.get(0).getTentk());
        name.setText(MainActivity.userModelList.get(0).getTentk());
        phone.setText(MainActivity.userModelList.get(0).getSdt());
        address.setText(MainActivity.userModelList.get(0).getDiachi());
        Glide.with(getApplicationContext())
                .load(MainActivity.userModelList.get(0).getHinhanh())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(imageView);
    }

}