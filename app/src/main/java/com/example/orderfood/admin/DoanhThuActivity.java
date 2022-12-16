package com.example.orderfood.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
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
import com.example.orderfood.activities.LoginActivity;
import com.example.orderfood.adapters.ThongKeAdapter;
import com.example.orderfood.models.BillDetailModel;
import com.example.orderfood.models.Server;
import com.example.orderfood.models.ThongKeModel;
import com.example.orderfood.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoanhThuActivity extends AppCompatActivity {

    List<ThongKeModel> thongKeModelList;
    ThongKeAdapter thongKeAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    EditText ngaybatdau,ngayketthuc;
    TextView thongke,tonghoadon,choxuly,danggiao,dahuy,dathanhtoan,doanhthu;
    String start,end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doanh_thu);

        ngaybatdau = findViewById(R.id.thongke_ngaybatdau);
        ngayketthuc = findViewById(R.id.thongke_ngayketthuc);
        thongke = findViewById(R.id.thongke_thongke);
        tonghoadon = findViewById(R.id.thongke_tong);
        choxuly = findViewById(R.id.thongke_choxuly);
        danggiao = findViewById(R.id.thongke_danggiao);
        dahuy = findViewById(R.id.thongke_dahuy);
        dathanhtoan = findViewById(R.id.thongke_dathanhtoan);
        doanhthu = findViewById(R.id.thongke_doanhthu);

        recyclerView = findViewById(R.id.thongke_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        thongKeModelList = new ArrayList<>();
        thongKeAdapter = new ThongKeAdapter(this,thongKeModelList);
        recyclerView.setAdapter(thongKeAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        ngaybatdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chonngaybatdau();
            }
        });

        ngayketthuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chonngayketthuc();
            }
        });

        thongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start == null || end == null){
                    Toast.makeText(DoanhThuActivity.this, "Chưa chọn ngày bắt đầu và kết thúc!", Toast.LENGTH_SHORT).show();
                    return;
                }
                thongKeModelList.clear();
                thongKeAdapter.notifyDataSetChanged();
                loadRec();
                loadthongke();
            }
        });


    }

    private void loadthongke() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkadminloadthongke, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            tonghoadon.setText(jsonObject.getString("tong"));
                            choxuly.setText(jsonObject.getString("tt1"));
                            danggiao.setText(jsonObject.getString("tt2"));
                            dahuy.setText(jsonObject.getString("tt3"));
                            dathanhtoan.setText(jsonObject.getString("tt4"));
                            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                            doanhthu.setText(decimalFormat.format(jsonObject.getInt("tongtien"))+"đ");
                        }
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }else{
                    Toast.makeText(DoanhThuActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DoanhThuActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("ngaybatdau",start);
                hashMap.put("ngayketthuc",end);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadRec() {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkadminloadrecthongke, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String tenmon;
                int gia;
                String danhgia;
                String hinhanh;
                int sl;
                String pt;
                if (response!=null){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            tenmon = jsonObject.getString("tenmon");
                            gia = jsonObject.getInt("gia");
                            danhgia = jsonObject.getString("danhgia");
                            hinhanh = jsonObject.getString("hinhanh");
                            try {
                                sl = jsonObject.getInt("sl");
                            }catch (Exception e){
                                sl = 0;
                            }
                            pt = jsonObject.getString("pt");
                            thongKeModelList.add(new ThongKeModel(hinhanh,tenmon,danhgia,gia,sl,pt));
                            thongKeAdapter.notifyDataSetChanged();
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
                hashMap.put("ngaybatdau",start);
                hashMap.put("ngayketthuc",end);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void Chonngayketthuc() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i,i1,i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                ngayketthuc.setText(simpleDateFormat.format(calendar.getTime()));
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
                end = simpleDateFormat1.format(calendar.getTime());
            }
        },year,month,day);
        datePickerDialog.show();
    }

    private void Chonngaybatdau() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i,i1,i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                ngaybatdau.setText(simpleDateFormat.format(calendar.getTime()));
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
                start = simpleDateFormat1.format(calendar.getTime());
            }
        },year,month,day);
        datePickerDialog.show();
    }
}