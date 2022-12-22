package com.example.BeautyApp.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.BeautyApp.R;
import com.example.BeautyApp.Retrofit2.APIUtils;
import com.example.BeautyApp.Retrofit2.DataClient;
import com.example.BeautyApp.activities.UpdateCategoryActivity;
import com.example.BeautyApp.admin.LoaiMonActivity;
import com.example.BeautyApp.models.HomeHorModel;
import com.example.BeautyApp.models.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class AdminLoaiMyPhamAdapter extends RecyclerView.Adapter<AdminLoaiMyPhamAdapter.ViewHolder> {
    Context context;
    List<HomeHorModel> homeHorModelList;

    ProgressDialog progressDialog;
    String nameCategory;
    public AdminLoaiMyPhamAdapter(Context context, List<HomeHorModel> homeHorModelList) {
        this.context = context;
        this.homeHorModelList = homeHorModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_loaimon_view_all_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(homeHorModelList.get(position).getHinhanh()).into(holder.imageView);
        holder.name.setText(homeHorModelList.get(position).getTenloai());

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Wait...");
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCategory(position);
            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateCategoryActivity.class);
                intent.putExtra("maloai",homeHorModelList.get(position).getMaloai()+"");
                intent.putExtra("tenloai",homeHorModelList.get(position).getTenloai());
                intent.putExtra("hinhanh",homeHorModelList.get(position).getHinhanh());
                context.startActivity(intent);
            }
        });
    }

    private void deleteCategory(int position) {
        try{
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Cảnh báo!");
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
                progressDialog.show();
                DataClient dataClient = APIUtils.getData();
                retrofit2.Call<String> callBack = dataClient.deleteCategory(homeHorModelList.get(position).getMaloai()+"");
                callBack.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        String message = response.body();
                        if (message.equals("Success")){
                            progressDialog.dismiss();
                            Toast.makeText(context, "Xóa món thành công!", Toast.LENGTH_SHORT).show();
                            reloadCategory();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(context, "Không thể xóa!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
        dialog.show();
    }catch (Exception exception){

        }
    }

    private void reloadCategory() {
        LoaiMonActivity.homeHorModelList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.linkloaimon, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null){
                    int maloai;
                    String tenloai;
                    String hinhanh;
                    for (int i = 0;i <response.length();i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            maloai = jsonObject.getInt("maloai");
                            tenloai = jsonObject.getString("tenloai");
                            hinhanh = jsonObject.getString("hinhanh");
                            LoaiMonActivity.homeHorModelList.add(new HomeHorModel(maloai,tenloai,hinhanh));
                            LoaiMonActivity.adminLoaiMonAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public int getItemCount() {
        return homeHorModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,update,delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.admin_loaimon_img);
            name = itemView.findViewById(R.id.admin_loaimon_name);
            update = itemView.findViewById(R.id.admin_loaimon_update);
            delete = itemView.findViewById(R.id.admin_loaimon_delete);
        }
    }
}
