package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Pedido extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;

    DatabaseHelper myDB;
    ArrayList<String> pedido_id, pedido_desc,total, dir;
    CustomAdapterPedido customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        recyclerView = findViewById(R.id.recyclerViewPedido);
        add_button = findViewById(R.id.new_pedido);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pedido.this,AddActivityPedido.class);
                startActivityForResult(intent,1);
            }
        });

        myDB = new DatabaseHelper(Pedido.this);
        pedido_id = new ArrayList<>();
        pedido_desc = new ArrayList<>();
        dir = new ArrayList<>();
        total = new ArrayList<>();

        displayDataInArrays();

        customAdapter = new CustomAdapterPedido(Pedido.this,this,pedido_id,pedido_desc,dir,total);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Pedido.this));
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
        Cursor cursor = myDB.readAllPedido();

        if(cursor.getCount() == 0)
            Toast.makeText(this,"No hay datos almacenados.",Toast.LENGTH_SHORT).show();
        else
            while(cursor.moveToNext()){

                pedido_id.add(cursor.getString(0));
                pedido_desc.add(cursor.getString(1));
                dir.add(cursor.getString(2));
                total.add(cursor.getString(3));
            }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar todo?");
        builder.setMessage("Seguro que quieres eliminar el universo?");
        builder.setPositiveButton("Chasquear dedos", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(pedido_id.isEmpty())
                    Toast.makeText(Pedido.this,"No hay datos para eliminar.",Toast.LENGTH_SHORT).show();
                else{
                    DatabaseHelper myDB = new DatabaseHelper(Pedido.this);
                    myDB.deleteAllPedidos();

                    Toast.makeText(Pedido.this,"Eliminados.",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Pedido.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

}
