package com.example.pm2examen310120;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;



import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;

import configuracion.ConexionSQLite;
import configuracion.Contactos;

public class MainActivity extends AppCompatActivity {

    EditText nombre, nota, telefono;
    Spinner pais;
    ImageView imageView;
    Button usarcamara, salvarContacto, contactoSalvado;

    private static final int peticion_camara = 100;
    private static final int peticion_foto = 101;

    private Bitmap imagenBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        nombre = findViewById(R.id.nombre);
        pais = findViewById(R.id.pais);
        nota = findViewById(R.id.nota);
        telefono = findViewById(R.id.telefono);
        imageView = findViewById(R.id.avatar);
        salvarContacto = findViewById(R.id.salvarContacto);
        contactoSalvado = findViewById(R.id.contactoSalvado);
        usarcamara = findViewById(R.id.usarcamara);


        String[] paises = {"Honduras", "Guatemala", "Nicaragua", "El Salvador"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paises);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pais.setAdapter(adapter);

        contactoSalvado.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });
        salvarContacto.setOnClickListener(view -> agregarContacto());
        usarcamara.setOnClickListener(view -> Permisos());
    }

    private void Permisos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    peticion_camara);
        } else {
            TomarFoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == peticion_camara) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TomarFoto();
            }
        }
    }

    private void TomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, peticion_foto);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == peticion_foto && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            imagenBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imagenBitmap);
        }
    }

    private void agregarContacto() {
        ConexionSQLite conexion = new ConexionSQLite(this, Contactos.NameDB, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();


        Object paisSeleccionado = pais.getSelectedItem();
        if (paisSeleccionado == null) {
            Toast.makeText(this, "Seleccione un país", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nombre.getText().toString().trim().isEmpty() ||
                telefono.getText().toString().trim().isEmpty() ||
                nota.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(Contactos.nombre, nombre.getText().toString());
        values.put(Contactos.nota, nota.getText().toString());
        values.put(Contactos.pais, pais.getSelectedItem().toString());
        values.put(Contactos.telefono, telefono.getText().toString());

        if (imagenBitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagenBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            values.put(Contactos.imagen, byteArray);
        }

        long resultado = db.insert(Contactos.TablaContacto, null, values);

        Toast.makeText(this,
                "Contacto ingresado con éxito. ID: " + resultado,
                Toast.LENGTH_LONG).show();

        nombre.setText("");
        telefono.setText("");
        nota.setText("");
        pais.setSelection(0);
        imageView.setImageResource(0);
        imagenBitmap = null;




        db.close();
    }

}

