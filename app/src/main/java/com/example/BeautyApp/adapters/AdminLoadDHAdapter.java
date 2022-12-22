package com.example.BeautyApp.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BeautyApp.R;
import com.example.BeautyApp.admin.CTDHActivity;
import com.example.BeautyApp.models.MyOrderModel;

import java.util.List;

public class AdminLoadDHAdapter extends RecyclerView.Adapter<AdminLoadDHAdapter.ViewHolder> {
    Context context;
    List<MyOrderModel> myOrderModelList;
    String trangthai;

    public AdminLoadDHAdapter(Context context, List<MyOrderModel> myOrderModelList) {
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
        holder.tt.setText(myOrderModelList.get(position).getTt());
        if (myOrderModelList.get(position).getTt().equals("Đang giao.")){
            trangthai = "<font color='#1976D2'>Đang giao.</font>";
            holder.tt.setText(Html.fromHtml(trangthai));
        }
        if (myOrderModelList.get(position).getTt().equals("Đơn hàng đã bị hủy!")){
            trangthai = "<font color='#D84315'>Đơn hàng đã bị hủy!</font>";
            holder.tt.setText(Html.fromHtml(trangthai));
        }
        if (myOrderModelList.get(position).getTt().equals("Đã thanh toán.")){
            trangthai = "<font color='#393d40'>Đã thanh toán.</font>";
            holder.tt.setText(Html.fromHtml(trangthai));
        }
        if (myOrderModelList.get(position).getTt().equals("Chờ xử lý.")){
            trangthai = "<font color='#43A047'>Chờ xử lý.</font>";
            holder.tt.setText(Html.fromHtml(trangthai));
        }
        holder.sdt.setText(myOrderModelList.get(position).getSdt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CTDHActivity.class);
                intent.putExtra("mahd",myOrderModelList.get(position).getMahd()+"");
                intent.putExtra("sdt",myOrderModelList.get(position).getSdt());
                intent.putExtra("dc",myOrderModelList.get(position).getDc());
                intent.putExtra("tt",myOrderModelList.get(position).getTt());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myOrderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tt,date,sdt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tt = itemView.findViewById(R.id.my_orders_tt);
            date = itemView.findViewById(R.id.my_orders_date);
            sdt = itemView.findViewById(R.id.my_orders_sdt);
        }
    }
}
