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
import com.example.BeautyApp.models.ThongKeModel;

import java.text.DecimalFormat;
import java.util.List;

public class ThongKeAdapter extends RecyclerView.Adapter<ThongKeAdapter.ViewHolder> {

    Context context;
    List<ThongKeModel> thongKeModelList;

    public ThongKeAdapter(Context context, List<ThongKeModel> thongKeModelList) {
        this.context = context;
        this.thongKeModelList = thongKeModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.thongke_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(thongKeModelList.get(position).getImg()).into(holder.imageView);
        holder.name.setText(thongKeModelList.get(position).getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.price.setText(decimalFormat.format(thongKeModelList.get(position).getPrice())+"đ");
        holder.rating.setText(thongKeModelList.get(position).getRating());
        holder.quantity.setText(thongKeModelList.get(position).getQuantity()+"");
        DecimalFormat decimalFormat1 = new DecimalFormat("###.#");
        try{
            holder.percent.setText("Chiếm "+decimalFormat1.format(Float.parseFloat(thongKeModelList.get(position).getPercent()))+"%");
        }catch (Exception e){
            holder.percent.setText("Chiếm 0%");
        }
    }

    @Override
    public int getItemCount() {
        return thongKeModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,rating,price,quantity,percent;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.thongke_name);
            rating = itemView.findViewById(R.id.thongke_rating);
            price = itemView.findViewById(R.id.thongke_price);
            quantity = itemView.findViewById(R.id.thongke_quantity);
            imageView = itemView.findViewById(R.id.thongke_img);
            percent = itemView.findViewById(R.id.thongke_percent);
        }
    }
}
