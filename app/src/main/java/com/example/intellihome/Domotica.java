package com.example.intellihome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Domotica extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domotica);

        // Encuentra el botón de regreso en el layout
        ImageView btnBack = findViewById(R.id.botonRegresar);

        // Asigna el evento onClick al botón de regreso
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a HomePage
                Intent intent = new Intent(Domotica.this, HomePage.class);
                startActivity(intent);
                finish(); // Finaliza la actividad actual
            }
        });
        try {
            // Encuentra el ImageView de perfil
            ImageView perfilMenu = findViewById(R.id.perfilmenu);
            ImageView domoticaMenu = findViewById(R.id.domotica);
            ImageView historialMenu = findViewById(R.id.historial);
            ImageView busquedaMenu = findViewById(R.id.busquedamenu);

            perfilMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Domotica.this, Perfil.class));
                    finish();
                }
            });


            historialMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Domotica.this, RegistroPropiedad.class));
                    finish();
                }
            });

            busquedaMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Domotica.this, Historial.class));
                    finish();
                }
            });

        } catch (Exception e) {
            // Muestra un mensaje en caso de error
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace(); // Imprime la traza de la excepción en Logcat
        }

    }
}