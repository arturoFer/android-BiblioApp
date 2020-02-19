package org.afgl.biblioapp.libro.ui;

/**
 * Created by arturo on 09/06/2017.
 * interfaz del detector de gestos
 */
interface MyGestureListener {
    void onNext();
    void onPrevious();
    boolean pageUpDown(float y);
}
