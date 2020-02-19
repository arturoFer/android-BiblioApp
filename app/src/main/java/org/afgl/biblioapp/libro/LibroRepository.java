package org.afgl.biblioapp.libro;

import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.webkit.WebResourceResponse;

/**
 * Created by arturo on 09/06/2017.
 * Repositorio de libro
 */

public interface LibroRepository {
    void setBook(String fileName, AssetManager assetManager);
    WebResourceResponse fetch(Uri resourceUri, boolean isNightMode);
    void nextChapter(Uri resourceUri);
    void previousChapter(Uri resourceUri);
    void getIndex();

    void initTts(Activity activity);
    void startSpeak(Uri resourceUri, float velocity, boolean highlight);
    void stopSpeak();
    void onDestroy();
}
