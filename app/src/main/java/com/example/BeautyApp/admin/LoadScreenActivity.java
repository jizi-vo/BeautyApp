package com.example.BeautyApp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.BeautyApp.R;

import java.util.Timer;
import java.util.TimerTask;

public class LoadScreenActivity extends AppCompatActivity {

    Animation animationTop,animationBottom;

    ProgressBar progressBar;
    TextView textView,txtTop,txtBottom;
    Timer timer;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_load_screen);

        progressBar = findViewById(R.id.progressBar_loadScreen);
        textView = findViewById(R.id.loadScreen);
        animationTop = AnimationUtils.loadAnimation(this,R.anim.animation_top);
        animationBottom = AnimationUtils.loadAnimation(this,R.anim.animation_bottom);
        txtTop = findViewById(R.id.animation_top);
        txtBottom = findViewById(R.id.animation_bottom);

        txtTop.setAnimation(animationTop);
        txtBottom.setAnimation(animationBottom);

        timer = new Timer();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (i<=100){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                            progressBar.setProgress(i);
                            textView.setText(i+"%");
                            i++;
                        }else{
                            timer.cancel();
                            Intent intent = new Intent(LoadScreenActivity.this,LoadAppActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },0,10);
            }
        },1000);

    }

}