package com.example.project1.virtualinteriordesign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.virtualinteriordesign.adapter.GalleryAdapter;
import com.example.project1.virtualinteriordesign.model.GalleryModel;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    private TextView profile, username, email;
    private ImageView logout, back;
    private RecyclerView recyclerView;
    private ArrayList<GalleryModel> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //binding
        profile = findViewById(R.id.ProfilePic);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        logout = findViewById(R.id.logout);
        back = findViewById(R.id.backProfile);
        recyclerView = findViewById(R.id.galleryList);

        fetchInfo(getIntent());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new GalleryAdapter(this, arrayList));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void fetchInfo(Intent intent) {
        //fetch user name and email
        String name = intent.getStringExtra("userName");
        String authId = intent.getStringExtra("authId");
        String mail = intent.getStringExtra("email");

        username.setText(String.valueOf(name));
        email.setText(String.valueOf(mail));
        //create profile
        profile.setText(String.valueOf(name).substring(0,1).toUpperCase());

        arrayList = new ArrayList<GalleryModel>();
        //fetch list
    }
}