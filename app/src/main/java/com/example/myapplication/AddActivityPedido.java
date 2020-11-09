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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AddActivityPedido extends AppCompatActivity {

    EditText desc, dir;
    Button add_button, prods;
    TextView items;
    String[] prodsList;
    ArrayList<String> finalList = new ArrayList<>();
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pedido);

        DatabaseHelper myDB = new DatabaseHelper(AddActivityPedido.this);

        desc = findViewById(R.id.pedido_add);
        dir = findViewById(R.id.dir_add);
        items = findViewById(R.id.prodsSelected);

        add_button = findViewById(R.id.add_buttonPedido);
        prods = findViewById(R.id.btnProductosLista);

        prodsList = myDB.readAllProductoArray();
        checkedItems = new boolean[prodsList.length];

        prods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddActivityPedido.this);
                mBuilder.setTitle("Productos disponibles");
                mBuilder.setMultiChoiceItems(prodsList, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!mUserItems.contains(position)) {
                                mUserItems.add(position);
                            }
                        } else if(mUserItems.contains(position)) {
                            mUserItems.remove(position);
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + prodsList[mUserItems.get(i)];
                            finalList.add(prodsList[mUserItems.get(i)]);
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        items.setText(item);
                    }
                });

                mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Deseleccionar todo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                            items.setText("");
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] productos = new String[finalList.size()];

                long result = myDB.addPedido(desc.getText().toString().trim(), dir.getText().toString().trim(), finalList.toArray(productos));

                if (result == -1)
                    //Toast.makeText(context,"Fallo en agregar registro",Toast.LENGTH_SHORT).show();
                    desc.setText("");
                else {
                    desc.setText("");
                    dir.setText("");
                    items.setText("");
                    //Toast.makeText(context,"Agregado exitosamente",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}