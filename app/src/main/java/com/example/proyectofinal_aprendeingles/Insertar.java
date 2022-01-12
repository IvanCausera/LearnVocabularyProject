package com.example.proyectofinal_aprendeingles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class Insertar extends AppCompatActivity {

    private int tipoPalabra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar);

        Spinner spinner = findViewById(R.id.spinnerTipo_insert);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipoPalabras, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoPalabra = parent.getSelectedItemPosition();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText txtPalabra = findViewById(R.id.editPalabra_insert);
        EditText txtTraduccion = findViewById(R.id.editTraduccion_insert);

        Button btn = findViewById(R.id.insertarBoton_insert);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.putExtra("palabra", txtPalabra.getText().toString());
                intent.putExtra("traduccion", txtTraduccion.getText().toString());
                intent.putExtra("tipo", tipoPalabra);

                setResult(RESULT_OK, intent);

                finish();
            }
        });

    }
}