package com.example.proyectofinal_aprendeingles;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.proyectofinal_aprendeingles.basedatos.Palabra;
import com.example.proyectofinal_aprendeingles.conexion.ApiPalabras;
import com.example.proyectofinal_aprendeingles.conexion.Cliente;
import com.example.proyectofinal_aprendeingles.viewmodel.PalabraViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    public final static String BORRAR = "borrar";

    private DrawerLayout drawerLayout;

    private static PalabraAdapter palabraAdapter;
    private int posPulsada;
    private Retrofit retrofit;
    private static ArrayList<Palabra> listPalabra;
    private static int fragmentTipe;

    private PalabraViewModel palabraViewModel;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ActivityResultLauncher<Intent> resultInsert = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent datosResult = result.getData();

                        Palabra p = new Palabra();
                        p.setPalabra(datosResult.getStringExtra("palabra"));
                        p.setTraduccion(datosResult.getStringExtra("traduccion"));
                        p.setTipo(datosResult.getIntExtra("tipo", 0));
                        p.setFav(false);

                        postDatos(p);
                    }
                }
            }
    );

    ActivityResultLauncher<Intent> resultPutRemove = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent datosResult = result.getData();
                        if (datosResult.getBooleanExtra(BORRAR, true)){
                            removeDatos(datosResult.getIntExtra("id", 0));
                        } else {
                            Palabra p = new Palabra();
                            p.setId(datosResult.getIntExtra("id", 0));
                            p.setPalabra(datosResult.getStringExtra("palabra"));
                            p.setTraduccion(datosResult.getStringExtra("traduccion"));
                            p.setTipo(datosResult.getIntExtra("tipo", 0));
                            p.setFav(datosResult.getBooleanExtra("fav", false));

                            putDatos(p);
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = Cliente.getCliente();
        getDatos();
        palabraViewModel = new ViewModelProvider(this).get(PalabraViewModel.class);

        //Preferences
        Preferencias.CambiarModo(this);

        //UI
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navview);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                boolean fragmentTransaction = false;
                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.menu_seccion_num:
                        fragment = new fragmentNum();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_seccion_dias:
                        fragment = new fragmentDias();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_seccion_colores:
                        fragment = new fragmentColores();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_seccion_fav:
                        fragment = new fragmentFav();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_seccion_add:
                        Intent intent = new Intent(MainActivity.this, Insertar.class);
                        resultInsert.launch(intent);
                        break;
                    case R.id.menu_seccion_preferencias:
                        Intent intentPref = new Intent(MainActivity.this, Preferencias.class);
                        startActivity(intentPref);
                }

                if (fragmentTransaction){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .addToBackStack(null)
                            .commit();
                    item.setChecked(true);
                    setTitle(item.getTitle());
                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

    } //En onCreate

    public PalabraViewModel getPalabraViewModel() {
        return palabraViewModel;
    }

    public int getPosPulsada() {
        return posPulsada;
    }

    public void setPosPulsada(int posPulsada) {
        this.posPulsada = posPulsada;
    }

    public static void setFragmentTipe(int fragmentTipe) {
        MainActivity.fragmentTipe = fragmentTipe;
    }

    public static ArrayList<Palabra> getListPalabra() {
        return listPalabra;
    }

    public static PalabraAdapter getPalabraAdapter() {
        return palabraAdapter;
    }

    public static void setPalabraAdapter(PalabraAdapter palabraAdapter) {
        MainActivity.palabraAdapter = palabraAdapter;
    }

    private void getDatos(){
        ApiPalabras api = retrofit.create(ApiPalabras.class);

        Call<ArrayList<Palabra>> respuesta = api.getPalabras();

        respuesta.enqueue(new Callback<ArrayList<Palabra>>() {
            @Override
            public void onResponse(Call<ArrayList<Palabra>> call, Response<ArrayList<Palabra>> response) {
                if (response.isSuccessful()){
                    listPalabra = response.body();
                } else{
                    Toast.makeText(getApplicationContext(),
                            R.string.fallo_respuesta, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Palabra>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postDatos(Palabra palabra){
        ApiPalabras api = retrofit.create(ApiPalabras.class);

        Call<Palabra> p = api.postPalabra(palabra);

        p.enqueue(new Callback<Palabra>() {
            @Override
            public void onResponse(Call<Palabra> call, Response<Palabra> response) {
                if (response.isSuccessful()){
                    Palabra p = response.body();
                    if (p.getTipo() == fragmentTipe)  palabraAdapter.addToList(p);
                    listPalabra.add(p);
                } else{
                    Toast.makeText(getApplicationContext(),
                            R.string.fallo_respuesta, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Palabra> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void putDatos(Palabra palabra){
        ApiPalabras api = retrofit.create(ApiPalabras.class);
        Call<Palabra> p = api.putPalabra(palabra.getId(), palabra);

        p.enqueue(new Callback<Palabra>() {
            @Override
            public void onResponse(Call<Palabra> call, Response<Palabra> response) {
                if (response.isSuccessful()){
                    Palabra p = response.body();
                    if (p.getTipo() == fragmentTipe)  palabraAdapter.updateList(posPulsada, p);
//                    else palabraAdapter.removeFromList(posPulsada);

                    if (p.isFav()) palabraViewModel.update(p);

                    int index = listPalabra.indexOf(palabra);
                    listPalabra.set(index, p);
                } else{
                    Toast.makeText(getApplicationContext(),
                            R.string.fallo_respuesta, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Palabra> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeDatos(int id){
        ApiPalabras api = retrofit.create(ApiPalabras.class);
        Call<Palabra> p = api.deletePalabra(id);

        p.enqueue(new Callback<Palabra>() {
            @Override
            public void onResponse(Call<Palabra> call, Response<Palabra> response) {
                if (response.isSuccessful()){
                    palabraAdapter.removeFromList(posPulsada);
                    Palabra eliminar = new Palabra();
                    eliminar.setId(id);

                    int index = listPalabra.indexOf(eliminar);
                    Palabra p = listPalabra.remove(index);
                    if (p.isFav()) palabraViewModel.delete(p);
                } else{
                    Toast.makeText(getApplicationContext(),
                            R.string.fallo_respuesta, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Palabra> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}