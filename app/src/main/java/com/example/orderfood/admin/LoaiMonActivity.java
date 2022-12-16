package com.example.orderfood.admin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.orderfood.R;
import com.example.orderfood.Retrofit2.APIUtils;
import com.example.orderfood.Retrofit2.DataClient;
import com.example.orderfood.Retrofit2.RealPathUtil;
import com.example.orderfood.adapters.AdminLoaiMonAdapter;
import com.example.orderfood.adapters.HomeHorAdapter;
import com.example.orderfood.models.HomeHorModel;
import com.example.orderfood.models.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class LoaiMonActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    RecyclerView recyclerView;
    public static List<HomeHorModel> homeHorModelList;
    public static AdminLoaiMonAdapter adminLoaiMonAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    ImageView dialogImg;
    TextView dialogAdd,dialogCancel,addCategory;
    Uri mUri;
    int RequestCodeImg = 123;

    EditText dialogName;
    String nameCategory;


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
                            dialogImg.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_mon);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        swipeRefreshLayout = findViewById(R.id.swipe_loaimon);
        addCategory = findViewById(R.id.admin_loaimon_add);
        recyclerView = findViewById(R.id.admin_loaimon_viewall);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeHorModelList = new ArrayList<>();
        adminLoaiMonAdapter = new AdminLoaiMonAdapter(this,homeHorModelList);
        recyclerView.setAdapter(adminLoaiMonAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);

        /////////Load Category
        progressDialog.show();
        loadCategory();

        ///////// setOnClick AddCategory
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddCategory(Gravity.BOTTOM);
            }
        });

    }

    private void dialogAddCategory(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_mon_add_category);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.BOTTOM == gravity){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }

        dialogImg = dialog.findViewById(R.id.dialog_them_loai_img);
        dialogName = dialog.findViewById(R.id.dialog_them_loai_name);
        dialogAdd = dialog.findViewById(R.id.dialog_them_loai_them);
        dialogCancel = dialog.findViewById(R.id.dialog_them_loai_huy);


        ///////////loadImgCategory
        dialogImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        /////////////uploadCategory
        dialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameCategory = dialogName.getText().toString();
                if (mUri == null){
                    Toast.makeText(LoaiMonActivity.this, "Chưa chọn ảnh!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(nameCategory)){
                    Toast.makeText(LoaiMonActivity.this, "Chưa nhập tên loại!", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadCategory();
                dialog.dismiss();
            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void uploadCategory() {
        try {
            progressDialog.show();
            String strRealPath = RealPathUtil.getRealPath(this,mUri);
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
                            Call<String> callBack1 = uploadCategory.uploadCategory(nameCategory,APIUtils.BaseUrl+"imgCategory/"+message);
                            callBack1.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                    String result = response.body();
                                    if (result.equals("Success")) {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoaiMonActivity.this, "Thêm loại món thành công!", Toast.LENGTH_SHORT).show();
                                        homeHorModelList.clear();
                                        loadCategory();
                                        mUri = null;
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoaiMonActivity.this, "Tên loại món đã tồn tại!"+result, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(LoaiMonActivity.this, "Tên loại món đã tồn tại!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{

                        }
                    }else {
                        Toast.makeText(LoaiMonActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(LoaiMonActivity.this, "Upload Hinh anh"+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));
    }

    private void loadCategory() {
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
                            adminLoaiMonAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoaiMonActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public void onRefresh() {
        homeHorModelList.clear();
        loadCategory();
        swipeRefreshLayout.setRefreshing(false);
    }
}