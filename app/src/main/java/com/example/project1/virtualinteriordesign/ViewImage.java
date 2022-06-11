package com.example.project1.virtualinteriordesign;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ViewImage extends AppCompatActivity {

    ImageView image, save, share, back;
    LinearLayout titleBar;
    private boolean visibility = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        image = findViewById(R.id.viewImage);
        save = findViewById(R.id.downloadImage);
        share = findViewById(R.id.ShareImage);
        back = findViewById(R.id.backBtn);
        titleBar = findViewById(R.id.infoTB);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visibility){
                    titleBar.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                    share.setVisibility(View.GONE);
                }
                else{
                    titleBar.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);
                    share.setVisibility(View.VISIBLE);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}