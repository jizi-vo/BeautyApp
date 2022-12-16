package com.example.orderfood.ui.my_orders;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.orderfood.MainActivity;
import com.example.orderfood.R;
import com.example.orderfood.activities.LoginActivity;
import com.example.orderfood.activities.ViewAllActivity;
import com.example.orderfood.adapters.AdminLoadDHAdapter;
import com.example.orderfood.adapters.MyOrderAdapter;
import com.example.orderfood.admin.DonHangActivity;
import com.example.orderfood.models.MyOrderModel;
import com.example.orderfood.models.NewDishModel;
import com.example.orderfood.models.Server;
import com.example.orderfood.models.ViewAllModel;
import com.example.orderfood.ui.my_cart.MyCartFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.orderfood.MainActivity.myCartModelList;


public class MyOrdersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    RecyclerView recyclerView;
    MyOrderAdapter myOrderAdapter;
    public static List<MyOrderModel> myOrderModelList;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;

    public static LinearLayout linearLayout1,linearLayout2;
    int check = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_orders, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading...");
        linearLayout1 = root.findViewById(R.id.linear_my_order1);
        linearLayout2 = root.findViewById(R.id.linear_my_order2);

        swipeRefreshLayout = root.findViewById(R.id.my_order_swipe);

        recyclerView = root.findViewById(R.id.order_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myOrderModelList = new ArrayList<>();
        myOrderAdapter = new MyOrderAdapter(getActivity(),myOrderModelList);
        recyclerView.setAdapter(myOrderAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        loadbill();
        return root;
    }

    private void linear(int check) {
        if (check == 0){
            linearLayout1.setVisibility(View.VISIBLE);
        }else{
            linearLayout2.setVisibility(View.VISIBLE);
        }
    }

    private void loadbill() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                            check = 1;
                        }
                        linear(check);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("phone",MainActivity.userModelList.get(0).getSdt());
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    public void onRefresh() {
        myOrderModelList.clear();
        myOrderAdapter.notifyDataSetChanged();
        loadbill();
        swipeRefreshLayout.setRefreshing(false);
    }
}