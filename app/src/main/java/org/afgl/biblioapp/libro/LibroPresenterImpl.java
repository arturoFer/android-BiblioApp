package org.afgl.biblioapp.libro;

import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.webkit.WebResourceResponse;

import org.afgl.biblioapp.libro.events.LibroEvent;
import org.afgl.biblioapp.libro.ui.LibroView;
import org.afgl.biblioapp.libs.base.Eventbus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by arturo on 09/06/2017.
 * Implementacion del presentador
 */

public class LibroPresenterImpl implements LibroPresenter{

    private Eventbus eventbus;
    private LibroView view;
    private LibroInteractor interactor;

    public LibroPresenterImpl(Eventbus eventbus, LibroView view, LibroInteractor interactor) {
        this.eventbus = eventbus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onResume() {
        eventbus.register(this);
    }

    @Override
    public void onPause() {
        eventbus.unregister(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        interactor.onDestroy();
    }

    @Override
    public void setBook(String fileName, AssetManager assetManager) {
        interactor.setBook(fileName, assetManager);
    }

    @Override
    public WebResourceResponse fetch(Uri resourceUri, boolean isNightMode) {
        return interactor.fetch(resourceUri, isNightMode);
    }

    @Override
    public void nextChapter(Uri resourceUri) {
        interactor.nextChapter(resourceUri);
    }

    @Override
    public void previousChapter(Uri resourceUri) {
        interactor.previousChapter(resourceUri);
    }

    @Override
    public void getIndex() {
        interactor.getIndex();
    }

    @Override
    public void initTts(Activity activity) {
        interactor.initTts(activity);
    }

    @Override
    public void startSpeak(Uri resourceUri, float velocity, boolean highlight) {
        interactor.startSpeak(resourceUri, velocity, highlight);
    }

    @Override
    public void stopSpeak() {
        interactor.stopSpeak();
    }

    @Override
    @Subscribe
    public void onEventMainThread(LibroEvent event) {
        String error = event.getError();
        switch(event.getType()){
            case LibroEvent.SET_BOOK_EVENT:
                if(error == null){
                    view.firstChapter(event.getResourceUri());
                } else{
                    view.showError(error);
                }
                break;
            case LibroEvent.NEXT_CHAPTER_EVENT:
            case LibroEvent.PREVIOUS_CHAPTER_EVENT:
                if(error == null){
                    view.changeChapter(event.getResourceUri());
                } else{
                    view.showError(event.getError());
                }
                break;
            case LibroEvent.GET_INDEX:
                if(error == null){
                    view.launchChaptersList(event.getCapitulos());
                } else{
                    view.showError(event.getError());
                }
                break;
            case LibroEvent.SPEAK:
                view.endSpeak();
                if(error != null){
                    view.showError(event.getError());
                }
                break;
            case LibroEvent.HIGHLIGHT_SPEAK:
                view.highLightSpeak(event.getTextHighlight());
                break;
        }
    }
}
