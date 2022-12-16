package com.example.orderfood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.orderfood.MainActivity;
import com.example.orderfood.R;
import com.example.orderfood.adapters.RatingAdapter;
import com.example.orderfood.models.MyOrderModel;
import com.example.orderfood.models.RatingModel;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class RatingActivity extends AppCompatActivity {

    ImageView img;
    public static CircleImageView imgUser;
    public static TextView userName,userDate,userCmt,ratingupdate;
    public static RatingBar userRatingBar;

    ProgressBar progressBar;
    ScrollView scrollView;

    public static TextView tenmon,daban,danhgia,songuoidanhgia;
    public static RatingBar ratingBar,ratingBarSmall;
    public static ProgressBar progressBar1,progressBar2,progressBar3,progressBar4,progressBar5;
    RecyclerView recyclerView;
    public static LinearLayout linearLayout,linearLayoutUser;

    public static RatingAdapter ratingAdapter;
    public static List<RatingModel> ratingModelList;

    String getmamon,gethinhanh,gettenmon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        userName = findViewById(R.id.rating_ten_user);
        imgUser = findViewById(R.id.rating_img_user);
        userDate = findViewById(R.id.rating_date_user);
        userCmt = findViewById(R.id.rating_cmt_user);
        userRatingBar = findViewById(R.id.rating_raingbar_user);
        ratingupdate = findViewById(R.id.rating_update);

        img = findViewById(R.id.rating_img);
        tenmon = findViewById(R.id.rating_mon);
        daban = findViewById(R.id.rating_daban);
        danhgia = findViewById(R.id.rating_danhgia);
        songuoidanhgia = findViewById(R.id.rating_songuoidanhgia);

        ratingBar = findViewById(R.id.ratingbar_danhgia);
        ratingBarSmall = findViewById(R.id.ratingsmall_danhgia);

        progressBar1 = findViewById(R.id.rating_progressbar1);
        progressBar2 = findViewById(R.id.rating_progressbar2);
        progressBar3 = findViewById(R.id.rating_progressbar3);
        progressBar4 = findViewById(R.id.rating_progressbar4);
        progressBar5 = findViewById(R.id.rating_progressbar5);

        recyclerView = findViewById(R.id.rating_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ratingModelList = new ArrayList<>();
        ratingAdapter = new RatingAdapter(this,ratingModelList);
        recyclerView.setAdapter(ratingAdapter);

        linearLayout = findViewById(R.id.rating_linear_danhgia);
        linearLayoutUser = findViewById(R.id.linear_rating_user);
        progressBar = findViewById(R.id.rating_progressbar);
        scrollView = findViewById(R.id.rating_scrollView);

        Intent intent = getIntent();
        getmamon = intent.getStringExtra("mamon");
        gethinhanh = intent.getStringExtra("img");
        gettenmon = intent.getStringExtra("tenmon");

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        tenmon.setText(gettenmon);
        Glide.with(this).load(gethinhanh).into(img);

        checkRating();
        loadRating();
        loadCmt();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Intent intent1 = new Intent(RatingActivity.this,UserRatingActivity.class);
                intent1.putExtra("rating",ratingBar.getRating()+"");
                intent1.putExtra("mamon",getmamon);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }
        });

        ratingupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(RatingActivity.this,UpdateRatingActivity.class);
                intent1.putExtra("mamon",getmamon);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }
        });



    }

    private void loadCmt() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkloadrecrating, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    String img = "";
                    String ten = "";
                    String danhgia = "";
                    String date = "";
                    String cmt = "";
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            img = jsonObject.getString("hinhanh");
                            ten = jsonObject.getString("tentk");
                            danhgia = jsonObject.getString("rating");
                            date = jsonObject.getString("date");
                            cmt = jsonObject.getString("cmt");

                            ratingModelList.add(new RatingModel(img,ten,danhgia,date,cmt));
                            ratingAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RatingActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("mamon",getmamon);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadRating() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkloadrating, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response!=null){
                    try {
                        double rating1,rating2,rating3,rating4,rating5,tong;
                        int getdaban;
                        float rating;
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            rating1 = jsonObject.getDouble("rating1");
                            rating2 = jsonObject.getDouble("rating2");
                            rating3 = jsonObject.getDouble("rating3");
                            rating4 = jsonObject.getDouble("rating4");
                            rating5 = jsonObject.getDouble("rating5");
                            tong = jsonObject.getDouble("tong");
                            try {
                                getdaban = jsonObject.getInt("daban");
                            }catch (Exception e){
                                getdaban = 0;
                            }

                            daban.setText("Đã bán: "+getdaban);

                            if (tong != 0 ){
                                songuoidanhgia.setText((int)tong+"");
                                progressBar1.setProgress((int)((rating1/tong)*100));
                                progressBar2.setProgress((int)((rating2/tong)*100));
                                progressBar3.setProgress((int)((rating3/tong)*100));
                                progressBar4.setProgress((int)((rating4/tong)*100));
                                progressBar5.setProgress((int)((rating5/tong)*100));

                                DecimalFormat decimalFormat = new DecimalFormat("#.#");

                                rating = (float) (((rating1*1)+(rating2*2)+(rating3*3)+(rating4*4)+(rating5*5))/tong);
                                danhgia.setText(decimalFormat.format(rating)+"");
                                ratingBarSmall.setRating(rating);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RatingActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String, String>();
                hashMap.put("mamon",getmamon);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void checkRating() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkcheckrating, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response!=null){
                    try {
                        int checkdamua = 0,checkdanhgia = 0;
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            checkdanhgia = jsonObject.getInt("checkdanhgia");
                            try {
                                checkdamua = jsonObject.getInt("checkdamua");
                            }catch (Exception e){
                                checkdamua = 0;
                            }
                        }
                        if (checkdamua != 0 && checkdanhgia == 0){
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                        if (checkdanhgia == 1){
                            loadRatingUser();
                            linearLayoutUser.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("matk",MainActivity.userModelList.get(0).getMatk()+"");
                hashMap.put("mamon",getmamon);
                hashMap.put("sdt",MainActivity.userModelList.get(0).getSdt());
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadRatingUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkloadratinguser, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    String img = "";
                    String ten = "";
                    String danhgia = "";
                    String date = "";
                    String cmt = "";
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            img = jsonObject.getString("hinhanh");
                            ten = jsonObject.getString("tentk");
                            danhgia = jsonObject.getString("rating");
                            date = jsonObject.getString("date");
                            cmt = jsonObject.getString("cmt");

                            Glide.with(getApplicationContext())
                                    .load(img)
                                    .placeholder(R.drawable.user)
                                    .error(R.drawable.user)
                                    .into(imgUser);
                            userName.setText(ten);
                            userRatingBar.setRating(Float.parseFloat(danhgia));
                            userDate.setText(date);
                            userCmt.setText(cmt);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RatingActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("mamon",getmamon);
                hashMap.put("matk",MainActivity.userModelList.get(0).getMatk()+"");
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }
}