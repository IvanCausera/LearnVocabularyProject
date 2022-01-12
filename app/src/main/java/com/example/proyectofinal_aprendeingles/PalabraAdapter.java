package com.example.proyectofinal_aprendeingles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinal_aprendeingles.basedatos.Palabra;
import com.example.proyectofinal_aprendeingles.viewmodel.PalabraViewModel;

import java.util.ArrayList;

public class PalabraAdapter extends RecyclerView.Adapter<PalabraAdapter.PalabraViewHolder>{

    private Context context;
    private ArrayList<Palabra> list;
    private View.OnClickListener onClickListener;

    public PalabraAdapter(Context context){
        this.context = context;
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public PalabraAdapter.PalabraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_elements, parent, false);
        view.setOnClickListener(onClickListener);
        PalabraViewHolder viewHolder = new PalabraViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PalabraAdapter.PalabraViewHolder holder, int position) {
        Palabra p = list.get(position);

        if (p.isFav()) holder.imgHearth.setImageResource(R.drawable.heart);
        else holder.imgHearth.setImageResource(R.drawable.heart_outlined);

        holder.imgHearth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).setPosPulsada(position);
                PalabraViewModel viewModel = ((MainActivity) context).getPalabraViewModel();
                if (!p.isFav()) {
                    viewModel.insert(p);
                    p.setFav(true);
                } else {
                    viewModel.delete(p);
                    p.setFav(false);
                }

                ((MainActivity) context).putDatos(p);
            }
        });

        holder.txtSpanish.setText(p.getPalabra());
        holder.txtEnglish.setText(p.getTraduccion());
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        else
            return list.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public void addToList(ArrayList<Palabra> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void addToList(Palabra palabra){
        this.list.add(palabra);
        notifyDataSetChanged();
    }

    public void updateList(int pos, Palabra palabra){
        this.list.set(pos, palabra);
        notifyItemChanged(pos);
    }

    public void removeFromList(int pos){
        this.list.remove(pos);
        notifyDataSetChanged();
    }

    public ArrayList<Palabra> getList() {
        return list;
    }

    class PalabraViewHolder extends RecyclerView.ViewHolder{
        TextView txtSpanish;
        TextView txtEnglish;
        ImageView imgHearth;
        public PalabraViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSpanish = itemView.findViewById(R.id.txtSpanish_list);
            txtEnglish = itemView.findViewById(R.id.txtEnglish_list);
            imgHearth = itemView.findViewById(R.id.imgHearth_list);
        }
    }
}
