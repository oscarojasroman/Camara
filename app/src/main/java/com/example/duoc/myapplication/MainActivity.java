package com.example.duoc.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import static com.example.duoc.myapplication.ToDoDbHelper.*;

public class MainActivity extends Activity {

    private String[] asd ={TAREA_NOMBRE,TAREA_IMAGEN};

    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tomarFoto(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!= null)
        {
            startActivityForResult(intent, REQUEST_CODE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            ImageView imageView =(ImageView)findViewById(R.id.ImangenView);
            imageView.setImageBitmap(bitmap);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String BitmapToString(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
}
    public void guardarProducto(View view){
        //recuperacion valores de controles
        String Nombre = ((EditText)findViewById(R.id.Nombre)).getText().toString();
        String imgTemp =BitmapToString(bitmap);




        //codigo SQLite
        ToDoDbHelper toDoDbHelper = new ToDoDbHelper(this);
        SQLiteDatabase db = toDoDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TAREA_NOMBRE, Nombre);
        cv.put(TAREA_IMAGEN,imgTemp);


        //nombre de la tabla, nullhack, valores
        db.insert(TAREA_TABLE, null, cv);

        //notifica la creacion con un TOAST
        Toast.makeText(this, "Producto Guardado", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, ListarDatos.class);
        startActivity(intent);
    }
}
