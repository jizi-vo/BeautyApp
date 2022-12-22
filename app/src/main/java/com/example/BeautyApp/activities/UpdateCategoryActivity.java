package com.example.BeautyApp.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.BeautyApp.R;
import com.example.BeautyApp.Retrofit2.APIUtils;
import com.example.BeautyApp.Retrofit2.DataClient;
import com.example.BeautyApp.Retrofit2.RealPathUtil;
import com.example.BeautyApp.admin.LoaiMonActivity;
import com.example.BeautyApp.models.HomeHorModel;
import com.example.BeautyApp.models.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateCategoryActivity extends AppCompatActivity {
    Uri mUri;
    ImageView imageView;
    String nameCategory;
    int RequestCodeImg = 123;

    ProgressDialog progressDialog;
    String getmaloai,gethinhanh,gettenloai;
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()== Activity.RESULT_OK){
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait...");

        Intent intent = getIntent();
        getmaloai = intent.getStringExtra("maloai");
        gethinhanh = intent.getStringExtra("hinhanh");
        gettenloai = intent.getStringExtra("tenloai");

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.admin_dialog_loai_mypham);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == Gravity.CENTER){
            dialog.setCancelable(false);
        }else {
            dialog.setCancelable(false);
        }


        EditText dialogNameCategory = dialog.findViewById(R.id.loai_mon_dialog_name);
        TextView dialogUpdateCategory = dialog.findViewById(R.id.loai_mon_dialog_update);
        TextView dialogCancel = dialog.findViewById(R.id.loai_mon_dialog_cancel);
        imageView = dialog.findViewById(R.id.loai_mon_dialog_img);

        dialogNameCategory.setText(gettenloai);
        Glide.with(this).load(gethinhanh).into(imageView);

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        dialogUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameCategory = dialogNameCategory.getText().toString();
                if (TextUtils.isEmpty(nameCategory)) {
                    Toast.makeText(UpdateCategoryActivity.this, "Chưa nhập tên loại!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                if (mUri == null){
                    DataClient dataClient = APIUtils.getData();
                    Call<String> callBack = dataClient.updateCategory(getmaloai,nameCategory,gethinhanh);
                    callBack.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            if (response != null) {
                                String message = response.body();{
                                    if (message.equals("Success")){
                                        progressDialog.dismiss();
                                        Toast.makeText(UpdateCategoryActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                        reloadCategory();
                                        finish();
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(UpdateCategoryActivity.this, "Tên loại đã tồn tại", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                } else {
                    try {
                        String strRealPath = RealPathUtil.getRealPath(getApplicationContext(),mUri);
                        File file = new File(strRealPath);
                        String file_path = file.getAbsolutePath();
                        String[] nameFileArray = file_path.split("\\.");

                        file_path = nameFileArray[0]+System.currentTimeMillis()+"."+nameFileArray[1];
                        RequestBody requestBodyImg = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                        MultipartBody.Part multipartBodyImg = MultipartBody.Part.createFormData("uploadimgcategory",file_path,requestBodyImg);

                        DataClient dataClient = APIUtils.getData();
                        Call<String> callBack = dataClient.uploadImgCategory(multipartBodyImg);
                        callBack.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                if (response !=null){
                                    String message = response.body();
                                    if (message.length()>0){
                                        DataClient uploadCategory = APIUtils.getData();
                                        Call<String> callBack1 = uploadCategory.updateCategory(getmaloai,nameCategory,APIUtils.BaseUrl+"imgCategory/"+message);
                                        callBack1.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                                String result = response.body();
                                                if (result.equals("Success")) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(UpdateCategoryActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                                    reloadCategory();
                                                    mUri = null;
                                                    finish();
                                                }else {
                                                    Toast.makeText(UpdateCategoryActivity.this, "Tên loại món đã tồn tại!"+result, Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                progressDialog.dismiss();
                                                Toast.makeText(UpdateCategoryActivity.this, "Tên loại món đã tồn tại!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{

                                    }
                                }else {
                                    Toast.makeText(UpdateCategoryActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(UpdateCategoryActivity.this, "Upload Hinh anh"+t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (Exception e){
                    }
                }
            }
        });

        dialog.show();

    }


    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));
    }

    private void reloadCategory() {
        LoaiMonActivity.homeHorModelList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.linkloaimon, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null){
                    int maloai;
                    String tenloai;
                    String hinhanh;
                    for (int i = 0;i <response.length();i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            maloai = jsonObject.getInt("maloai");
                            tenloai = jsonObject.getString("tenloai");
                            hinhanh = jsonObject.getString("hinhanh");
                            LoaiMonActivity.homeHorModelList.add(new HomeHorModel(maloai,tenloai,hinhanh));
                            LoaiMonActivity.adminLoaiMonAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateCategoryActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}