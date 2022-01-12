package com.example.proyectofinal_aprendeingles.repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.proyectofinal_aprendeingles.basedatos.BDRoom;
import com.example.proyectofinal_aprendeingles.basedatos.DaoPalabra;
import com.example.proyectofinal_aprendeingles.basedatos.Palabra;

import java.util.List;

public class RepositorioPalabra {
    private DaoPalabra daoPalabra;
    private LiveData<List<Palabra>> todasPalabras;

    public RepositorioPalabra(Application application){
        BDRoom db = BDRoom.getBD(application);
        daoPalabra = db.daoPalabra();
        todasPalabras = daoPalabra.getPalabras();
    }

    public LiveData<List<Palabra>> getTodasPalabras(){
        return todasPalabras;
    }

    public void insert(Palabra palabra){
        BDRoom.bdWriteExecutor.execute(
                () -> {
                    daoPalabra.insert(palabra);
                }
        );
    }

    public void update(Palabra palabra){
        BDRoom.bdWriteExecutor.execute(
                () -> {
                    daoPalabra.update(palabra.getId(),
                            palabra.getPalabra(),
                            palabra.getTraduccion(),
                            palabra.getTipo());
                }
        );
    }

    public void delete(Palabra palabra){
        BDRoom.bdWriteExecutor.execute(
                () -> { daoPalabra.delete(palabra.getId()); }
        );
    }
}
