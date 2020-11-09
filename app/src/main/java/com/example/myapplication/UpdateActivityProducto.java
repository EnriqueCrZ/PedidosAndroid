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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;

public class UpdateActivityProducto extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 1;
    EditText prod, precio;
    ImageView imagen;
    Spinner spinner;
    Button update_button, delete_button, image_btn;

    String id, prod_name, precio_str,image_uri;
    byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_producto);
        DatabaseHelper myDB = new DatabaseHelper(UpdateActivityProducto.this);

        prod = findViewById(R.id.producto_updt);
        precio = findViewById(R.id.precio_updt);
        imagen = findViewById(R.id.imagen_prod_updt);
        update_button = findViewById(R.id.update_button_prod);
        spinner = findViewById(R.id.spinnerProductoUpdate);
        delete_button = findViewById(R.id.delete_button_prod);
        image_btn = findViewById(R.id.btn_img2_updt);

        //data
        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if(ab != null)
            ab.setTitle("Actualizar "+prod);

        String[] spinnerLists = myDB.readAllMarcaArray();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(UpdateActivityProducto.this,android.R.layout.simple_spinner_item, spinnerLists);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                imagen.setDrawingCacheEnabled(true);
                imagen.buildDrawingCache();
                Bitmap bitmap = imagen.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                image = baos.toByteArray();

                //data of spinner
                String item_selected = spinner.getSelectedItem().toString();
                String[] marca_id = item_selected.split("-");

                myDB.close();

                prod_name = prod.getText().toString().trim();
                precio_str = precio.getText().toString().trim();

                long result = myDB.updateProducto(id,prod_name,precio_str,marca_id[0],image);

                if (result == -1){
                    //Toast.makeText(UpdateActivity.this,"Fallo en actualizar registro",Toast.LENGTH_SHORT).show();
                    Log.d("Error","Fallo en actualizar");
                }
                else{
                    UpdateActivityProducto.super.finish();
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

        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(UpdateActivityProducto.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UpdateActivityProducto.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        selectImage(UpdateActivityProducto.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("nombre") && getIntent().hasExtra("precio")){
            DatabaseHelper myDB = new DatabaseHelper(UpdateActivityProducto.this);
            id = getIntent().getStringExtra("id");
            prod_name = getIntent().getStringExtra("nombre");
            precio_str = getIntent().getStringExtra("precio");

            byte[] image = myDB.productPicture(id);
            myDB.close();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);

            prod.setText(prod_name);
            precio.setText(precio_str);
            imagen.setImageBitmap(Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),false));

        }else{
            Toast.makeText(this,"Registro inexistente",Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar "+prod_name+"?");
        builder.setMessage("Seguro que quieres eliminar "+prod_name+"?");
        builder.setPositiveButton("Simon", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper myDB = new DatabaseHelper(UpdateActivityProducto.this);
                prod_name = prod.getText().toString().trim();

                long result = myDB.deleteProducto(id,prod_name);

                if(result != -1)
                    UpdateActivityProducto.super.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Tomar Foto", "Escoger desde Galeria","Cancelar" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Escoger foto de producto");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Tomar Foto")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Escoger desde Galeria")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imagen.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imagen.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PICK_FROM_GALLERY) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
            }  //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery

        }
    }
}