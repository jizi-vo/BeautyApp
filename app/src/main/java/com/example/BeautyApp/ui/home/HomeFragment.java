package com.example.BeautyApp.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.BeautyApp.R;
import com.example.BeautyApp.adapters.HomeHorAdapter;
import com.example.BeautyApp.adapters.NewDishAdapter;
import com.example.BeautyApp.adapters.PopularAdapter;
import com.example.BeautyApp.adapters.ViewAllAdapter;
import com.example.BeautyApp.models.HomeHorModel;
import com.example.BeautyApp.models.NewMyphamModel;
import com.example.BeautyApp.models.PopularModel;
import com.example.BeautyApp.models.Server;
import com.example.BeautyApp.models.ViewAllModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.BeautyApp.MainActivity.myCartModelList;

public class HomeFragment extends Fragment {

    ScrollView scrollView;
    ProgressBar progressBar;

    RecyclerView homehorRec,popRec,newRec;

    /////New Dish
    List<NewMyphamModel> newDishModelList;
    NewDishAdapter newDishAdapter;

    /////Popular
    List<PopularModel> popularModelList;
    PopularAdapter popularAdapter;

    /////Categories
    List<HomeHorModel> homeHorModelList;
    HomeHorAdapter homeHorAdapter;

    /////////Search View
    EditText searchBox;
    private List<ViewAllModel> viewAllModelList;
    private RecyclerView recyclerViewSearch;
    private ViewAllAdapter viewAllAdapter;

    ////////////////
    TextView HiUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        if (myCartModelList != null){

        }else {
            myCartModelList = new ArrayList<>();
        }
        String userFullName;
        Intent intent = getActivity().getIntent();
        userFullName = intent.getStringExtra("userName");
        String[] userName = userFullName.split(" ");
        HiUser = root.findViewById(R.id.home_user_name);
        HiUser.setText("Hello "+userName[userName.length-1]);
        ///////Progressbar
        progressBar = root.findViewById(R.id.progressbar_main);
        scrollView = root.findViewById(R.id.scroll_view);


        ////////Home Categories
        homehorRec = root.findViewById(R.id.home_hor_rec);
        homehorRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        homeHorModelList = new ArrayList<>();
        homeHorAdapter = new HomeHorAdapter(getActivity(),homeHorModelList);
        homehorRec.setAdapter(homeHorAdapter);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        ViewCategories();


        /////////Home Popular
        popRec = root.findViewById(R.id.home_pop_rec);

        popRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        if (popularModelList != null){

        }else {
            popularModelList = new ArrayList<>();
        }
        popularAdapter = new PopularAdapter(getActivity(),popularModelList);
        popRec.setAdapter(popularAdapter);

        ViewPopular();

        ////////Home New Dish
        newRec = root.findViewById(R.id.home_new_rec);

        newRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        if (newDishModelList != null){

        }else {
            newDishModelList = new ArrayList<>();
        }

        newDishAdapter = new NewDishAdapter(getActivity(),newDishModelList);
        newRec.setAdapter(newDishAdapter);

        ViewNewDish();

        /////////Search View
        recyclerViewSearch = root.findViewById(R.id.search_rec);
        searchBox = root.findViewById(R.id.search_box);
        if (viewAllModelList != null){

        }else {
            viewAllModelList = new ArrayList<>();
        }

        viewAllAdapter = new ViewAllAdapter(getContext(),viewAllModelList);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(viewAllAdapter);
        recyclerViewSearch.setHasFixedSize(true);
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
                    viewAllModelList.clear();
                    viewAllAdapter.notifyDataSetChanged();
                }else {
                    searchProduct(editable.toString());
                }
            }
        });

        return root;
    }

    private void searchProduct(String type) {
        if (!type.isEmpty()) {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linksearchmon, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        int mamon;
                        String tenmon;
                        int gia;
                        String danhgia;
                        int maloai;
                        String hinhanh;
                        int tt;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            viewAllModelList.clear();
                            viewAllAdapter.notifyDataSetChanged();
                            for (int i = 0; i < jsonArray.length(); i++) {
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
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("tenmon", type);
                    return hashMap;
                }
            };
            requestQueue.add(stringRequest);
        }
        
    }


    private void ViewNewDish() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.linkmonmoi, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null){
                    int mamon;
                    String tenmon;
                    int gia;
                    String danhgia;
                    int maloai;
                    String hinhanh;
                    for (int i = 0;i <response.length();i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            mamon = jsonObject.getInt("mamon");
                            tenmon = jsonObject.getString("tenmon");
                            gia = jsonObject.getInt("gia");
                            danhgia = jsonObject.getString("danhgia");
                            maloai = jsonObject.getInt("maloai");
                            hinhanh = jsonObject.getString("hinhanh");
                            newDishModelList.add(new NewMyphamModel(mamon,tenmon,gia,danhgia,maloai,hinhanh));
                            newDishAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void ViewCategories() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.linkloaimon, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null){
                    int maloai;
                    String tenloai;
                    String hinhanh;
                    for (int i = 0;i <response.length();i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            maloai = jsonObject.getInt("maloai");
                            tenloai = jsonObject.getString("tenloai");
                            hinhanh = jsonObject.getString("hinhanh");
                            homeHorModelList.add(new HomeHorModel(maloai,tenloai,hinhanh));
                            homeHorAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);

    }

    private void ViewPopular() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.linkpopular, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null){
                    int mamon;
                    String tenmon;
                    int gia;
                    String danhgia;
                    int maloai;
                    String hinhanh;
                    for (int i = 0;i <response.length();i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            mamon = jsonObject.getInt("mamon");
                            tenmon = jsonObject.getString("tenmon");
                            gia = jsonObject.getInt("gia");
                            danhgia = jsonObject.getString("danhgia");
                            maloai = jsonObject.getInt("maloai");
                            hinhanh = jsonObject.getString("hinhanh");
                            popularModelList.add(new PopularModel(mamon,tenmon,gia,danhgia,maloai,hinhanh));
                            popularAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

}