package org.afgl.biblioapp.estanteria.ui.adapter;

import org.afgl.biblioapp.entities.Libro;

/**
 * Created by arturo on 08/06/2017.
 * Interfaz atencion click en recyclerview item
 */

public interface OnItemClickListener {
    void OnItemClick(Libro libro);
}
