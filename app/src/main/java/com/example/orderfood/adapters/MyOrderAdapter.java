package com.example.orderfood.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.orderfood.R;
import com.example.orderfood.Retrofit2.APIUtils;
import com.example.orderfood.Retrofit2.DataClient;
import com.example.orderfood.activities.BillDetailActivity;
import com.example.orderfood.admin.CTDHActivity;
import com.example.orderfood.admin.DonHangActivity;
import com.example.orderfood.models.MyOrderModel;
import com.example.orderfood.models.Server;
import com.example.orderfood.ui.my_cart.MyCartFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    Context context;
    List<MyOrderModel> myOrderModelList;
    String trangthai;
    ProgressDialog progressDialog;
    public MyOrderAdapter(Context context, List<MyOrderModel> myOrderModelList) {
        this.context = context;
        this.myOrderModelList = myOrderModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(myOrderModelList.get(position).getDate());
        if (myOrderModelList.get(position).getTt().equals("1")){
            trangthai = "<font color='#1976D2'>Chờ xử lý - Đang giao.</font>";
            holder.tt.setText(Html.fromHtml(trangthai));
        }
        if (myOrderModelList.get(position).getTt().equals("3")){
            trangthai = "<font color='#D84315'>Đơn hàng đã bị hủy!</font>";
            holder.tt.setText(Html.fromHtml(trangthai));
        }
        if (myOrderModelList.get(position).getTt().equals("2")){
            trangthai = "<font color='#393d40'>Đã thanh toán.</font>";
            holder.tt.setText(Html.fromHtml(trangthai));
        }
        holder.orderid.setText(myOrderModelList.get(position).getMahd().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BillDetailActivity.class);
                intent.putExtra("mahd",myOrderModelList.get(position).getMahd()+"");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Wait...");

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!myOrderModelList.get(position).getTt().equals("1")){
                    return false;
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Thông báo!");
                dialog.setMessage("Bạn có chắc muốn hủy đơn hàng?");

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
                        DataClient dataClient1 = APIUtils.getData();
                        Call<String> callBack1 = dataClient1.updateTrangThaiCTHD(myOrderModelList.get(position).getMahd()+"","3");
                        callBack1.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                String message = response.body();
                                if (message.equals("Success")){
                                }
                                else {
                                    Toast.makeText(context, "ttcthd"+response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(context, "ttcthd"+t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        DataClient dataClient = APIUtils.getData();
                        Call<String> callBack = dataClient.updateTrangThai(myOrderModelList.get(position).getMahd()+"","3");
                        callBack.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                String message = response.body();
                                if (message.equals("Success")){
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Hủy đơn hàng thành công!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, "Error"+response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(context, "Error"+t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return myOrderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tt,date,orderid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tt = itemView.findViewById(R.id.my_orders_tt);
            date = itemView.findViewById(R.id.my_orders_date);
            orderid = itemView.findViewById(R.id.my_orders_sdt);
        }
    }
}
