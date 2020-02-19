package org.afgl.biblioapp.capitulos.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.afgl.biblioapp.R;
import org.afgl.biblioapp.entities.Capitulo;

import java.util.List;

/**
 * Created by arturo on 09/06/2017.
 * Adaptador actividad capitulos
 */

public class CapitulosAdapter extends RecyclerView.Adapter<CapitulosAdapter.ViewHolder> {

    private List<Capitulo> dataset;
    private final OnCapituloClickListener listener;

    public CapitulosAdapter(List<Capitulo> dataset, OnCapituloClickListener listener) {
        this.dataset = dataset;
        this.listener = listener;
    }

    public void setCapitulos(List<Capitulo> capitulos){
        this.dataset = capitulos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.capitulo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Capitulo currentCapitulo = dataset.get(position);
        holder.capitulo.setText(currentCapitulo.getTitle());
        holder.setClickListener(currentCapitulo, listener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        private View view;

        private TextView capitulo;

        ViewHolder(View itemView){
            super(itemView);
            this.view = itemView;

            this.capitulo = this.view.findViewById(R.id.capitulo);
        }

        void setClickListener(final Capitulo capitulo, final OnCapituloClickListener listener){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(capitulo);
                }
            });
        }
    }
}
