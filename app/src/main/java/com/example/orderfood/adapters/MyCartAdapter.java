package com.example.orderfood.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.models.MyCartModel;
import com.example.orderfood.ui.my_cart.MyCartFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    Context context;
    List<MyCartModel> myCartModelList;


    public MyCartAdapter(Context context, List<MyCartModel> myCartModelList) {
        this.context = context;
        this.myCartModelList = myCartModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(myCartModelList.get(position).getHinhanh()).into(holder.myCartItem);
        holder.name.setText(myCartModelList.get(position).getTenmon());
        holder.quantity.setText(myCartModelList.get(position).getSoluong()+"");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.price.setText(decimalFormat.format(myCartModelList.get(position).getGia())+"đ");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Thông báo!");
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
                            myCartModelList.remove(myCartModelList.get(position));
                            notifyDataSetChanged();
                            MyCartFragment.totalPrice();
                            MyCartFragment.constraint();
                        }
                    });
                    dialog.show();
                }catch (Exception exception){
                    
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return myCartModelList.size();
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
