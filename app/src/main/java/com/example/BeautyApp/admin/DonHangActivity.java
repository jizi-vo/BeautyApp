package com.example.BeautyApp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.BeautyApp.R;
import com.example.BeautyApp.adapters.AdminLoadDHAdapter;
import com.example.BeautyApp.models.MyOrderModel;
import com.example.BeautyApp.models.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonHangActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static List<MyOrderModel> myOrderModelList;
    public static AdminLoadDHAdapter adminLoadDHAdapter;
    Spinner spinner;
    ProgressDialog progressDialog;

    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        spinner = findViewById(R.id.spinener_donhang);
        recyclerView = findViewById(R.id.rec_donhang);
        swipeRefreshLayout = findViewById(R.id.swipe_donhang);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        myOrderModelList = new ArrayList<>();
        adminLoadDHAdapter = new AdminLoadDHAdapter(getApplicationContext(),myOrderModelList);
        recyclerView.setAdapter(adminLoadDHAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        loadSpinner();
    }

    private void loadSpinner() {
        progressDialog.show();
        ArrayList<String> arraySpinner = new ArrayList<String>();
        arraySpinner.add("Tất cả");
        arraySpinner.add("Chờ xử lý.");
        arraySpinner.add("Đang giao.");
        arraySpinner.add("Đơn hàng đã bị hủy!");
        arraySpinner.add("Đã thanh toán.");

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,arraySpinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                myOrderModelList.clear();
                adminLoadDHAdapter.notifyDataSetChanged();
                if (i==0){
                    loadDonHang();
                    return;
                }
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkadminloadspinnerdonhang, new com.android.volley.Response.Listener<String>() {
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
                                    myOrderModelList.add(new MyOrderModel(mahd,sdt,date,tt,dc));
                                    adminLoadDHAdapter.notifyDataSetChanged();
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
                        Toast.makeText(DonHangActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("tt",arrayAdapter.getItem(i).toString());
                        return hashMap;
                    }
                };
                requestQueue.add(stringRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadDonHang() {
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
                            myOrderModelList.add(new MyOrderModel(mahd,sdt,date,tt,dc));
                            adminLoadDHAdapter.notifyDataSetChanged();
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
                Toast.makeText(DonHangActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        myOrderModelList.clear();
        spinner.setSelection(arrayAdapter.getPosition("Tất cả"));
        loadDonHang();
        swipeRefreshLayout.setRefreshing(false);
    }
}