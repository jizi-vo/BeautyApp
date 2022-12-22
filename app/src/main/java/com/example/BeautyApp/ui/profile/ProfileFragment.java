package com.example.BeautyApp.ui.profile;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.example.BeautyApp.MainActivity;
import com.example.BeautyApp.R;
import com.example.BeautyApp.activities.ChangePassActivity;
import com.example.BeautyApp.activities.UpdateProfileActivity;


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
