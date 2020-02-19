package org.afgl.biblioapp.estanteria.events;

import org.afgl.biblioapp.entities.Libro;

import java.util.List;

/**
 * Created by arturo on 08/06/2017.
 * Eventos estanteria
 */

public class EstanteriaEvent {
    private int type;
    private List<Libro> libros;
    private String error;

    public final static int READ_EVENT = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
