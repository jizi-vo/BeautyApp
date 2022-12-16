package com.example.orderfood.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.MainActivity;
import com.example.orderfood.R;
//import com.example.orderfood.activities.DetailedActivity;
import com.example.orderfood.activities.RatingActivity;
import com.example.orderfood.models.MyCartModel;
import com.example.orderfood.models.PopularModel;

import java.text.DecimalFormat;
import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {
    Context context;
    List<PopularModel> popularModelList;

    int totalQuantity = 1;
    int totalPrice = 0;
    public PopularAdapter(Context context, List<PopularModel> popularModelList) {
        this.context = context;
        this.popularModelList = popularModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_popular_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(popularModelList.get(position).getHinhanh()).into(holder.popImg);
        holder.name.setText(popularModelList.get(position).getTenmon());
        holder.rating.setText(popularModelList.get(position).getDanhgia()+"");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.price.setText(decimalFormat.format(popularModelList.get(position).getGia())+"đ");
        holder.ratingBar.setRating(Float.parseFloat(popularModelList.get(position).getDanhgia()));

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(Gravity.BOTTOM,position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RatingActivity.class);
                intent.putExtra("mamon",popularModelList.get(position).getMamon()+"");
                intent.putExtra("tenmon",popularModelList.get(position).getTenmon());
                intent.putExtra("img",popularModelList.get(position).getHinhanh());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
    private void addToCart(int gravity,int position) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.home_add_dialog);

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

        TextView dialogName = dialog.findViewById(R.id.home_add_dialog_name);
        TextView dialogPrice = dialog.findViewById(R.id.home_add_dialog_price);
        TextView dialogRating = dialog.findViewById(R.id.home_add_dialog_rating);
        TextView dialogQuantity = dialog.findViewById(R.id.home_add_dialog_quantity);
        ImageView dialogAdd = dialog.findViewById(R.id.home_add_dialog_add);
        ImageView dialogRemove = dialog.findViewById(R.id.home_add_dialog_remove);
        ImageView dialogImg = dialog.findViewById(R.id.home_add_dialog_img);
        Button dialogButton = dialog.findViewById(R.id.home_add_dialog_btn_add);

        Glide.with(context).load(popularModelList.get(position).getHinhanh()).into(dialogImg);
        dialogName.setText(popularModelList.get(position).getTenmon());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        dialogPrice.setText(decimalFormat.format(popularModelList.get(position).getGia())+"đ");
        dialogRating.setText(popularModelList.get(position).getDanhgia());


        dialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity < 10) {
                    totalQuantity++;
                    dialogQuantity.setText(String.valueOf(totalQuantity));
                    totalPrice = popularModelList.get(position).getGia() * totalQuantity;
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    dialogPrice.setText(decimalFormat.format(totalPrice) + "đ");
                }
            }
        });
        dialogRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity > 1) {
                    totalQuantity--;
                    dialogQuantity.setText(String.valueOf(totalQuantity));
                    totalPrice = popularModelList.get(position).getGia() * totalQuantity;
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    dialogPrice.setText(decimalFormat.format(totalPrice) + "đ");
                }
            }
        });

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mamon = 0,maloai=0,gia=0;
                String tenmon="",danhgia="",hinhanh="";
                mamon = popularModelList.get(position).getMamon();
                tenmon = popularModelList.get(position).getTenmon();
                gia = popularModelList.get(position).getGia();
                danhgia = popularModelList.get(position).getDanhgia();
                maloai = popularModelList.get(position).getMaloai();
                hinhanh = popularModelList.get(position).getHinhanh();


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
                Toast.makeText(context, "Đã thêm vào giở hàng!", Toast.LENGTH_SHORT).show();
                totalPrice = 0;
                totalQuantity = 1;
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return popularModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView popImg;
        TextView name,rating,price;
        Button btn;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            popImg = itemView.findViewById(R.id.pop_img);
            name = itemView.findViewById(R.id.pop_name);
            rating = itemView.findViewById(R.id.pop_rating);
            price = itemView.findViewById(R.id.pop_price);
            btn = itemView.findViewById(R.id.pop_btn_add);
            ratingBar = itemView.findViewById(R.id.pop_ratingbar);
        }
    }
}
