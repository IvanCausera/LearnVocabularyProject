package com.example.proyectofinal_aprendeingles.conexion;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Cliente {
    private static final String URL = "http://10.0.2.2:3000";
    private static Retrofit retrofit = null;

    public static Retrofit getCliente(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }

}
