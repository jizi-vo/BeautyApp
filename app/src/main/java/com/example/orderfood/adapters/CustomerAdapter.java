package com.example.orderfood.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.MainActivity;
import com.example.orderfood.R;
import com.example.orderfood.admin.ThongTinKhachHangActivity;
import com.example.orderfood.models.UserModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    Context context;
    List<UserModel> userModelList;

    String quyen;

    public CustomerAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(userModelList.get(position).getHinhanh())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(holder.imageView);
        holder.name.setText(userModelList.get(position).getTentk());
        holder.phone.setText(userModelList.get(position).getSdt());
        holder.address.setText(userModelList.get(position).getDiachi());
        if (userModelList.get(position).getQuyen().equals("admin")){
            quyen = "<font color='#FF5353'>Admin</font>";
            holder.position.setText(Html.fromHtml(quyen));
        }
        if (userModelList.get(position).getQuyen().equals("khach hang")){
            holder.position.setText("User");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ThongTinKhachHangActivity.class);
                intent.putExtra("sdt",userModelList.get(position).getSdt());
                intent.putExtra("quyen",userModelList.get(position).getQuyen());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name,phone,address,position;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.admin_kh_img);
            name = itemView.findViewById(R.id.admin_kh_name);
            phone = itemView.findViewById(R.id.admin_kh_phone);
            address = itemView.findViewById(R.id.admin_kh_address);
            position = itemView.findViewById(R.id.admin_kh_quyen);
        }
    }
}
