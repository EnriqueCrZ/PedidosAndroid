package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "Compras.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTO = "producto";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_IMAGEN = "imagen";
    public static final String COLUMN_PRECIO = "precio";
    public static final String COLUMN_MARCA_ID = "marca_id";

    public static final String TABLE_DETALLE_PEDIDO = "detalle_pedido";
    public static final String COLUMN_ID2 = "_id";
    public static final String COLUMN_PRODUCTO_ID = "pdoructo_id";
    public static final String COLUMN_PEDIDO_ID = "pedido_id";

    public static final String TABLE_PEDIDO = "pedido";
    public static final String COLUMN_ID3 = "_id";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_DIRECCION = "direccion";
    public static final String COLUMN_TOTAL = "total";

    public static final String TABLE_MARCA = "marca";
    public static final String COLUMN_ID4 = "_id";
    public static final String COLUMN_DESCRIPCION_MARCA = "descripcion";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTO
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOMBRE + " TEXT, "
                + COLUMN_IMAGEN + " BLOB, "
                + COLUMN_MARCA_ID + " TEXT, "
                + COLUMN_PRECIO + " TEST)");

        db.execSQL("CREATE TABLE " + TABLE_DETALLE_PEDIDO + " (" + COLUMN_ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_PRODUCTO_ID + " INTEGER, "
        + COLUMN_PEDIDO_ID + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_PEDIDO
                + " (" + COLUMN_ID3 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DESCRIPCION + " TEXT, "
                + COLUMN_DIRECCION + " TEXT, "
                + COLUMN_IMAGEN + " BLOB, "
                + COLUMN_MARCA_ID + " TEXT, "
                + COLUMN_TOTAL + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_MARCA
                + " (" + COLUMN_ID4 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DESCRIPCION_MARCA + " TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTO);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETALLE_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARCA);
        onCreate(db);
    }

//inicia marca
    long addMarca(String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DESCRIPCION_MARCA, desc);

        long result = db.insert(TABLE_MARCA, null, cv);

        if (result == -1)
            Toast.makeText(context, "Fallo en agregar registro", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, desc + " agregado exitosamente", Toast.LENGTH_SHORT).show();

        return result;
    }

    long updateMarca(String row_id, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DESCRIPCION_MARCA, desc);

        long result = db.update(TABLE_MARCA, cv, " _id=?", new String[]{row_id});

        if (result == -1)
            Toast.makeText(context, "Fallo en actualizar registro", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, desc + " actualizado.", Toast.LENGTH_SHORT).show();

        return result;
    }

    long deleteMarca(String row_id, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_MARCA, " _id=?", new String[]{row_id});

        if (result == -1)
            Toast.makeText(context, "Fallo en eliminar registro", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, desc + " eliminado.", Toast.LENGTH_SHORT).show();

        return result;
    }

    Cursor readAllDataMarca() {
        String query = "SELECT * FROM " + TABLE_MARCA;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery(query, null);

        return cursor;
    }

    public String[] readAllMarcaArray(){

        String selectQuery = "select * from " + TABLE_MARCA;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        ArrayList<String> spinnerContent = new ArrayList<String>();

        if(cursor.moveToFirst()){
            do{
                spinnerContent.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)) + " - " + cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION_MARCA)));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        String[] allSpinner = new String[spinnerContent.size()];

        return spinnerContent.toArray(allSpinner);
    }

    public String nombreMarca(String id){
        String query = "SELECT " + COLUMN_DESCRIPCION_MARCA + " FROM " + TABLE_MARCA + " WHERE " + COLUMN_ID + " = " + id;
        String marca = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query,null);

        if(c.getCount() == 1){
            c.moveToFirst();
            marca = c.getString(0);
        }
        c.close();
        return marca;
    }

    void deleteAllMarca() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MARCA);
    }
