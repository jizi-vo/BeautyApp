package com.example.BeautyApp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.BeautyApp.MainActivity;
import com.example.BeautyApp.R;
import com.example.BeautyApp.activities.RatingActivity;
import com.example.BeautyApp.models.MyCartModel;
import com.example.BeautyApp.models.ViewAllModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.BeautyApp.MainActivity.myCartModelList;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder> {
    Context context;
    List<ViewAllModel> viewAllModelList;


    public ViewAllAdapter(Context context, List<ViewAllModel> viewAllModelList) {
        this.context = context;
        this.viewAllModelList = viewAllModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(viewAllModelList.get(position).getHinhanh()).into(holder.imageView);
        holder.name.setText(viewAllModelList.get(position).getTenmon());
        holder.rating.setText(viewAllModelList.get(position).getDanhgia());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.price.setText(decimalFormat.format(viewAllModelList.get(position).getGia())+"đ");
        holder.ratingBar.setRating(Float.parseFloat(viewAllModelList.get(position).getDanhgia()));


        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalQuantity = Integer.parseInt(holder.quantity.getText().toString());
                if ( totalQuantity < 10){
                    totalQuantity ++;
                    holder.quantity.setText(totalQuantity+"");
                }
            }
        });
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalQuantity = Integer.parseInt(holder.quantity.getText().toString());
                if ( totalQuantity > 1){
                    totalQuantity --;
                    holder.quantity.setText(totalQuantity+"");
                }
            }
        });

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (myCartModelList != null){

                }else {
                    myCartModelList = new ArrayList<>();
                }
                int mamon,maloai,gia;
                String tenmon,danhgia,hinhanh;
                mamon = viewAllModelList.get(position).getMamon();
                tenmon = viewAllModelList.get(position).getTenmon();
                gia = viewAllModelList.get(position).getGia();
                danhgia = viewAllModelList.get(position).getDanhgia();
                maloai = viewAllModelList.get(position).getMaloai();
                hinhanh = viewAllModelList.get(position).getHinhanh();

                int totalQuantity = Integer.parseInt(holder.quantity.getText().toString());
                int totalPrice;
                if (MainActivity.myCartModelList.size() > 0) {
                    boolean checkadd = false;
                    for (int i = 0; i < MainActivity.myCartModelList.size(); i++) {
                        if (MainActivity.myCartModelList.get(i).getMamon() == mamon) {
                            MainActivity.myCartModelList.get(i).setSoluong(MainActivity.myCartModelList.get(i).getSoluong() + totalQuantity);
                            if (MainActivity.myCartModelList.get(i).getSoluong() > 10) {
                                MainActivity.myCartModelList.get(i).setSoluong(10);
                            }
                            totalPrice = gia * MainActivity.myCartModelList.get(i).getSoluong();
                            MainActivity.myCartModelList.get(i).setGia(totalPrice);
                            checkadd = true;
                        }
                    }
                    if (!checkadd){
                        totalPrice = gia*totalQuantity;
                        MainActivity.myCartModelList.add(new MyCartModel(mamon, tenmon, totalPrice, danhgia, maloai, totalQuantity, hinhanh));
                    }
                } else {
                    totalPrice = gia*totalQuantity;
                    MainActivity.myCartModelList.add(new MyCartModel(mamon, tenmon, totalPrice, danhgia, maloai, totalQuantity, hinhanh));
                }
                Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RatingActivity.class);
                intent.putExtra("mamon",viewAllModelList.get(position).getMamon()+"");
                intent.putExtra("tenmon",viewAllModelList.get(position).getTenmon());
                intent.putExtra("img",viewAllModelList.get(position).getHinhanh());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return viewAllModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,addItem,removeItem;
        TextView name,rating,price,quantity;
        Button addToCart;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.view_all_img);
            name = itemView.findViewById(R.id.view_all_name);
            rating = itemView.findViewById(R.id.view_all_rating);
            price = itemView.findViewById(R.id.view_all_price);
            quantity = itemView.findViewById(R.id.view_all_quantity);
            removeItem = itemView.findViewById(R.id.view_all_remove);
            addItem = itemView.findViewById(R.id.view_all_add);
            addToCart = itemView.findViewById(R.id.view_all_btn_add);
            ratingBar = itemView.findViewById(R.id.viewall_ratingbar);
        }
    }
}
