package com.example.pm2examen310120;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LlamarActivity extends AppCompatActivity {

    EditText txtTelefono;
    Button btnLlamar;
    Button btnAtras;

    private static final int REQUEST_CALL_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamar);

        // Referencias de los elementos
        btnAtras = findViewById(R.id.btnAtras);
        txtTelefono = findViewById(R.id.txtTelefono);
        btnLlamar = findViewById(R.id.btnLlamar);

        // Acción del botón Atrás
        btnAtras.setOnClickListener(v -> {
            Toast.makeText(this, "Regresando...", Toast.LENGTH_SHORT).show();
            finish(); // Cierra esta Activity
        });

        // Obtener número recibido desde otra actividad (si existe)
        String numeroRecibido = getIntent().getStringExtra("telefono");
        if (numeroRecibido != null) {
            txtTelefono.setText(numeroRecibido);
        }

        // Acción del botón Llamar
        btnLlamar.setOnClickListener(view -> {
            String numero = txtTelefono.getText().toString().trim();
            if (!numero.isEmpty()) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CALL_PERMISSION);
                } else {
                    realizarLlamada(numero);
                }
            } else {
                Toast.makeText(this, "Por favor ingrese un número", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Manejo de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String numero = txtTelefono.getText().toString().trim();
                realizarLlamada(numero);
            } else {
                Toast.makeText(this, "Permiso para llamadas denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Función para realizar la llamada
    private void realizarLlamada(String numero) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + numero));
        startActivity(intent);
    }
}