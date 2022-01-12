package com.example.proyectofinal_aprendeingles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal_aprendeingles.basedatos.Palabra;

import java.util.ArrayList;

public class fragmentFav extends Fragment {

    private RecyclerView recyclerView;
    private PalabraAdapter palabraAdapter;
    private int posPulsada;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        recyclerView = view.findViewById(R.id.recyclerFav);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(
                getContext(), 1));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        MainActivity.setFragmentTipe(-1);

        palabraAdapter = new PalabraAdapter(getContext());

        palabraAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posPulsada = recyclerView.getChildAdapterPosition(v);

                Palabra p = palabraAdapter.getList().get(posPulsada);
                ((MainActivity) getActivity()).setPosPulsada(posPulsada);
                MainActivity.setFragmentTipe(p.getTipo());

                Intent intent = new Intent(getContext(), ActualizarBorrar.class);

                intent.putExtra("id", p.getId());
                intent.putExtra("palabra", p.getPalabra());
                intent.putExtra("traduccion", p.getTraduccion());
                intent.putExtra("tipo", p.getTipo());
                intent.putExtra("fav", p.isFav());

                ((MainActivity) getActivity()).resultPutRemove.launch(intent);

            }
        });
        recyclerView.setAdapter(palabraAdapter);

        ArrayList<Palabra> filterList = new ArrayList<>();
        for (Palabra p:
                MainActivity.getListPalabra()) {
            if (p.isFav()) filterList.add(p);
        }

        palabraAdapter.addToList(filterList);
        MainActivity.setPalabraAdapter(palabraAdapter);
        return view;
    }
}