package org.afgl.biblioapp.estanteria.ui;

import org.afgl.biblioapp.entities.Libro;

import java.util.List;

/**
 * Created by arturo on 08/06/2017.
 * Interfaz de la vista
 */

public interface EstanteriaView {
    void setBooks(List<Libro> libros);
    void showBook(Libro libro);
    void showError(String error);
}
