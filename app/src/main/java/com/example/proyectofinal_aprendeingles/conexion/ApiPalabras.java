package com.example.proyectofinal_aprendeingles.conexion;

import com.example.proyectofinal_aprendeingles.basedatos.Palabra;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiPalabras {
    @GET("palabras")
    Call<ArrayList<Palabra>> getPalabras();

    @POST("palabras")
    Call<Palabra> postPalabra(
            @Body Palabra palabra
    );

    @PUT("palabras/{id}")
    Call<Palabra> putPalabra(
            @Path("id") int id,
            @Body Palabra palabra
    );

    @DELETE("palabras/{id}")
    Call<Palabra> deletePalabra(
            @Path("id") int id
    );
}
