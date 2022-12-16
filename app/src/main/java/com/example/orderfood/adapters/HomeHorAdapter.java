package com.example.orderfood.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.activities.ViewAllActivity;
import com.example.orderfood.models.HomeHorModel;

import java.util.List;

public class HomeHorAdapter extends RecyclerView.Adapter<HomeHorAdapter.ViewHolder>{

    Context context;
    List<HomeHorModel> homeHorModelList;

    public HomeHorAdapter(Context context, List<HomeHorModel> homeHorModelList) {
        this.context = context;
        this.homeHorModelList = homeHorModelList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_hor_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(homeHorModelList.get(position).getHinhanh()).into(holder.imageView);
        holder.name.setText(homeHorModelList.get(position).getTenloai());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mamon = homeHorModelList.get(position).getMaloai()+"";
                Intent intent = new Intent(context, ViewAllActivity.class);
                intent.putExtra("type",mamon);
                intent.putExtra("name",homeHorModelList.get(position).getTenloai());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeHorModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.hor_img);
            name = itemView.findViewById(R.id.hor_text);
        }
    }
}
