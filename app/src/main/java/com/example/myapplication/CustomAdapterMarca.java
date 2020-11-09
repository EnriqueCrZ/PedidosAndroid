package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterMarca extends RecyclerView.Adapter<CustomAdapterMarca.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList marca_id,marca_desc;

    CustomAdapterMarca(Activity activity, Context context, ArrayList marca_id, ArrayList marca_desc){
        this.activity = activity;
        this.context = context;
        this.marca_id = marca_id;
        this.marca_desc = marca_desc;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fila_marca, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterMarca.MyViewHolder holder, final int position) {

        holder.marca_id.setText(String.valueOf(marca_id.get(position)));
        holder.marca_desc.setText(String.valueOf(marca_desc.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateActivityMarca.class);
                intent.putExtra("id",String.valueOf(marca_id.get(position)));
                intent.putExtra("marca",String.valueOf(marca_desc.get(position)));
                activity.startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return marca_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView marca_id, marca_desc;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            marca_id = itemView.findViewById(R.id.marca_id);
            marca_desc = itemView.findViewById(R.id.marca_descripcion);
            mainLayout = itemView.findViewById(R.id.mainLayoutMarca);
        }
    }
}
