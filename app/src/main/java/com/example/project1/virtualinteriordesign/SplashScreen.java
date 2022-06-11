package com.example.project1.virtualinteriordesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    private TextView description;
    private ImageView logoImage;
    private LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //binding
        logoImage = findViewById(R.id.appNameLogo);
        ll = findViewById(R.id.logoName);
        description = findViewById(R.id.description);

        //animation
        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_to_up);
        ll.startAnimation(slideUp);
        description.startAnimation(slideUp);
        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_to_down);
        logoImage.startAnimation(slideDown);

        //new Activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        },2000);
    }
}