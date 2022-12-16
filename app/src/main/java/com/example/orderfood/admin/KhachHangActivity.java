package com.example.orderfood.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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
import com.example.orderfood.adapters.CustomerAdapter;
import com.example.orderfood.adapters.ViewAllAdapter;
import com.example.orderfood.models.MyOrderModel;
import com.example.orderfood.models.Server;
import com.example.orderfood.models.UserModel;
import com.example.orderfood.models.ViewAllModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KhachHangActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;

    List<UserModel> userModelList;
    CustomerAdapter customerAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    EditText searchBox;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khach_hang);

        searchBox = findViewById(R.id.admin_khach_hang_search);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        swipeRefreshLayout = findViewById(R.id.customer_swiper);
        recyclerView = findViewById(R.id.rec_khach_hang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userModelList = new ArrayList<>();
        customerAdapter = new CustomerAdapter(this,userModelList);
        recyclerView.setAdapter(customerAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressDialog.show();
        getCustomer();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()){
                    userModelList.clear();
                    customerAdapter.notifyDataSetChanged();
                    getCustomer();
                }else {
                    searchProduct(editable.toString());
                }
            }
        });
    }

    private void searchProduct(String type) {
        if (!type.isEmpty()){
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkadminsearchcustomer, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        int matk;
                        String name;
                        String phone;
                        String pass;
                        String address;
                        String quyen;
                        String img;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            userModelList.clear();
                            customerAdapter.notifyDataSetChanged();
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
                                customerAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(KhachHangActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("type", type);
                    return hashMap;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    private void getCustomer() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkadminloadcustomer, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    int matk;
                    String name;
                    String phone;
                    String pass;
                    String address;
                    String quyen;
                    String img;
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        userModelList.clear();
                        customerAdapter.notifyDataSetChanged();
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
                            customerAdapter.notifyDataSetChanged();
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
                Toast.makeText(KhachHangActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        userModelList.clear();
        customerAdapter.notifyDataSetChanged();
        getCustomer();
        searchBox.setText("");
        swipeRefreshLayout.setRefreshing(false);
    }
}