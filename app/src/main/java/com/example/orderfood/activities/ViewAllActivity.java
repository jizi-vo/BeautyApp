package com.example.orderfood.activities;

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
import com.example.orderfood.R;
import com.example.orderfood.adapters.ViewAllAdapter;
import com.example.orderfood.models.Server;
import com.example.orderfood.models.ViewAllModel;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAllActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    List<ViewAllModel> viewAllModelList;
    ViewAllAdapter viewAllAdapter;

    TextView product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        String type = getIntent().getStringExtra("type");
        String name = getIntent().getStringExtra("name");

        recyclerView = findViewById(R.id.view_all);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(this,viewAllModelList);
        recyclerView.setAdapter(viewAllAdapter);

        product = findViewById(R.id.view_product);
        product.setText(name);



        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkmon, new Response.Listener<String>() {
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
                            viewAllAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewAllActivity.this, "Error"+type, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("maloai",type);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);

    }
}