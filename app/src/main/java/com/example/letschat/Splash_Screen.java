package com.example.letschat;

import static com.example.letschat.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ImageView picture;
        TextView tv;
        Animation animation,tvanimation;
        picture=findViewById(R.id.imagesplash);
        tv=findViewById(R.id.tvsplash);
        animation= AnimationUtils.loadAnimation(this,R.anim.splashanimation);
        tvanimation=AnimationUtils.loadAnimation(this,R.anim.textsplashanimation);
        picture.startAnimation(animation);
        tv.startAnimation(tvanimation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_Screen.this, Login_Screen.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }

}