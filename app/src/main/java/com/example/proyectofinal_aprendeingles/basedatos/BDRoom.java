package com.example.proyectofinal_aprendeingles.basedatos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Palabra.class}, version = 1, exportSchema = false)
public abstract class BDRoom extends RoomDatabase {
    public abstract DaoPalabra daoPalabra();
    private static volatile BDRoom INSTANCIA;
    private static final int NUM_HILOS = 4;

    public static final ExecutorService bdWriteExecutor = Executors.newFixedThreadPool(NUM_HILOS);

    public static BDRoom getBD(final Context context){
        if (INSTANCIA == null){
            synchronized (BDRoom.class){
                if (INSTANCIA == null){
                    INSTANCIA = Room.databaseBuilder(context
                    .getApplicationContext(), BDRoom.class,
                            "basedatos_palabra")
                            .build();
                }
            }
        }
        return INSTANCIA;
    }
}
