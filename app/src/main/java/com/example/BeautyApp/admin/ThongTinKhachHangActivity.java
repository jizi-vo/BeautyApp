package com.example.BeautyApp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.example.BeautyApp.R;
import com.example.BeautyApp.Retrofit2.APIUtils;
import com.example.BeautyApp.Retrofit2.DataClient;
import com.example.BeautyApp.adapters.MyOrderAdapter;
import com.example.BeautyApp.models.CustomerDetailModel;
import com.example.BeautyApp.models.MyOrderModel;
import com.example.BeautyApp.models.Server;

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

public class ThongTinKhachHangActivity extends AppCompatActivity {

    MyOrderAdapter myOrderAdapter;
    List<MyOrderModel> myOrderModelList;
    RecyclerView recyclerView;

    TextView name,phone,address,bill,total,billcancel;
    List<CustomerDetailModel> customerDetailModelList;
    String sdt,quyen,strQuyen;
    Spinner spinner;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_khach_hang);

        spinner = findViewById(R.id.customer_detail_spinner);
        recyclerView = findViewById(R.id.rec_billCustomer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        myOrderModelList = new ArrayList<>();
        myOrderAdapter = new MyOrderAdapter(getApplicationContext(),myOrderModelList);
        recyclerView.setAdapter(myOrderAdapter);

        loadBillCustomer();

        Intent intent = getIntent();
        sdt = intent.getStringExtra("sdt");
        quyen = intent.getStringExtra("quyen");
        if (quyen.equals("admin")){
            strQuyen = "Admin";
        }else{
            strQuyen = "User";
        }

        ArrayList<String> arrayQuyen = new ArrayList<String>();
        arrayQuyen.add("Admin");
        arrayQuyen.add("User");


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,arrayQuyen);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setSelection(arrayAdapter.getPosition(strQuyen));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String quyen,updateQuyen;
                if (i == 0) {
                    quyen =  "Admin";
                    updateQuyen = "admin";
                }else {
                    quyen = "User";
                    updateQuyen = "khach hang";
                }
                count++;
                if (count!=1){
                    DataClient dataClient = APIUtils.getData();
                    Call<String> callBack = dataClient.updateQuyen(sdt,updateQuyen);
                    callBack.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            String result = response.body();
                            if (result.equals("Success")){
                                Toast.makeText(ThongTinKhachHangActivity.this, "Quyền truy cập hiện tại là "+quyen, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ThongTinKhachHangActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        customerDetailModelList = new ArrayList<>();

        name = findViewById(R.id.customer_detail_name);
        phone = findViewById(R.id.customer_detail_phone);
        address = findViewById(R.id.customer_detail_address);
        bill = findViewById(R.id.customer_detail_hd);
        billcancel = findViewById(R.id.customer_detail_hdhuy);
        total = findViewById(R.id.customer_detail_tong);
        customerDetailModelList = new ArrayList<>();
        loadBillDetailCustomer();
    }

    private void loadBillDetailCustomer() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkadminloadcustomerdetail, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null){
                    String tentk;
                    String sdt;
                    String diachi;
                    String hd;
                    String hdhuy;
                    int tong;
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            tentk = jsonObject.getString("tentk");
                            sdt = jsonObject.getString("sdt");
                            diachi = jsonObject.getString("diachi");
                            hd = jsonObject.getString("hd");
                            hdhuy = jsonObject.getString("hdhuy");
                            try {
                                tong = jsonObject.getInt("tong");
                            }catch (Exception e){
                                tong = 0;
                            }
                            customerDetailModelList.add(new CustomerDetailModel(tentk,sdt,diachi,hd,hdhuy,tong));
                        }
                        name.setText(customerDetailModelList.get(0).getTentk());
                        phone.setText(customerDetailModelList.get(0).getSdt());
                        address.setText(customerDetailModelList.get(0).getDiachi());
                        bill.setText(customerDetailModelList.get(0).getHd());
                        billcancel.setText(customerDetailModelList.get(0).getHdhuy());
                        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                        total.setText(decimalFormat.format(customerDetailModelList.get(0).getTong())+"đ");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThongTinKhachHangActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("sdt", sdt);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadBillCustomer() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkbill, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    String mahd = "";
                    String sdt = "";
                    String date = "";
                    String tt = "";
                    String dc = "";
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            mahd = jsonObject.getString("mahd");
                            sdt = jsonObject.getString("sdt");
                            date = jsonObject.getString("date");
                            tt = jsonObject.getString("tt");
                            dc = jsonObject.getString("dc");
                            myOrderModelList.add(new MyOrderModel(mahd, sdt, date, tt, dc));
                            myOrderAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("phone", sdt);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

}