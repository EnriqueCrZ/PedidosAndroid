package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapterPedido extends RecyclerView.Adapter<CustomAdapterPedido.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList ped_id,desc,dir,total;

    CustomAdapterPedido(Activity activity, Context context, ArrayList ped_id, ArrayList desc, ArrayList dir, ArrayList total){
        this.activity = activity;
        this.context = context;
        this.ped_id = ped_id;
        this.desc = desc;
        this.dir = dir;
        this.total = total;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fila_pedido, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterPedido.MyViewHolder holder, final int position) {

        holder.pedido.setText(String.valueOf(desc.get(position)));
        holder.dir.setText(String.valueOf(dir.get(position)));
        holder.total.setText("Total: Q."+String.valueOf(total.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DetalleProducto.class);
                intent.putExtra("id",String.valueOf(ped_id.get(position)));
                intent.putExtra("total",String.valueOf(total.get(position)));
                activity.startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ped_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pedido, dir, total;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pedido = itemView.findViewById(R.id.pedido_rcl);
            dir = itemView.findViewById(R.id.dir_rcl);
            total = itemView.findViewById(R.id.total_rcl);
            mainLayout = itemView.findViewById(R.id.mainLayoutPedido);
        }
    }
}
