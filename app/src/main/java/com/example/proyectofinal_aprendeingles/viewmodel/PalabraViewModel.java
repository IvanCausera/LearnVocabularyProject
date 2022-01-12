package com.example.proyectofinal_aprendeingles.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proyectofinal_aprendeingles.basedatos.Palabra;
import com.example.proyectofinal_aprendeingles.repositorio.RepositorioPalabra;

import java.util.List;

public class PalabraViewModel extends AndroidViewModel {
     private RepositorioPalabra repositorioPalabra;
     private LiveData<List<Palabra>> todasPalabras;

     public PalabraViewModel(Application application){
         super(application);
         repositorioPalabra = new RepositorioPalabra(application);

         todasPalabras = repositorioPalabra.getTodasPalabras();
     }

     public LiveData<List<Palabra>> getTodasPalabras(){
         return todasPalabras;
     }

     public void insert(Palabra palabra){
         repositorioPalabra.insert(palabra);
     }

     public void update(Palabra palabra){
         repositorioPalabra.update(palabra);
     }

     public void delete(Palabra palabra){ repositorioPalabra.delete(palabra); }
}
