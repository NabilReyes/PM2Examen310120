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


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import configuracion.ConexionSQLite;
import configuracion.Contactos;

public class MainActivity extends AppCompatActivity {

    EditText nombre, nota, telefono;
    Spinner pais;
    ImageView imageView;
    Button usarcamara, salvarContacto, contactoSalvado;

    private static final int peticion_camara = 100;
    private static final int peticion_foto = 101;

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
            Bitmap image = (Bitmap) extras.get("data");
            imageView.setImageBitmap(image);
        }
    }

    private void agregarContacto() {
        ConexionSQLite conexion = new ConexionSQLite(this, Contactos.NameDB, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contactos.nombre, nombre.getText().toString());
        values.put(Contactos.nota, nota.getText().toString());
        values.put(Contactos.pais, pais.getSelectedItem().toString());
        values.put(Contactos.telefono, telefono.getText().toString());

        long resultado = db.insert(Contactos.TablaContacto, null, values);

        Toast.makeText(this,
                "Contacto ingresado con éxito. ID: " + resultado,
                Toast.LENGTH_LONG).show();

        db.close();
    }
}

