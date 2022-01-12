package com.example.proyectofinal_aprendeingles.basedatos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "palabras")
public class Palabra {
    public static final int TIPO_NUM = 0;
    public static final int TIPO_DIA = 1;
    public static final int TIPO_COLOR = 2;

    public static final int FAV_YES = 1;
    public static final int FAV_NO = 0;

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    private int id;

    @NonNull
    @ColumnInfo(name = "palabra")
    @SerializedName("palabra")
    @Expose
    private String palabra;

    @NonNull
    @ColumnInfo(name = "traduccion")
    @SerializedName("traduccion")
    @Expose
    private String traduccion;

    @NonNull
    @ColumnInfo(name = "tipo")
    @SerializedName("tipo")
    @Expose
    private int tipo;

    @NonNull
    @ColumnInfo(name = "fav")
    @SerializedName("fav")
    @Expose
    private boolean fav;

    public Palabra(){}

    public Palabra(@NonNull String palabra, @NonNull String traduccion, int tipo) {
        this.palabra = palabra;
        this.traduccion = traduccion;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getPalabra() {
        return palabra;
    }

    @NonNull
    public String getTraduccion() {
        return traduccion;
    }

    public int getTipo() {
        return tipo;
    }

    public boolean isFav() {
        return fav;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPalabra(@NonNull String palabra) {
        this.palabra = palabra;
    }

    public void setTraduccion(@NonNull String traduccion) {
        this.traduccion = traduccion;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Palabra palabra = (Palabra) o;
        return id == palabra.id;
    }
}
