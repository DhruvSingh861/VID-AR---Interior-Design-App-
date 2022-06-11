package com.example.project1.virtualinteriordesign.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.virtualinteriordesign.R;

import com.example.project1.virtualinteriordesign.model.CategoryModel;
import com.example.project1.virtualinteriordesign.model.ModelModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> list;
    ArrayList<ModelModel> model;
    ModelAdapter adapter;

    public CategoryAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cat_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        model = new ArrayList<ModelModel>();
        String currCat = list.get(position).toString();
        holder.CatName.setText(list.get(position).toString());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.catLL.getVisibility() == View.GONE){
                    holder.catLL.setVisibility(View.VISIBLE);
                    holder.imageView.setRotation(180f);

                    fetchModel(currCat);
                    adapter = new ModelAdapter(context, model);
                    holder.recyclerView.setAdapter(adapter);
                }
                else{
                    holder.recyclerView.setAdapter(null);
                    holder.catLL.setVisibility(View.GONE);
                    holder.imageView.setRotation(0f);
                    model.clear();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void fetchModel(String category){

        /*model.add("Model 1");
        model.add("Model 2");
        model.add("Model 3");*/
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Objects")
                .child("Category").child(category).child("Models");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    model.add(new ModelModel(dataSnapshot.child("Name").getValue().toString(), dataSnapshot.child("link").getValue().toString()));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView CatName;
        LinearLayout catLL;
        RecyclerView recyclerView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.CatImage);
            CatName = itemView.findViewById(R.id.CatName);
            catLL = itemView.findViewById(R.id.CatLL);
            recyclerView = itemView.findViewById(R.id.CatSubList);
        }
    }
}
