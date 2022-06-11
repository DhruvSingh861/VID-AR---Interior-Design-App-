package com.example.project1.virtualinteriordesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.virtualinteriordesign.MainActivity;
import com.example.project1.virtualinteriordesign.R;
import com.example.project1.virtualinteriordesign.model.ModelModel;

import java.util.ArrayList;

public class ModelAdapter extends RecyclerView.Adapter<com.example.project1.virtualinteriordesign.adapter.ModelAdapter.MyViewHolder> {
    Context context;
    ArrayList<ModelModel> model;

    public ModelAdapter(Context context, ArrayList<ModelModel> model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_lay, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String Name = model.get(position).getName();
        String link = model.get(position).getLink();
        holder.modelName.setText(Name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.SELECTED_MODEL_LINK = link;
                Toast.makeText(context, Name +" is selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView modelName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            modelName = itemView.findViewById(R.id.modelName);
        }
    }
}
