package org.afgl.biblioapp.libro.ui;

import android.net.Uri;

import org.afgl.biblioapp.entities.Capitulo;

import java.util.List;

/**
 * Created by arturo on 09/06/2017.
 * Interface Vista Libro
 */

public interface LibroView {
    void firstChapter(Uri uri);
    void showError(String error);
    void changeChapter(Uri resourceUri);
    void launchChaptersList(List<Capitulo> capitulos);
    void endSpeak();
    void highLightSpeak(final String speak);
}
