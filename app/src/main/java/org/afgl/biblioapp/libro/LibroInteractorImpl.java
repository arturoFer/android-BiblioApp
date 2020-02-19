package org.afgl.biblioapp.libro;

import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.webkit.WebResourceResponse;

/**
 * Created by arturo on 09/06/2017.
 * Implementacion del interactuador del Libro
 */

public class LibroInteractorImpl implements LibroInteractor {

    private LibroRepository repository;

    public LibroInteractorImpl(LibroRepository repository) {
        this.repository = repository;
    }

    @Override
    public void setBook(String filename, AssetManager assetManager) {
        repository.setBook(filename, assetManager);
    }

    @Override
    public WebResourceResponse fetch(Uri resourceUri, boolean isNightMode) {
        return repository.fetch(resourceUri, isNightMode);
    }

    @Override
    public void nextChapter(Uri resourceUri) {
        repository.nextChapter(resourceUri);
    }

    @Override
    public void previousChapter(Uri resourceUri) {
        repository.previousChapter(resourceUri);
    }

    @Override
    public void getIndex() {
        repository.getIndex();
    }

    @Override
    public void startSpeak(Uri resourceUri, float velocity, boolean highlight) {
        repository.startSpeak(resourceUri, velocity, highlight);
    }

    @Override
    public void stopSpeak() {
        repository.stopSpeak();
    }

    @Override
    public void onDestroy() {
        repository.onDestroy();
    }

    @Override
    public void initTts(Activity activity) {
        repository.initTts(activity);
    }

}
