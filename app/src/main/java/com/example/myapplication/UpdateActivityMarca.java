package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;

public class UpdateActivityMarca extends AppCompatActivity {
    EditText desc_edit;
    Button update_button, delete_button;

    String id, desc;
    byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_marca);

        desc_edit = findViewById(R.id.marca_desc_edit);
        update_button = findViewById(R.id.update_buttonMarca);
        delete_button = findViewById(R.id.delete_buttonMarca);

        //data
        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if(ab != null)
            ab.setTitle("Actualizar "+desc);

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDB = new DatabaseHelper(UpdateActivityMarca.this);

                desc = desc_edit.getText().toString().trim();

                long result = myDB.updateMarca(id,desc);

                if (result == -1){
                    //Toast.makeText(UpdateActivity.this,"Fallo en actualizar registro",Toast.LENGTH_SHORT).show();
                    Log.d("Error","Fallo en actualizar");
                }
                else{
                    UpdateActivityMarca.super.finish();
                    //Toast.makeText(UpdateActivity.this,title+" actualizado.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("marca") ){
            DatabaseHelper myDB = new DatabaseHelper(UpdateActivityMarca.this);
            id = getIntent().getStringExtra("id");
            desc = getIntent().getStringExtra("marca");

            myDB.close();
            desc_edit.setText(desc);
        }else{
            Toast.makeText(this,"Registro inexistente",Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar "+desc+"?");
        builder.setMessage("Seguro que quieres eliminar "+desc+"?");
        builder.setPositiveButton("Simon", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper myDB = new DatabaseHelper(UpdateActivityMarca.this);
                desc = desc_edit.getText().toString().trim();

                long result = myDB.deleteMarca(id,desc);

                if(result != -1)
                    UpdateActivityMarca.super.finish();
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