package com.example.pm2examen310120;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import configuracion.ConexionSQLite;
import configuracion.Contactos;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);  // Usa el layout adecuado

        LinearLayout contenedor = findViewById(R.id.contenedorContactos);

        ConexionSQLite conexion = new ConexionSQLite(this, Contactos.NameDB, null, 1);
        SQLiteDatabase db = conexion.getReadableDatabase();

        Cursor cursor = db.rawQuery(Contactos.SelectContacto, null);
        int i = 0;

        while (cursor.moveToNext()) {
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

            contenedor.addView(contacto);
            i++;
        }

        cursor.close();
        db.close();
    }
}
