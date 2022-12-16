package com.example.orderfood.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.Retrofit2.APIUtils;
import com.example.orderfood.Retrofit2.DataClient;
import com.example.orderfood.Retrofit2.RealPathUtil;
import com.example.orderfood.admin.MonActivity;
import com.example.orderfood.models.HomeHorModel;
import com.example.orderfood.models.Server;
import com.example.orderfood.models.ViewAllModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateMonActivity extends AppCompatActivity {

    Uri mUri;
    ImageView imageView;
    ArrayAdapter arrayAdapterTenLoai;
    Spinner spinner;
    String selecttenloai,gettenmon,getgia,getimg,getmamon,nameProduct,priceProduct;
    int getmaloai;
    int RequestCodeImg = 123;
    ProgressDialog progressDialog;
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
        setContentView(R.layout.activity_update_mon);

        Intent intent = getIntent();
        getmaloai = Integer.parseInt(intent.getStringExtra("maloai"));
        gettenmon = intent.getStringExtra("tenmon");
        getgia = intent.getStringExtra("gia");
        getimg = intent.getStringExtra("img");
        getmamon = intent.getStringExtra("mamon");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait...");

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.admin_dialog_mon);

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

        EditText dialogName = dialog.findViewById(R.id.mon_dialog_name);
        EditText dialogPrice = dialog.findViewById(R.id.mon_dialog_price);
        TextView dialogUpdate = dialog.findViewById(R.id.mon_dialog_update);
        TextView dialogCancel = dialog.findViewById(R.id.mon_dialog_cancel);
        spinner = dialog.findViewById(R.id.spinner_mon_loaimon_update);
        imageView = dialog.findViewById(R.id.mon_dialog_img);

        //////////////load spinner
        final List<HomeHorModel> homeHorModelList = new ArrayList<>();
        final ArrayList<String> arrayTenloai = new ArrayList<>();
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
                            homeHorModelList.add(new HomeHorModel(maloai,tenloai,hinhanh));
                            arrayTenloai.add(tenloai);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    arrayAdapterTenLoai = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,arrayTenloai);
                    spinner.setAdapter(arrayAdapterTenLoai);

                    for (int i = 0;i<homeHorModelList.size();i++){
                        if (homeHorModelList.get(i).getMaloai() == getmaloai){
                            selecttenloai = homeHorModelList.get(i).getTenloai();
                        }
                    }
                    spinner.setSelection(arrayAdapterTenLoai.getPosition(selecttenloai));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateMonActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getmaloai = homeHorModelList.get(i).getMaloai();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialogName.setText(gettenmon);
        dialogPrice.setText(getgia);
        Glide.with(this).load(getimg).into(imageView);

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

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        dialogUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameProduct = dialogName.getText().toString().trim();
                priceProduct = dialogPrice.getText().toString().trim();
                if (TextUtils.isEmpty(nameProduct) || TextUtils.isEmpty(priceProduct)){
                    Toast.makeText(UpdateMonActivity.this, "Dữ liệu không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                try {
                    if (mUri == null){
                        DataClient dataClient = APIUtils.getData();
                        Call<String> callBack = dataClient.updateMon(getmamon,dialogName.getText().toString(),dialogPrice.getText().toString(),getmaloai+"",getimg);
                        callBack.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                String result = response.body();
                                if (result.equals("Success")){
                                    progressDialog.dismiss();
                                    Toast.makeText(UpdateMonActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    MonActivity.viewAllModelList.clear();
                                    loadMon();
                                    mUri = null;
                                    finish();
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(UpdateMonActivity.this, "Món đã tồn tại!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(UpdateMonActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        String strRealPath = RealPathUtil.getRealPath(getApplicationContext(),mUri);
                        File file = new File(strRealPath);
                        String file_path = file.getAbsolutePath();
                        String[] nameFileArray = file_path.split("\\.");

                        file_path = nameFileArray[0]+System.currentTimeMillis()+"."+nameFileArray[1];
                        RequestBody requestBodyImg = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                        MultipartBody.Part multipartBodyImg = MultipartBody.Part.createFormData("uploadimgmon",file_path,requestBodyImg);

                        DataClient dataClient = APIUtils.getData();
                        retrofit2.Call<String> callBack = dataClient.upLoadImgMon(multipartBodyImg);
                        callBack.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                if (response.body() != null){
                                    String message = response.body();
                                    if (message.length()>0){
                                        DataClient data = APIUtils.getData();
                                        Call<String> callBack = data.updateMon(getmamon,dialogName.getText().toString(),dialogPrice.getText().toString(),getmaloai+"",APIUtils.BaseUrl+"imgMon/"+message);
                                        callBack.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                                String result = response.body();
                                                if (result.equals("Success")){
                                                    progressDialog.dismiss();
                                                    Toast.makeText(UpdateMonActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                                    MonActivity.viewAllModelList.clear();
                                                    loadMon();
                                                    mUri = null;
                                                    finish();
                                                }else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(UpdateMonActivity.this, "Món đã tồn tại!", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                Toast.makeText(UpdateMonActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }else{
                                    Toast.makeText(UpdateMonActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(UpdateMonActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }catch (Exception e){
                    Toast.makeText(UpdateMonActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void loadMon() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkadminmon, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int mamon;
                String tenmon;
                int gia;
                String danhgia;
                int maloai;
                String hinhanh;
                int tt;
                if (response!=null){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            mamon = jsonObject.getInt("mamon");
                            tenmon = jsonObject.getString("tenmon");
                            gia = jsonObject.getInt("gia");
                            danhgia = jsonObject.getString("danhgia");
                            maloai = jsonObject.getInt("maloai");
                            hinhanh = jsonObject.getString("hinhanh");
                            tt = jsonObject.getInt("tt");
                            MonActivity.viewAllModelList.add(new ViewAllModel(mamon,tenmon,gia,danhgia,maloai,hinhanh,tt));
                            MonActivity.adminMonAdapter.notifyDataSetChanged();
                        }
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateMonActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));
    }
}