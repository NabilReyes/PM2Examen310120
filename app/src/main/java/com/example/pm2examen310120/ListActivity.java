package com.example.pm2examen310120;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import configuracion.ConexionSQLite;
import configuracion.Contactos;

public class ListActivity extends AppCompatActivity {

    private String contactoSeleccionadoId = null;
    private TextView contactoSeleccionadoView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        LinearLayout contenedor = findViewById(R.id.contenedorContactos);
        Button btnEliminarContactos = findViewById(R.id.btnEliminarContactos);

        ConexionSQLite conexion = new ConexionSQLite(this, Contactos.NameDB, null, 1);
        SQLiteDatabase db = conexion.getReadableDatabase();

        Cursor cursor = db.rawQuery(Contactos.SelectContacto, null);
        int i = 0;

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(Contactos.id));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(Contactos.nombre));
            String telefono = cursor.getString(cursor.getColumnIndexOrThrow(Contactos.telefono));
            String pais = cursor.getString(cursor.getColumnIndexOrThrow(Contactos.pais));
            String nota = cursor.getString(cursor.getColumnIndexOrThrow(Contactos.nota));

            TextView contacto = new TextView(this);
            contacto.setText(nombre + " - " + telefono + "\n" + pais + " - " + nota);
            contacto.setPadding(20, 30, 20, 30);
            contacto.setTextSize(16);
            contacto.setTextColor(Color.BLACK);
            contacto.setBackgroundColor(i % 2 == 0 ? Color.WHITE : Color.parseColor("#E3F2FD"));
            contacto.setTag(id);


            contacto.setOnClickListener(v -> {
                if (contactoSeleccionadoView != null) {

                    contactoSeleccionadoView.setBackgroundColor(Color.WHITE);
                }

                contactoSeleccionadoId = (String) v.getTag();
                contactoSeleccionadoView = (TextView) v;

                contactoSeleccionadoView.setBackgroundColor(Color.parseColor("#ADD8E6"));
            });

            contenedor.addView(contacto);
            i++;
        }

        cursor.close();
        db.close();


        btnEliminarContactos.setOnClickListener(v -> {
            if (contactoSeleccionadoId != null && contactoSeleccionadoView != null) {
                eliminarContacto(contactoSeleccionadoId);
                contenedor.removeView(contactoSeleccionadoView);
                contactoSeleccionadoId = null;
                contactoSeleccionadoView = null;
            } else {
                Toast.makeText(this, "Selecciona un contacto primero", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarContacto(String id) {
        ConexionSQLite conexion = new ConexionSQLite(this, Contactos.NameDB, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        String whereClause = "id=?";
        String[] whereArgs = { id };

        db.delete("contacto", whereClause, whereArgs);
        db.close();
    }


}
