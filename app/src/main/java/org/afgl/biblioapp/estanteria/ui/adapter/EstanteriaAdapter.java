package org.afgl.biblioapp.estanteria.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.afgl.biblioapp.R;
import org.afgl.biblioapp.entities.Libro;
import org.afgl.biblioapp.libs.base.ImageLoader;

import java.util.List;

/**
 * Created by arturo on 08/06/2017.
 * Adaptador del recyclerview de la actividad estanteria
 */

public class EstanteriaAdapter extends RecyclerView.Adapter<EstanteriaAdapter.ViewHolder> {

    private List<Libro> dataset;
    private final OnItemClickListener listener;
    private final ImageLoader loader;

    public EstanteriaAdapter(List<Libro> dataset, OnItemClickListener listener, ImageLoader loader){
        this.dataset = dataset;
        this.listener = listener;
        this.loader = loader;
    }

    public void setBooks(List<Libro> libros){
        dataset = libros;
        notifyDataSetChanged();
    }

    public ImageLoader getImageLoader(){
        return loader;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Libro currentLibro = dataset.get(position);

        String titulo = currentLibro.getTitulo();
        if(titulo.contains("aventurero") || titulo.contains("Buscón")){
            holder.titulo.setTextSize(13.5f);
        } else if(titulo.contains("Lazarillo") || titulo.contains("Alfarache")){
            holder.titulo.setTextSize(12.75f);
        } else if(titulo.contains("Segunda")){
            holder.titulo.setTextSize(11.75f);
        }
        holder.titulo.setText(titulo);
        holder.autor.setText(currentLibro.getAutor());
        loader.load(holder.portada, currentLibro.getPortada());

        holder.setClickListener(currentLibro, listener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private View view;

        private ImageView portada;
        private TextView titulo;
        private TextView autor;

        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            this.titulo = itemView.findViewById(R.id.titulo);
            this.autor = itemView.findViewById(R.id.autor);
            this.portada = itemView.findViewById(R.id.portada);
        }

        void setClickListener(final Libro libro, final OnItemClickListener listener){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(libro);
                }
            });
        }
    }
}
