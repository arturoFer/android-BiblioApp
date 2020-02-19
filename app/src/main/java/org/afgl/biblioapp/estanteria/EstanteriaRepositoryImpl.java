package org.afgl.biblioapp.estanteria;

import android.content.res.AssetManager;

import org.afgl.biblioapp.domain.BookWrapper;
import org.afgl.biblioapp.entities.Libro;
import org.afgl.biblioapp.estanteria.events.EstanteriaEvent;
import org.afgl.biblioapp.libs.base.Eventbus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by arturo on 08/06/2017.
 * Implementacion repository
 */

public class EstanteriaRepositoryImpl implements EstanteriaRepository{

    private Eventbus eventbus;

    public EstanteriaRepositoryImpl(Eventbus eventbus) {
        this.eventbus = eventbus;
    }

    @Override
    public void getLibros(AssetManager assetManager) {
        String[] paths = null;
        List<Libro> libros = new ArrayList<>();
        EstanteriaEvent event = new EstanteriaEvent();
        event.setType(EstanteriaEvent.READ_EVENT);
        try {
            paths = assetManager.list("books");
        } catch (IOException e){
            event.setError("Error al abrir el directorio books");
        }
        if(paths != null){
            for(String path : paths){
                Libro libro = BookWrapper.getLibroFromPath(assetManager, path);
                libro.setLocation("books/" + path);
                libros.add(libro);
            }
            event.setLibros(libros);

        } else{
            event.setError("No se encontró ningún archivo epub");
        }
        eventbus.post(event);
    }

}
