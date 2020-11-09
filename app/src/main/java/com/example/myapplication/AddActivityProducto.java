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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;

public class AddActivityProducto extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1;
    EditText prod_name, precio;
    Button add_button, img_btn;
    ImageView imageView;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prod);

        DatabaseHelper myDB = new DatabaseHelper(AddActivityProducto.this);

        prod_name = findViewById(R.id.producto_add);
        precio = findViewById(R.id.precio_add);

        imageView = findViewById(R.id.imagen_prod_add);
        add_button = findViewById(R.id.add_buttonProducto);
        img_btn = findViewById(R.id.btn_img);
        spinner = findViewById(R.id.spinnerProducto);

        String[] spinnerLists = myDB.readAllMarcaArray();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AddActivityProducto.this,android.R.layout.simple_spinner_item, spinnerLists);
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

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDB = new DatabaseHelper(AddActivityProducto.this);
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                //data of spinner
                String item_selected = spinner.getSelectedItem().toString();
                String[] marca_id = item_selected.split("-");

                myDB.close();

                long result = myDB.addProducto(prod_name.getText().toString().trim(),precio.getText().toString().trim(),marca_id[0],data);

                if(result == -1)
                    //Toast.makeText(context,"Fallo en agregar registro",Toast.LENGTH_SHORT).show();
                    prod_name.setText("");
                else{
                    prod_name.setText("");
                    precio.setText("");
                    imageView.setImageResource(0);
                    //Toast.makeText(context,"Agregado exitosamente",Toast.LENGTH_SHORT).show();
                }
            }
        });

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(AddActivityProducto.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddActivityProducto.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        selectImage(AddActivityProducto.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void selectImage(Context context) {
        final CharSequence[] options = { "Tomar Foto", "Escoger desde Galeria","Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                        imageView.setImageBitmap(selectedImage);
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
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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