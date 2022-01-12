package com.example.proyectofinal_aprendeingles.basedatos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoPalabra {
    @Query("SELECT * from palabras")
    LiveData<List<Palabra>> getPalabras();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Palabra palabra);

    @Query("UPDATE palabras SET palabra = :palabra, traduccion = :traduccion, tipo = :tipo where id = :id")
    void update(int id, String palabra, String traduccion, int tipo);

    @Query("DELETE FROM palabras WHERE id = :id")
    void delete(int id);
}
