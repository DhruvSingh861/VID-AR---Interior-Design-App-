package com.example.project1.virtualinteriordesign.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.virtualinteriordesign.R;
import com.example.project1.virtualinteriordesign.ViewImage;
import com.example.project1.virtualinteriordesign.model.GalleryModel;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<com.example.project1.virtualinteriordesign.adapter.GalleryAdapter.MyViewHolder> {

    Context context;
    ArrayList<GalleryModel> list;

    public GalleryAdapter(Context context, ArrayList<GalleryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GalleryModel model = list.get(position);
        //set image
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewImage.class);
                intent.putExtra("Link", model.getPicture());
                intent.putExtra("Info", model.getTime());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.GalleryImg);
        }
    }
}
