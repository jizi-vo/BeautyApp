package com.example.BeautyApp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.BeautyApp.MainActivity;
import com.example.BeautyApp.R;
import com.example.BeautyApp.Retrofit2.APIUtils;
import com.example.BeautyApp.Retrofit2.DataClient;
import com.example.BeautyApp.models.RatingModel;
import com.example.BeautyApp.models.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRatingActivity extends AppCompatActivity {

    CircleImageView imageView;
    TextView ten,dang,maxlenght;
    EditText cmt;
    RatingBar ratingBar;

    float rating;
    String mamon,saveCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rating);

        imageView = findViewById(R.id.user_rating_img);
        ten = findViewById(R.id.user_rating_ten);
        cmt = findViewById(R.id.user_rating_cmt);
        dang = findViewById(R.id.user_rating_dang);
        ratingBar = findViewById(R.id.user_rating_ratingbar);
        maxlenght = findViewById(R.id.user_rating_maxlenght);

        Intent intent = getIntent();
        rating = Float.parseFloat(intent.getStringExtra("rating"));
        mamon = intent.getStringExtra("mamon");

        Glide.with(this)
                .load(MainActivity.userModelList.get(0).getHinhanh())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(imageView);
        ten.setText(MainActivity.userModelList.get(0).getTentk());
        ratingBar.setRating(rating);

        Calendar calendar = Calendar.getInstance();

        final SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd");
        saveCurrentDate = currentDate.format(calendar.getTime());

        cmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                maxlenght.setText(s.length() +"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialog(Gravity.CENTER);

                DataClient dataClient = APIUtils.getData();
                Call<String> callBack = dataClient.uploadRating(mamon,MainActivity.userModelList.get(0).getMatk()+"",(int)ratingBar.getRating()+"",cmt.getText().toString(),saveCurrentDate);
                callBack.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        if (result.equals("Success")){
                            reloadRecRating();
                            reloadRating();
                            RatingActivity.linearLayoutUser.setVisibility(View.VISIBLE);
                            reloadCmt();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            }
        });
    }

    private void reloadCmt() {
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

                            RatingActivity.userRatingBar.setRating(Float.parseFloat(danhgia));
                            RatingActivity.userDate.setText(date);
                            RatingActivity.userCmt.setText(cmt);
                            RatingActivity.userName.setText(ten);
                            Glide.with(getApplicationContext())
                                    .load(img)
                                    .placeholder(R.drawable.user)
                                    .error(R.drawable.user)
                                    .into(RatingActivity.imgUser);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserRatingActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("mamon",mamon);
                hashMap.put("matk",MainActivity.userModelList.get(0).getMatk()+"");
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void reloadRating() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkloadrating, new com.android.volley.Response.Listener<String>() {
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

                            RatingActivity.daban.setText("Đã bán: "+getdaban);

                            if (tong != 0 ){
                                RatingActivity.songuoidanhgia.setText((int)tong+"");
                                RatingActivity.progressBar1.setProgress((int)((rating1/tong)*100));
                                RatingActivity.progressBar2.setProgress((int)((rating2/tong)*100));
                                RatingActivity.progressBar3.setProgress((int)((rating3/tong)*100));
                                RatingActivity.progressBar4.setProgress((int)((rating4/tong)*100));
                                RatingActivity.progressBar5.setProgress((int)((rating5/tong)*100));

                                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                                RatingActivity.danhgia.setText(decimalFormat.format((((rating1*1)+(rating2*2)+(rating3*3)+(rating4*4)+(rating5*5))/tong))+"");

                                rating = (float) (((rating1*1)+(rating2*2)+(rating3*3)+(rating4*4)+(rating5*5))/tong);
                                RatingActivity.ratingBarSmall.setRating(rating);

                                DataClient dataClient = APIUtils.getData();
                                retrofit2.Call<String> callBack = dataClient.updaterating(mamon,decimalFormat.format((((rating1*1)+(rating2*2)+(rating3*3)+(rating4*4)+(rating5*5))/tong))+"");
                                callBack.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                        String message = response.body();
                                        if (message.equals("Success")){

                                        }else{

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {

                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserRatingActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String, String>();
                hashMap.put("mamon",mamon);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void reloadRecRating() {
        RatingActivity.ratingModelList.clear();
        RatingActivity.ratingAdapter.notifyDataSetChanged();
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

                            RatingActivity.ratingModelList.add(new RatingModel(img,ten,danhgia,date,cmt));
                            RatingActivity.ratingAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserRatingActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("mamon",mamon);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rating);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == gravity){
            dialog.setCancelable(false);
        }else {
            dialog.setCancelable(false);
        }

        TextView dialogHoanTat = dialog.findViewById(R.id.dialog_rating_hoantat);

        dialogHoanTat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                RatingActivity.linearLayout.setVisibility(View.GONE);
                finish();
            }
        });
        dialog.show();
    }

}