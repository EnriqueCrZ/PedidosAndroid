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

public class CustomAdapterDetalleProducto extends RecyclerView.Adapter<CustomAdapterDetalleProducto.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList prod_id,nombre,imagen,precio,marca;

    CustomAdapterDetalleProducto(Activity activity, Context context, ArrayList prod_id, ArrayList nombre, ArrayList imagen, ArrayList precio, ArrayList marca){
        this.activity = activity;
        this.context = context;
        this.prod_id = prod_id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.precio = precio;
        this.marca = marca;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fila_producto, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterDetalleProducto.MyViewHolder holder, final int position) {
        byte[] image = Base64.decode((String) imagen.get(position),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);

        DatabaseHelper myDB = new DatabaseHelper(context);


        holder.nombre.setText(String.valueOf(nombre.get(position)));
        holder.precio.setText("Q. "+ String.valueOf(precio.get(position)));
        holder.marca.setText(myDB.nombreMarca((String) marca.get(position)));
        holder.imagen.setImageBitmap(Bitmap.createScaledBitmap(bitmap,200,200,false));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateActivityProducto.class);
                intent.putExtra("id",String.valueOf(prod_id.get(position)));
                intent.putExtra("nombre",String.valueOf(nombre.get(position)));
                intent.putExtra("precio",String.valueOf(precio.get(position)));
                activity.startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return prod_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, precio, marca;
        ImageView imagen;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.producto_rcl);
            precio = itemView.findViewById(R.id.precio_rcl);
            marca = itemView.findViewById(R.id.marca_rcl);
            imagen = itemView.findViewById(R.id.imagen_prod_rcl);
            mainLayout = itemView.findViewById(R.id.mainLayoutProducto);
        }
    }
}
