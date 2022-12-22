package com.example.BeautyApp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import com.example.BeautyApp.adapters.BillDetailAdapter;
import com.example.BeautyApp.models.BillDetailModel;
import com.example.BeautyApp.models.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillDetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<BillDetailModel> billDetailModelList;
    BillDetailAdapter billDetailAdapter;
    private String type;

    TextView textView;
    int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);

        type = getIntent().getStringExtra("mahd");

        textView = findViewById(R.id.bill_detail_totalPrice);

        recyclerView = findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        billDetailModelList = new ArrayList<>();
        billDetailAdapter = new BillDetailAdapter(getApplicationContext(),billDetailModelList);
        recyclerView.setAdapter(billDetailAdapter);

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
                            billDetailAdapter.notifyDataSetChanged();
                            totalPrice = totalPrice + billDetailModelList.get(i).getGia();
                            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                            textView.setText("Tổng: "+decimalFormat.format(totalPrice)+"đ");
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