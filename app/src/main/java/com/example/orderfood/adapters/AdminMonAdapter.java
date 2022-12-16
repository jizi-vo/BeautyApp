package com.example.orderfood.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.Retrofit2.APIUtils;
import com.example.orderfood.Retrofit2.DataClient;
import com.example.orderfood.activities.RegistrationActivity;
import com.example.orderfood.activities.UpdateMonActivity;
import com.example.orderfood.admin.MonActivity;
import com.example.orderfood.models.HomeHorModel;
import com.example.orderfood.models.Server;
import com.example.orderfood.models.ViewAllModel;
import com.example.orderfood.ui.my_cart.MyCartFragment;

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

public class AdminMonAdapter extends RecyclerView.Adapter<AdminMonAdapter.ViewHolder> {
    Context context;
    List<ViewAllModel> viewAllModelList;
    Spinner spinner;
    int maloai;
    String selecttenloai;
    ArrayAdapter arrayAdapterTenLoai;

    ProgressDialog progressDialog;
    public AdminMonAdapter(Context context, List<ViewAllModel> viewAllModelList) {
        this.context = context;
        this.viewAllModelList = viewAllModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_mon_view_all_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Glide.with(context).load(viewAllModelList.get(position).getHinhanh()).into(holder.imageView);
        holder.name.setText(viewAllModelList.get(position).getTenmon());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.price.setText(decimalFormat.format(viewAllModelList.get(position).getGia())+"đ");
        holder.rating.setText(viewAllModelList.get(position).getDanhgia());
        if (viewAllModelList.get(position).getTt() == 1){
            holder.cardView.setBackgroundResource(R.drawable.cardview);
        }else {
            holder.cardView.setBackgroundResource(R.drawable.cardview_default);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewAllModelList.get(position).getTt() == 0){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Cảnh báo!");
                    dialog.setMessage("Bạn muốn vô hiệu hóa món này? \nCác món bị vô hiệu hóa sẽ không được hiển thị trong cửa hàng!");
                    dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });

                    dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            progressDialog.show();
                            DataClient dataClient = APIUtils.getData();
                            retrofit2.Call<String> callBack = dataClient.updatettmon(1,viewAllModelList.get(position).getMamon()+"");
                            callBack.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                    String message = response.body();
                                    if (message.equals("Success")){
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Đã vô hiệu hóa món!", Toast.LENGTH_SHORT).show();
                                        reloadMon();
                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        }
                    });
                    dialog.show();
                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Cảnh báo!");
                    dialog.setMessage("Bạn muốn kích hoạt lại món này? \nSau đi được kích hoạt món sẽ được hiển thị trở lại trong cửa hàng!");
                    dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });

                    dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            progressDialog.show();
                            DataClient dataClient = APIUtils.getData();
                            retrofit2.Call<String> callBack = dataClient.updatettmon(0,viewAllModelList.get(position).getMamon()+"");
                            callBack.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                    String message = response.body();
                                    if (message.equals("Success")){
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Đã kích hoạt lại món!", Toast.LENGTH_SHORT).show();
                                        reloadMon();
                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        }
                    });
                    dialog.show();
                }
            }
        });
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Wait...");
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateMonActivity.class);
                intent.putExtra("mamon",viewAllModelList.get(position).getMamon()+"");
                intent.putExtra("maloai",viewAllModelList.get(position).getMaloai()+"");
                intent.putExtra("tenmon",viewAllModelList.get(position).getTenmon());
                intent.putExtra("gia",viewAllModelList.get(position).getGia()+"");
                intent.putExtra("img",viewAllModelList.get(position).getHinhanh());
                context.startActivity(intent);
//                openUpdateDialog(Gravity.BOTTOM,position);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMon(position);
            }
        });
    }

    private void deleteMon(int position) {
        try{
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Cảnh báo!");
            dialog.setMessage("Bạn có muốn xóa món này?");
            dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });

            dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    progressDialog.show();
                    DataClient dataClient = APIUtils.getData();
                    retrofit2.Call<String> callBack = dataClient.xoaMon(viewAllModelList.get(position).getMamon()+"");
                    callBack.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            String message = response.body();
                            if (message.equals("Success")){
                                progressDialog.dismiss();
                                Toast.makeText(context, "Xóa món thành công!", Toast.LENGTH_SHORT).show();
                                reloadMon();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(context, "Không thể xóa!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            });
            dialog.show();
        }catch (Exception exception){

        }
    }


    private void openUpdateDialog(int gravity,int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.admin_dialog_mon);

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

        EditText dialogName = dialog.findViewById(R.id.mon_dialog_name);
        EditText dialogPrice = dialog.findViewById(R.id.mon_dialog_price);
        TextView dialogUpdate = dialog.findViewById(R.id.mon_dialog_update);
        TextView dialogCancel = dialog.findViewById(R.id.mon_dialog_cancel);
        spinner = dialog.findViewById(R.id.spinner_mon_loaimon_update);

        //////////////load spinner
        final List<HomeHorModel> homeHorModelList = new ArrayList<>();
        final ArrayList<String> arrayTenloai = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                            arrayTenloai.add(tenloai);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    arrayAdapterTenLoai = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,arrayTenloai);
                    spinner.setAdapter(arrayAdapterTenLoai);

                    for (int i = 0;i<homeHorModelList.size();i++){
                        if (homeHorModelList.get(i).getMaloai() == viewAllModelList.get(position).getMaloai()){
                            selecttenloai = homeHorModelList.get(i).getTenloai();
                        }
                    }
                    spinner.setSelection(arrayAdapterTenLoai.getPosition(selecttenloai));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                maloai = homeHorModelList.get(i).getMaloai();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialogName.setText(viewAllModelList.get(position).getTenmon());
        dialogPrice.setText(viewAllModelList.get(position).getGia()+"");

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                progressDialog.show();
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkadminupdatemon, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        if (response.equals("Success")){
                            progressDialog.dismiss();
                            dialog.setMessage("Cập nhật thành công!");
                            reloadMon();
                        }else{
                            progressDialog.dismiss();
                            dialog.setMessage("Tên món đã tồn tại!");
                        }
                        dialog.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dialog.show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> hashMap = new HashMap<String,String>();
                        hashMap.put("mamon",viewAllModelList.get(position).getMamon()+"");
                        hashMap.put("tenmon",dialogName.getText().toString());
                        hashMap.put("maloai",maloai+"");
                        hashMap.put("gia",dialogPrice.getText().toString());
                        return hashMap;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        dialog.show();
    }


    private void reloadMon() {
        MonActivity.viewAllModelList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.linkadminmon, new Response.Listener<String>() {
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
                            MonActivity.viewAllModelList.add(new ViewAllModel(mamon,tenmon,gia,danhgia,maloai,hinhanh,tt));
                            MonActivity.adminMonAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return viewAllModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,rating;
        ImageView imageView;
        Button delete,update;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.admin_mon_img);
            name = itemView.findViewById(R.id.admin_mon_name);
            price = itemView.findViewById(R.id.admin_mon_price);
            rating = itemView.findViewById(R.id.admin_mon_rating);
            delete = itemView.findViewById(R.id.admin_mon_xoa);
            update = itemView.findViewById(R.id.admin_mon_update);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
