package com.example.BeautyApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.BeautyApp.R;
import com.example.BeautyApp.models.BillDetailModel;

import java.text.DecimalFormat;
import java.util.List;

public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailAdapter.ViewHolder> {
    Context context;
    List<BillDetailModel> billDetailModelList;

    public BillDetailAdapter(Context context, List<BillDetailModel> billDetailModelList) {
        this.context = context;
        this.billDetailModelList = billDetailModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(billDetailModelList.get(position).getHinhanh()).into(holder.myCartItem);
        holder.name.setText(billDetailModelList.get(position).getTenmon());
        holder.quantity.setText(billDetailModelList.get(position).getSl()+"");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.price.setText(decimalFormat.format(billDetailModelList.get(position).getGia())+"đ");
    }

    @Override
    public int getItemCount() {
        return billDetailModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView myCartItem;
        TextView quantity,price,name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myCartItem = itemView.findViewById(R.id.my_cart_img);
            name = itemView.findViewById(R.id.my_cart_name);
            price = itemView.findViewById(R.id.my_cart_price);
            quantity = itemView.findViewById(R.id.my_cart_quantity);
        }
    }
}
