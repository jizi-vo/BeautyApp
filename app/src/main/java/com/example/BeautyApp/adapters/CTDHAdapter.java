package com.example.BeautyApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BeautyApp.R;
import com.example.BeautyApp.models.BillDetailModel;

import java.text.DecimalFormat;
import java.util.List;

public class CTDHAdapter extends RecyclerView.Adapter<CTDHAdapter.ViewHolder> {
    Context context;
    List<BillDetailModel> billDetailModelList;

    public CTDHAdapter(Context context, List<BillDetailModel> billDetailModelList) {
        this.context = context;
        this.billDetailModelList = billDetailModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_ctdh_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tenmon.setText(billDetailModelList.get(position).getTenmon());
        holder.sl.setText(billDetailModelList.get(position).getSl()+"");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.gia.setText(decimalFormat.format(billDetailModelList.get(position).getGia())+"đ");
    }

    @Override
    public int getItemCount() {
        return billDetailModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenmon,sl,gia;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenmon = itemView.findViewById(R.id.ctdh_temon);
            sl = itemView.findViewById(R.id.ctdh_sl);
            gia = itemView.findViewById(R.id.ctdh_gia);
        }
    }
}
