package org.afgl.biblioapp.libro;

import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.webkit.WebResourceResponse;

import org.afgl.biblioapp.libro.events.LibroEvent;

/**
 * Created by arturo on 09/06/2017.
 * Presentador de la actividad libro
 */

public interface LibroPresenter {
    void onResume();
    void onPause();
    void onDestroy();

    void setBook(String fileName, AssetManager assetManager);
    WebResourceResponse fetch(Uri resourceUri, boolean isNightMode);
    void nextChapter(Uri resourceUri);
    void previousChapter(Uri resourceUri);
    void getIndex();

    void initTts(Activity activity);
    void startSpeak(Uri resourceUri, float velocity, boolean highlight);
    void stopSpeak();
    @SuppressWarnings("unused")
    void onEventMainThread(LibroEvent event);
}
