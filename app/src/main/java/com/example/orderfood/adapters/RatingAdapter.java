package com.example.orderfood.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.MainActivity;
import com.example.orderfood.R;
import com.example.orderfood.activities.RatingActivity;
import com.example.orderfood.models.RatingModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {

    Context context;
    List<RatingModel> ratingModelList;

    public RatingAdapter(Context context, List<RatingModel> ratingModelList) {
        this.context = context;
        this.ratingModelList = ratingModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(ratingModelList.get(position).getImg())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(holder.imageView);
        holder.ten.setText(ratingModelList.get(position).getTen());
        holder.date.setText(ratingModelList.get(position).getDate());
        holder.cmt.setText(ratingModelList.get(position).getCmt());
        holder.ratingBar.setRating(Float.parseFloat(ratingModelList.get(position).getDanhgia()));
    }

    @Override
    public int getItemCount() {
        return ratingModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView ten,date,cmt;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.rating_img_rec);
            ten = itemView.findViewById(R.id.rating_ten_rec);
            date = itemView.findViewById(R.id.rating_date_rec);
            cmt = itemView.findViewById(R.id.rating_cmt_rec);
            ratingBar = itemView.findViewById(R.id.rating_raingbar_rec);
        }
    }
}
