package com.example.orderfood.admin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.orderfood.MainActivity;
import com.example.orderfood.R;
import com.example.orderfood.Retrofit2.APIUtils;
import com.example.orderfood.Retrofit2.DataClient;
import com.example.orderfood.Retrofit2.RealPathUtil;
import com.example.orderfood.activities.UpdateProfileActivity;
import com.example.orderfood.activities.ViewAllActivity;
import com.example.orderfood.adapters.AdminMonAdapter;
import com.example.orderfood.models.HomeHorModel;
import com.example.orderfood.models.Server;
import com.example.orderfood.models.UserModel;
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

public class MonActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout swipeRefreshLayout;
    public static List<ViewAllModel> viewAllModelList;
    public static AdminMonAdapter adminMonAdapter;
    public static RecyclerView recyclerView;

    ImageView dialogImg;
    TextView addMon,dialogAdd,dialogCancel;
    Uri mUri;
    EditText dialogName;
    Spinner dialogCategory,viewCategory;
    EditText dialogPrice;
    int RequestCodeImg = 123;
    int maloai;
    String tenmon;
    String gia;
    ArrayAdapter arrayAdapterTenLoai;

    ProgressDialog progressDialog,progressDialog1;
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
        setContentView(R.layout.activity_mon);
        addMon = findViewById(R.id.admin_mon_add);
        swipeRefreshLayout = findViewById(R.id.swipe_mon);
        viewCategory = findViewById(R.id.spinener_mon);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Wait...");

        recyclerView = findViewById(R.id.admin_mon_viewall);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewAllModelList = new ArrayList<>();
        adminMonAdapter = new AdminMonAdapter(this,viewAllModelList);
        recyclerView.setAdapter(adminMonAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);

        loadSpinner();

        addMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddMonDialog(Gravity.BOTTOM);
            }
        });

    }

    private void loadSpinner() {
        progressDialog.show();
        final List<HomeHorModel> homeHorModelList1 = new ArrayList<>();
        final ArrayList<String> arrayView = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.linkloaimon, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null){
                    arrayView.add("All");
                    arrayView.add("Ngừng kinh doanh");
                    int maloai;
                    String tenloai;
                    String hinhanh;
                    for (int i = 0;i <response.length();i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            maloai = jsonObject.getInt("maloai");
                            tenloai = jsonObject.getString("tenloai");
                            hinhanh = jsonObject.getString("hinhanh");
                            homeHorModelList1.add(new HomeHorModel(maloai,tenloai,hinhanh));
                            arrayView.add(tenloai);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    arrayAdapterTenLoai = new ArrayAdapter(MonActivity.this, android.R.layout.simple_spinner_dropdown_item,arrayView);
                    viewCategory.setAdapter(arrayAdapterTenLoai);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MonActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);

        viewCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    viewAllModelList.clear();
                    adminMonAdapter.notifyDataSetChanged();
                    loadMon();
                    return;
                }
                if (i == 1){
                    viewAllModelList.clear();
                    adminMonAdapter.notifyDataSetChanged();
                    loadMonDisable();
                    return;
                }
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkmon, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        viewAllModelList.clear();
                        adminMonAdapter.notifyDataSetChanged();
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
                                    viewAllModelList.add(new ViewAllModel(mamon,tenmon,gia,danhgia,maloai,hinhanh,tt));
                                    adminMonAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MonActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> hashMap = new HashMap<String,String>();
                        hashMap.put("maloai",""+homeHorModelList1.get(i-2).getMaloai());
                        return hashMap;
                    }
                };
                requestQueue.add(stringRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    private void loadMonDisable() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkadminmondisable, new Response.Listener<String>() {
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
                            viewAllModelList.add(new ViewAllModel(mamon,tenmon,gia,danhgia,maloai,hinhanh,tt));
                            adminMonAdapter.notifyDataSetChanged();
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
                Toast.makeText(MonActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void openAddMonDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_mon_add);

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

        dialogImg = dialog.findViewById(R.id.dialog_them_mon_img);
        dialogName = dialog.findViewById(R.id.dialog_them_mon_name);
        dialogCategory = (Spinner) dialog.findViewById(R.id.spinner_mon_loaimon);
        dialogPrice = dialog.findViewById(R.id.dialog_them_mon_price);
        dialogAdd = dialog.findViewById(R.id.dialog_them_mon_them);
        dialogCancel = dialog.findViewById(R.id.dialog_them_mon_huy);


        //////////////Load Spinner Category
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
                    ArrayAdapter arrayAdapterTenLoai = new ArrayAdapter(MonActivity.this, android.R.layout.simple_spinner_dropdown_item,arrayTenloai);
                    dialogCategory.setAdapter(arrayAdapterTenLoai);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MonActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);

        /////////////////////get idCategory
        dialogCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                maloai = homeHorModelList.get(i).getMaloai();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ////////////////////////load Img Product
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


        //////////////////////Dialog Cancel
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ////////////////////Dialog Add
        dialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tenmon = dialogName.getText().toString();
                gia = dialogPrice.getText().toString();
                if (TextUtils.isEmpty(tenmon) || TextUtils.isEmpty(gia)){
                    Toast.makeText(MonActivity.this, "Dữ liệu không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mUri == null){
                    Toast.makeText(MonActivity.this, "Chưa chọn ảnh!", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadMon();
                dialog.dismiss();

            }
        });

        dialog.show();
    }


    ////////////////////////Load Product
    private void uploadMon() {
            try {
                progressDialog1.show();
                String strRealPath = RealPathUtil.getRealPath(this,mUri);
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
                                DataClient uploadMon = APIUtils.getData();
                                Call<String> callBack = uploadMon.uploadMon(tenmon,gia,maloai+"",APIUtils.BaseUrl+"imgMon/"+message);
                                callBack.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                        String result = response.body();
                                        if (result.equals("Success")){
                                            progressDialog1.dismiss();
                                            Toast.makeText(MonActivity.this, "Thêm món thành công!", Toast.LENGTH_SHORT).show();
                                            viewAllModelList.clear();
                                            loadMon();
                                            mUri = null;
                                        }else {
                                            progressDialog1.dismiss();
                                            Toast.makeText(MonActivity.this, "Món đã tồn tại!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(MonActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }else{
                            Toast.makeText(MonActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(MonActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    ////////////////////////Select Picture Product
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));
    }


    ///////////////////////Load Product
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
                            viewAllModelList.add(new ViewAllModel(mamon,tenmon,gia,danhgia,maloai,hinhanh,tt));
                            adminMonAdapter.notifyDataSetChanged();
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
                Toast.makeText(MonActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    ////////////////Refresh
    @Override
    public void onRefresh() {
        viewAllModelList.clear();
        viewCategory.setSelection(arrayAdapterTenLoai.getPosition("All"));
        loadMon();
        swipeRefreshLayout.setRefreshing(false);
    }
}