package com.example.intellihome;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.util.ArrayList;


public class RegistroPropiedad extends AppCompatActivity {

    private TextInputEditText nombrePropiedadInput, cantidadPersonasInput, cantidadHabitacionesInput, precioInput, ubicacionInput, amenidadesCasaInput;
    private Button registrarCasaBtn;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_propiedad);

        // Inicializa los componentes
        nombrePropiedadInput = findViewById(R.id.nombrePropiedadInput);
        cantidadPersonasInput = findViewById(R.id.cantidadPersonasInput);
        cantidadHabitacionesInput = findViewById(R.id.cantidadHabitacionesInput);
        precioInput = findViewById(R.id.precioInput);
        ubicacionInput = findViewById(R.id.ubicacionInput);
        amenidadesCasaInput = findViewById(R.id.amenidadesCasaInput);
        registrarCasaBtn = findViewById(R.id.registrarCasaBtn);

        // Inicializa la referencia de la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Propiedades");

        // Encuentra el botón de regreso en el layout
        ImageView btnBack = findViewById(R.id.botonRegresar);

        // Asigna el evento onClick al botón de regreso
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a HomePage
                startActivity(new Intent(RegistroPropiedad.this, Historial.class));
                finish(); // Finaliza la actividad actual
            }
        });


        // Configura la acción del botón Registrar
        registrarCasaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarPropiedad();
                //sendMessage();
            }
        });

        // Configuración del campo de amenidades con selección múltiple
        amenidadesCasaInput.setOnClickListener(v -> showAmenitiesDialog(amenidadesCasaInput));
    }

    private void registrarPropiedad() {
        String nombrePropiedad = nombrePropiedadInput.getText().toString().trim();
        String cantidadPersonasStr = cantidadPersonasInput.getText().toString().trim();
        String cantidadHabitacionesStr = cantidadHabitacionesInput.getText().toString().trim();
        String precio = precioInput.getText().toString().trim();
        String ubicacion = ubicacionInput.getText().toString().trim();
        String amenidadesCasa = amenidadesCasaInput.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (TextUtils.isEmpty(nombrePropiedad) || TextUtils.isEmpty(cantidadPersonasStr) ||
                TextUtils.isEmpty(cantidadHabitacionesStr) || TextUtils.isEmpty(precio) ||
                TextUtils.isEmpty(ubicacion) || TextUtils.isEmpty(amenidadesCasa)) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que las cantidades sean números enteros
        int cantidadPersonas, cantidadHabitaciones;
        try {
            cantidadPersonas = Integer.parseInt(cantidadPersonasStr);
            cantidadHabitaciones = Integer.parseInt(cantidadHabitacionesStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Cantidad de personas y habitaciones deben ser números enteros", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que el nombre no contenga caracteres prohibidos en Firebase
        if (nombrePropiedad.contains(".") || nombrePropiedad.contains("#") ||
                nombrePropiedad.contains("$") || nombrePropiedad.contains("[") ||
                nombrePropiedad.contains("]")) {
            Toast.makeText(this, "El nombre de la propiedad no puede contener . # $ [ ]", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el objeto Propiedad
        Propiedad propiedad = new Propiedad(nombrePropiedad, cantidadPersonas, cantidadHabitaciones, precio, ubicacion, amenidadesCasa);

        // Guardar la propiedad con el nombre de la casa como ID en Firebase
        databaseReference.child(nombrePropiedad).setValue(propiedad).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RegistroPropiedad.this, "Propiedad registrada exitosamente", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            } else {
                Toast.makeText(RegistroPropiedad.this, "Error al registrar la propiedad", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        nombrePropiedadInput.setText("");
        cantidadPersonasInput.setText("");
        cantidadHabitacionesInput.setText("");
        precioInput.setText("");
        ubicacionInput.setText("");
        amenidadesCasaInput.setText("");
    }

    // Método para mostrar el cuadro de selección múltiple para amenidades
    private void showAmenitiesDialog(TextInputEditText amenidadesEditText) {
        String[] amenidadesArray = {"Wifi", "Aire acondicionado", "Cocina equipada", "Lavadora", "Estacionamiento", "TV", "Patio", "Terraza", "Piscina"};
        boolean[] seleccionados = new boolean[amenidadesArray.length];
        ArrayList<String> amenidadesSeleccionadas = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona las amenidades");

        builder.setMultiChoiceItems(amenidadesArray, seleccionados, (dialog, which, isChecked) -> {
            if (isChecked) {
                amenidadesSeleccionadas.add(amenidadesArray[which]);
            } else {
                amenidadesSeleccionadas.remove(amenidadesArray[which]);
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            amenidadesEditText.setText(TextUtils.join(", ", amenidadesSeleccionadas));
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }


    // Clase para representar una propiedad
    public static class Propiedad {
        public String idPropiedad;
        public String nombrePropiedad;
        public int cantidadPersonas;
        public int cantidadHabitaciones;
        public String precio;
        public String ubicacion;
        public String amenidadesCasa;

        public Propiedad() {
            // Constructor vacío requerido para Firebase
        }

        public Propiedad(String nombrePropiedad, int cantidadPersonas, int cantidadHabitaciones,
                         String precio, String ubicacion, String amenidadesCasa) {
            this.nombrePropiedad = nombrePropiedad;
            this.cantidadPersonas = cantidadPersonas;
            this.cantidadHabitaciones = cantidadHabitaciones;
            this.precio = precio;
            this.ubicacion = ubicacion;
            this.amenidadesCasa = amenidadesCasa;
        }
    }
}

