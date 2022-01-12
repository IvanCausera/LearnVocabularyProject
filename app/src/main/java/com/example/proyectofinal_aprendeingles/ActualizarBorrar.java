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
import android.widget.Toast;

import javax.xml.transform.Result;

public class ActualizarBorrar extends AppCompatActivity {

    private int tipoPalabra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_borrar);

        Spinner spinner = findViewById(R.id.spinnerTipo_actualizar);

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

        EditText txtPalabra = findViewById(R.id.editPalabra_actualizar);
        EditText txtTraduccion = findViewById(R.id.editTraduccion_actualizar);

        Button btnActualizar = findViewById(R.id.btnActualizar_actualizar);
        Button btnBorrar = findViewById(R.id.btnBorrar_actualizar);

        Intent intent = getIntent();
        if (intent != null){
            txtPalabra.setText(intent.getStringExtra("palabra"));
            txtTraduccion.setText(intent.getStringExtra("traduccion"));
            tipoPalabra = intent.getIntExtra("tipo", 0);
            spinner.setSelection(tipoPalabra);

            btnActualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentMain = new Intent();

                    intentMain.putExtra(MainActivity.BORRAR, false);
                    intentMain.putExtra("id", intent.getIntExtra("id", 0));
                    intentMain.putExtra("palabra", txtPalabra.getText().toString());
                    intentMain.putExtra("traduccion", txtTraduccion.getText().toString());
                    intentMain.putExtra("tipo", spinner.getSelectedItemPosition());
                    intentMain.putExtra("fav", intent.getBooleanExtra("fav", false));

                    setResult(RESULT_OK, intentMain);

                    finish();
                }
            });

            btnBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentMain = new Intent();

                    intentMain.putExtra(MainActivity.BORRAR, true);
                    intentMain.putExtra("id", intent.getIntExtra("id", 0));

                    setResult(RESULT_OK, intentMain);
                    finish();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.fallo_respuesta, Toast.LENGTH_SHORT).show();
        }

    }
}