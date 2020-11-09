package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;

public class AddActivityMarca extends AppCompatActivity {

    EditText description;
    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marca);

        description = findViewById(R.id.marca_desc_input);
        /*pages_input = findViewById(R.id.pages_inputs);*/
        add_button = findViewById(R.id.add_buttonMarca);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDB = new DatabaseHelper(AddActivityMarca.this);

                long result = myDB.addMarca(description.getText().toString().trim());

                if(result == -1)
                    //Toast.makeText(context,"Fallo en agregar registro",Toast.LENGTH_SHORT).show();
                    description.setText("");
                else{
                    description.setText("");
                }
            }
        });


    }
}