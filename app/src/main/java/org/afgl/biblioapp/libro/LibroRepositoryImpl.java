package org.afgl.biblioapp.libro;

import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.speech.tts.UtteranceProgressListener;
import android.webkit.WebResourceResponse;

import org.afgl.biblioapp.domain.BookWrapper;
import org.afgl.biblioapp.domain.TextToSpeechWrapper;
import org.afgl.biblioapp.entities.Capitulo;
import org.afgl.biblioapp.libro.events.LibroEvent;
import org.afgl.biblioapp.libs.base.Eventbus;
import org.afgl.biblioapp.utilities.XMLUtil;
import org.afgl.biblioapp.utilities.XhtmlToText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arturo on 09/06/2017.
 * Repositorio de Libro
 */

public class LibroRepositoryImpl implements LibroRepository {

    private Eventbus eventbus;

    private BookWrapper book;

    private TextToSpeechWrapper mTtsWrapper;
    private ArrayList<String> mText;
    private int mTextLine;

    private boolean mHighlight;

    public LibroRepositoryImpl(Eventbus eventbus) {
        this.eventbus = eventbus;
    }

    @Override
    public void initTts(Activity activity) {
        mTtsWrapper = new TextToSpeechWrapper(activity);
    }

    @Override
    public void setBook(String fileName, AssetManager assetManager) {
        if((book == null) || !book.getFilename().equals(fileName)) {
            book = new BookWrapper(fileName, assetManager);
            LibroEvent event = new LibroEvent();
            event.setType(LibroEvent.SET_BOOK_EVENT);
            if(book != null){
                Uri resourceName = book.firstChapter();
                event.setResourceUri(resourceName);
            } else{
                event.setError("No se pudo cargar el libro seleccionado");
            }
            eventbus.post(event);
        }
    }

    @Override
    public WebResourceResponse fetch(Uri resourceUri, boolean isNightMode) {
        String resource = BookWrapper.url2ResourceName(resourceUri);
        return book.fetch(resource, isNightMode);
    }

    @Override
    public void nextChapter(Uri resourceUri) {
        Uri nextResource = book.nextResource(resourceUri);
        LibroEvent event = new LibroEvent();
        event.setType(LibroEvent.NEXT_CHAPTER_EVENT);
        if(nextResource == null){
            event.setError("Se alcanzó el final del libro");
        } else{
            event.setResourceUri(nextResource);
        }
        eventbus.post(event);
    }

    @Override
    public void previousChapter(Uri resourceUri) {
        Uri previousResource = book.previousResource(resourceUri);
        LibroEvent event = new LibroEvent();
        event.setType(LibroEvent.PREVIOUS_CHAPTER_EVENT);
        if(previousResource == null){
            event.setError("Se alcanzo el inicio del libro");
        } else{
            event.setResourceUri(previousResource);
        }
        eventbus.post(event);
    }

    @Override
    public void getIndex() {
        List<Capitulo> capitulos = new ArrayList<>();
        book.getTableOfContents(capitulos);
        LibroEvent event = new LibroEvent();
        event.setType(LibroEvent.GET_INDEX);
        if(capitulos.size() == 0){
            event.setError("No se pudo obtener el índice del libro");
        } else{
            event.setCapitulos(capitulos);
        }
        eventbus.post(event);
    }

    @Override
    public void startSpeak(Uri resourceUri, float velocity, boolean highlight) {
        if((book != null) && (resourceUri != null)){
            String url = resourceUri.toString();
            if(url.contains("cover")){
                setErrorSpeak("Este capítulo no contiene texto, sólo imágenes. No se puede leer en voz alta.");
            } else {
                mText = new ArrayList<>();
                XMLUtil.parseXmlResource(book.getResourceByUri(resourceUri), new XhtmlToText(mText));
                mTextLine = 0;
                mTtsWrapper.setVelocity(velocity);
                mTtsWrapper.setOnUtteranceProgressListener(mListener);
                mHighlight = highlight;
                if (0 < mText.size()) {
                    mTtsWrapper.speak(mText.get(0));
                    if(mHighlight) {
                        setHighLightSpeak();
                    }
                } else {
                    setErrorSpeak("No se obtuvo ningún texto de este capítulo. No se puede leer en voz alta.");
                }
            }
        }
    }

    private void setErrorSpeak(String error){
        LibroEvent event = new LibroEvent();
        event.setType(LibroEvent.SPEAK);
        event.setError(error);
        eventbus.post(event);
    }

    private void setHighLightSpeak(){
        LibroEvent event = new LibroEvent();
        event.setType(LibroEvent.HIGHLIGHT_SPEAK);
        event.setTextHighlight(mText.get(mTextLine));
        eventbus.post(event);
    }

    private UtteranceProgressListener mListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String s) {

        }

        @Override
        public void onDone(String s) {
            if(mTextLine < mText.size()-1){
                mTtsWrapper.pause();
                mTtsWrapper.speak(mText.get(++mTextLine));
                if(mHighlight) {
                    setHighLightSpeak();
                }
            } else{
                LibroEvent event = new LibroEvent();
                event.setType(LibroEvent.SPEAK);
                eventbus.post(event);
            }
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onError(String s) {

        }

    };

    @Override
    public void stopSpeak() {
        if(mTtsWrapper != null){
            mTtsWrapper.setOnUtteranceProgressListener(null);
            mTtsWrapper.stop();
            mText = null;
        }
    }

    @Override
    public void onDestroy() {
        if(mTtsWrapper != null) {
            mTtsWrapper.onDestroy();
        }
    }

}
