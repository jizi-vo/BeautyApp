package com.example.orderfood.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.example.orderfood.Retrofit2.APIUtils;
import com.example.orderfood.Retrofit2.DataClient;
import com.example.orderfood.adapters.CTDHAdapter;
import com.example.orderfood.models.BillDetailModel;
import com.example.orderfood.models.MyOrderModel;
import com.example.orderfood.models.Server;
import com.example.orderfood.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class CTDHActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<BillDetailModel> billDetailModelList;
    CTDHAdapter ctdhAdapter;
    List<UserModel> userModelList;

    TextView XacNhan,tenkh,tongcong,sdt,dc;
    Spinner TrangThai;
    String type,tthd,address,status;
    String phone;
    int totalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctdh);

        recyclerView = findViewById(R.id.rec_ctdh);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        billDetailModelList = new ArrayList<>();
        userModelList = new ArrayList<>();
        ctdhAdapter = new CTDHAdapter(getApplicationContext(),billDetailModelList);
        recyclerView.setAdapter(ctdhAdapter);

        type = getIntent().getStringExtra("mahd");
        phone = getIntent().getStringExtra("sdt");
        address = getIntent().getStringExtra("dc");
        status = getIntent().getStringExtra("tt");

        XacNhan = findViewById(R.id.ctdh_xacnhan);
        TrangThai = findViewById(R.id.spinner_tt);
        tenkh = findViewById(R.id.ctdh_ten);
        sdt = findViewById(R.id.ctdh_sdt);
        dc = findViewById(R.id.ctdh_dc);
        tongcong = findViewById(R.id.ctdh_tongcong);

        ArrayList<String> arrayTrangThai = new ArrayList<String>();
        arrayTrangThai.add("Chờ xử lý.");
        arrayTrangThai.add("Đang giao.");
        arrayTrangThai.add("Đơn hàng đã bị hủy!");
        arrayTrangThai.add("Đã thanh toán.");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,arrayTrangThai);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TrangThai.setAdapter(arrayAdapter);
        TrangThai.setSelection(arrayAdapter.getPosition(status));
        loadCTDH();
        loadUserInfo();
        sdt.setText(phone);
        dc.setText(address);
        TrangThai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tthd = arrayTrangThai.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        XacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataClient dataClient1 = APIUtils.getData();
                Call<String> callBack1 = dataClient1.updateTrangThaiCTHD(type,tthd);
                callBack1.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        String message = response.body();
                        if (message.equals("Success")){
                        }
                        else {
                            Toast.makeText(CTDHActivity.this, "ttcthd"+response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(CTDHActivity.this, "ttcthd"+t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                DataClient dataClient = APIUtils.getData();
                Call<String> callBack = dataClient.updateTrangThai(type,tthd);
                callBack.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        String message = response.body();
                        if (message.equals("Success")){
                            Toast.makeText(CTDHActivity.this, "Đã xác nhận hóa đơn!", Toast.LENGTH_SHORT).show();
                            DonHangActivity.myOrderModelList.clear();
                            reloadDH();
                            finish();
                        }
                        else {
                            Toast.makeText(CTDHActivity.this, "Error"+response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(CTDHActivity.this, "Error"+t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

    private void reloadDH() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkadminloaddonhang, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null){
                    String mahd = "";
                    String sdt = "";
                    String date = "";
                    String tt = "";
                    String dc = "";
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            mahd = jsonObject.getString("mahd");
                            sdt = jsonObject.getString("sdt");
                            date = jsonObject.getString("date");
                            tt = jsonObject.getString("tt");
                            dc = jsonObject.getString("dc");
                            DonHangActivity.myOrderModelList.add(new MyOrderModel(mahd,sdt,date,tt,dc));
                            DonHangActivity.adminLoadDHAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CTDHActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void loadUserInfo() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkuserinfo, new Response.Listener<String>() {
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
                            userModelList.add(new UserModel(matk,name,phone,pass,address,quyen,img));
                            tenkh.setText(name);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CTDHActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String, String>();
                hashMap.put("sdt",phone);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadCTDH() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkbilldetail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String mahd;
                int mamon;
                String tenmon;
                int sl;
                int gia;
                String danhgia;
                String hinhanh;
                if (response!=null){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            mahd = jsonObject.getString("mahd");
                            mamon = jsonObject.getInt("mamon");
                            tenmon = jsonObject.getString("tenmon");
                            sl = jsonObject.getInt("sl");
                            gia = jsonObject.getInt("gia");
                            danhgia = jsonObject.getString("danhgia");
                            hinhanh = jsonObject.getString("hinhanh");

                            billDetailModelList.add(new BillDetailModel(mahd,mamon,tenmon,sl,gia,danhgia,hinhanh));
                            ctdhAdapter.notifyDataSetChanged();
                            totalPrice = totalPrice + billDetailModelList.get(i).getGia();
                            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                            tongcong.setText(decimalFormat.format(totalPrice)+"đ");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("mahd",type);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }
}