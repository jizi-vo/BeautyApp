package com.example.orderfood.ui.profile;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.orderfood.MainActivity;
import com.example.orderfood.R;
import com.example.orderfood.activities.ChangePassActivity;
import com.example.orderfood.activities.UpdateProfileActivity;
import com.example.orderfood.models.Server;
import com.example.orderfood.models.UserModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    CircleImageView imageView;
    TextView userName,name,phone,address,update,changePass;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = root.findViewById(R.id.profileImg);
        userName = root.findViewById(R.id.profile_name_user);
        name = root.findViewById(R.id.profile_name);
        phone = root.findViewById(R.id.profile_phone);
        address = root.findViewById(R.id.profile_address);
        update = root.findViewById(R.id.profile_update);
        changePass = root.findViewById(R.id.profile_change_pass);



        Glide.with(getContext())
                .load(MainActivity.userModelList.get(0).getHinhanh())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(imageView);
        userName.setText(MainActivity.userModelList.get(0).getTentk());
        name.setText(MainActivity.userModelList.get(0).getTentk());
        phone.setText(MainActivity.userModelList.get(0).getSdt());
        address.setText(MainActivity.userModelList.get(0).getDiachi());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChangePassActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }


}
