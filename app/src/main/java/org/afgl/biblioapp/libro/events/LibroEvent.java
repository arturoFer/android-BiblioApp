package org.afgl.biblioapp.libro.events;

import android.net.Uri;

import org.afgl.biblioapp.entities.Capitulo;

import java.util.List;

/**
 * Created by arturo on 09/06/2017.
 * Eventos Lanzados
 */

public class LibroEvent {
    private int type;
    private String error;
    private Uri resourceUri;
    private List<Capitulo> capitulos;
    private String textHighlight;

    public static final int SET_BOOK_EVENT = 0;
    public static final int NEXT_CHAPTER_EVENT = 1;
    public static final int PREVIOUS_CHAPTER_EVENT = 2;
    public static final int GET_INDEX = 3;
    public static final int SPEAK = 4;
    public static final int HIGHLIGHT_SPEAK = 5;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Uri getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(Uri resourceUri) {
        this.resourceUri = resourceUri;
    }

    public List<Capitulo> getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(List<Capitulo> capitulos) {
        this.capitulos = capitulos;
    }

    public String getTextHighlight() {
        return textHighlight;
    }

    public void setTextHighlight(String textHighlight) {
        this.textHighlight = textHighlight;
    }
}