//termina marca

    //inicia producto
    long addProducto(String nombre, String precio, String marca, byte[] imagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOMBRE, nombre);
        cv.put(COLUMN_IMAGEN, imagen);
        cv.put(COLUMN_PRECIO, precio);
        cv.put(COLUMN_MARCA_ID, marca);

        long result = db.insert(TABLE_PRODUCTO, null, cv);

        if (result == -1)
            Toast.makeText(context, "Fallo en agregar registro", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, nombre + " agregado exitosamente", Toast.LENGTH_SHORT).show();

        return result;
    }

    Cursor readAllDataProducto() {
        String query = "SELECT * FROM " + TABLE_PRODUCTO;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery(query, null);

        return cursor;
    }

    long updateProducto(String row_id, String nombre, String precio, String marca, byte[] imagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOMBRE, nombre);
        cv.put(COLUMN_PRECIO, precio);
        cv.put(COLUMN_MARCA_ID, marca);
        cv.put(COLUMN_IMAGEN, imagen);

        long result = db.update(TABLE_PRODUCTO, cv, " _id=?", new String[]{row_id});

        if (result == -1)
            Toast.makeText(context, "Fallo en actualizar registro", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, nombre + " actualizado.", Toast.LENGTH_SHORT).show();

        return result;
    }

    long deleteProducto(String row_id, String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_PRODUCTO, " _id=?", new String[]{row_id});

        if (result == -1)
            Toast.makeText(context, "Fallo en eliminar registro", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, nombre + " eliminado.", Toast.LENGTH_SHORT).show();

        return result;
    }

    void deleteAllProducto() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PRODUCTO);
    }

    byte[] productPicture(String id){
        String query = "SELECT " + COLUMN_IMAGEN + " FROM " + TABLE_PRODUCTO + " WHERE " + COLUMN_ID + " = " + id;
        byte[] prod_pic = new byte[0];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query,null);


        if(c.getCount() == 1){
            c.moveToFirst();
            prod_pic = c.getBlob(0);
        }
        c.close();
        return prod_pic;
    }

    //termina producto

    /*long addBook(String title, String author, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_STUDENT_NAME, title);
        cv.put(COLUMN_STUDENT_LAST, author);
        cv.put(COLUMN_STUDENT_PICTURE, image);

        long result = db.insert(TABLE_STUDENT, null, cv);

        if (result == -1)
            Toast.makeText(context, "Fallo en agregar registro", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, title + " agregado exitosamente", Toast.LENGTH_SHORT).show();

        return result;
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_STUDENT;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery(query, null);

        return cursor;
    }

    public String[] readAllDataArray(){

        String selectQuery = "select * from " + TABLE_STUDENT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        ArrayList<String> spinnerContent = new ArrayList<String>();

        if(cursor.moveToFirst()){
            do{
                spinnerContent.add(cursor.getString(cursor.getColumnIndexOrThrow(com.example.sqlite2.DatabaseHelper.COLUMN_ID)) + " - " + cursor.getString(cursor.getColumnIndexOrThrow(com.example.sqlite2.DatabaseHelper.COLUMN_STUDENT_NAME)) + " " + cursor.getString(cursor.getColumnIndexOrThrow(com.example.sqlite2.DatabaseHelper.COLUMN_STUDENT_LAST)));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        String[] allSpinner = new String[spinnerContent.size()];

        return spinnerContent.toArray(allSpinner);
    }

    long updateData(String row_id, String title, String author, byte[] pages) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STUDENT_NAME, title);
        cv.put(COLUMN_STUDENT_LAST, author);
        cv.put(COLUMN_STUDENT_PICTURE, pages);

        long result = db.update(TABLE_STUDENT, cv, " _id=?", new String[]{row_id});

        if (result == -1)
            Toast.makeText(context, "Fallo en actualizar registro", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, String.valueOf(title) + " actualizado.", Toast.LENGTH_SHORT).show();

        return result;
    }

    long deleteData(String row_id, String title) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_STUDENT, " _id=?", new String[]{row_id});

        if (result == -1)
            Toast.makeText(context, "Fallo en eliminar registro", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, String.valueOf(title) + " eliminado.", Toast.LENGTH_SHORT).show();

        return result;
    }

    void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STUDENT);
    }

    //Inscripcion
    public long addIns(String carne, String date, String id_alumno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CARNE, carne);
        cv.put(COLUMN_DATE, String.valueOf(date));
        cv.put(COLUMN_STUDENT, id_alumno);

        long result = db.insert(TABLE_INSCRIPCION, null, cv);

        if (result == -1)
            Toast.makeText(context, "Fallo en agregar registro", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Inscrito exitosamente", Toast.LENGTH_SHORT).show();

        return result;
    }

    public Cursor readAllDataIns() {
        String query = "SELECT * FROM " + TABLE_INSCRIPCION;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery(query, null);

        return cursor;
    }

    public String studentName(String id){
        String query = "SELECT " + COLUMN_STUDENT_NAME + " FROM " + TABLE_STUDENT + " WHERE " + COLUMN_ID + " = " + id;
        String student_name = "";
        Log.d(student_name,"false");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query,null);

        if(c.getCount() == 1){
            c.moveToFirst();
            student_name = c.getString(0);
        }
        c.close();
        return student_name;
    }

    byte[] studentPicture(String id){
        String query = "SELECT " + com.example.sqlite2.DatabaseHelper.COLUMN_STUDENT_PICTURE + " FROM " + com.example.sqlite2.DatabaseHelper.TABLE_STUDENT + " WHERE " + com.example.sqlite2.DatabaseHelper.COLUMN_ID + " = " + id;
        byte[] student_pic = new byte[0];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query,null);


        if(c.getCount() == 1){
            c.moveToFirst();
            student_pic = c.getBlob(0);
        }
        c.close();
        return student_pic;
    }*/

}
