package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DetalleProducto extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;

    DatabaseHelper myDB;
    ArrayList<String> prod_id, nombre, precio, imagen, marca;
    String id, total;
    CustomAdapterProducto customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if(ab != null)
            ab.setTitle("Total del pedido: Q. "+total);

        recyclerView = findViewById(R.id.recyclerViewProducto);
        add_button = findViewById(R.id.new_producto);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalleProducto.this,AddActivityProducto.class);
                startActivityForResult(intent,1);
            }
        });

        myDB = new DatabaseHelper(DetalleProducto.this);
        prod_id = new ArrayList<>();
        nombre = new ArrayList<>();
        marca = new ArrayList<>();
        precio = new ArrayList<>();
        imagen = new ArrayList<>();

        displayDataInArrays();

        customAdapter = new CustomAdapterProducto(DetalleProducto.this,this,prod_id,nombre,imagen,precio,marca);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetalleProducto.this));
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("total") ){
            DatabaseHelper myDB = new DatabaseHelper(DetalleProducto.this);
            id = getIntent().getStringExtra("id");
            total = getIntent().getStringExtra("total");
            myDB.close();
        }else{
            Toast.makeText(this,"Registro inexistente",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflates = getMenuInflater();
        inflates.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
            recreate();
    }

    void displayDataInArrays(){
        Cursor cursor = myDB.readAllDataProductoDetalles(id);

        if(cursor.getCount() == 0)
            Toast.makeText(this,"No hay datos almacenados.",Toast.LENGTH_SHORT).show();
        else
            while(cursor.moveToNext()){
                byte[] image = cursor.getBlob(2);
                Bitmap bitmap = getbitmap(image);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                String base64string = Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);

                prod_id.add(cursor.getString(0));
                nombre.add(cursor.getString(1));
                imagen.add(base64string);
                marca.add(cursor.getString(3));
                precio.add(cursor.getString(4));
            }
    }


    public static Bitmap getbitmap(byte[] image){
        return BitmapFactory.decodeByteArray(image,0,image.length);
    }
}
