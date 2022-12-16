package com.example.orderfood.ui.my_cart;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.orderfood.MainActivity;
import com.example.orderfood.R;
import com.example.orderfood.adapters.MyCartAdapter;
import com.example.orderfood.models.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.orderfood.MainActivity.myCartModelList;

public class MyCartFragment extends Fragment {

    static TextView overTotalAmount;

    RecyclerView recyclerView;
    MyCartAdapter myCartAdapter;


    TextView dialogCancel,dialogConfirm;
    EditText dialogAddress;
    String Address;
    String saveCurrentDate,saveCurrentTime;
    ProgressDialog progressDialog;

    public static LinearLayout linearLayout1,linearLayout2;

//    ProgressBar progressBar;
    Button buyNow;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_cart, container, false);
        recyclerView = root.findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        overTotalAmount = root.findViewById(R.id.my_cart_total);
        buyNow = root.findViewById(R.id.my_cart_btn);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Wait...");

        linearLayout1 = root.findViewById(R.id.linear1);
        linearLayout2 = root.findViewById(R.id.linear2);

        if (myCartModelList != null){

        }else {
            myCartModelList = new ArrayList<>();

        }
        myCartAdapter = new MyCartAdapter(getActivity(),myCartModelList);
        recyclerView.setAdapter(myCartAdapter);


        constraint();
        //////totalPrice MyCart
        totalPrice();

        /////Buy Now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyNow(Gravity.BOTTOM);
            }
        });
        return root;
    }

    public static void constraint() {
        if (myCartModelList.size() <=0){
            linearLayout2.setVisibility(View.VISIBLE);
            linearLayout1.setVisibility(View.INVISIBLE);
        }else {
            linearLayout2.setVisibility(View.INVISIBLE);
            linearLayout1.setVisibility(View.VISIBLE);
        }
    }

    private void buyNow(int gravity) {
        if (myCartModelList.size()>0){
            String totalPrice = overTotalAmount.getText().toString();
            String totalPrice2 = totalPrice.substring(0,totalPrice.length() - 1);
            UUID uuid=UUID.randomUUID();
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_dc);

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

            dialogAddress = dialog.findViewById(R.id.dialog_my_cart_address);
            dialogCancel = dialog.findViewById(R.id.dialog_my_cart_huy);
            dialogConfirm = dialog.findViewById(R.id.dialog_my_cart_xacnhan);

            dialogAddress.setText(MainActivity.userModelList.get(0).getDiachi());

            dialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialogConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progressDialog.show();
                    Address = dialogAddress.getText().toString();
                    Calendar calendar = Calendar.getInstance();

                    final SimpleDateFormat currentDate = new SimpleDateFormat("yyyy/MM/dd");
                    saveCurrentDate = currentDate.format(calendar.getTime());

                    final SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                    saveCurrentTime = currentTime.format(calendar.getTime());

                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkhoadon, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            chitietHoaDon(response);
                            dialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Hoadon", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> hashMap = new HashMap<String,String>();
                            hashMap.put("userid", String.valueOf(MainActivity.userModelList.get(0).getMatk()));
                            hashMap.put("date",saveCurrentDate+" "+saveCurrentTime);
                            hashMap.put("dc",Address);
                            hashMap.put("mahd", String.valueOf(uuid));
                            hashMap.put("tongtien", totalPrice2);
                            return hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            });
            dialog.show();

        }else{
            Toast.makeText(getActivity(), "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
        }
    }

    private void chitietHoaDon(String hoadon) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkcthd, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("1")){
                    myCartModelList.clear();
                    myCartAdapter.notifyDataSetChanged();
                    MyCartFragment.totalPrice();
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    constraint();
                }else{
                    Toast.makeText(getActivity(), "Không thể đặt hàng!"+response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "CTHD"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONArray jsonArray = new JSONArray();
                for (int i=0;i< myCartModelList.size();i++){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("mahd",hoadon);
                        jsonObject.put("mamon",myCartModelList.get(i).getMamon());
                        jsonObject.put("tenmon",myCartModelList.get(i).getTenmon());
                        jsonObject.put("sl",myCartModelList.get(i).getSoluong());
                        jsonObject.put("gia",myCartModelList.get(i).getGia());
                        jsonObject.put("danhgia",myCartModelList.get(i).getDanhgia());
                        jsonObject.put("hinhanh",myCartModelList.get(i).getHinhanh());
                        jsonObject.put("date",saveCurrentDate+" "+saveCurrentTime);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("json",jsonArray.toString());
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }



    public static void totalPrice() {

        int totalPrice = 0;
        for (int i = 0;i< myCartModelList.size();i++){
            totalPrice = totalPrice + myCartModelList.get(i).getGia();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        overTotalAmount.setText(decimalFormat.format(totalPrice)+"đ");

    }

}