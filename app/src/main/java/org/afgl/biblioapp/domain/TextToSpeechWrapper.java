package org.afgl.biblioapp.domain;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by arturo on 19/06/2017.
 * Clase que encapsula la clase TextToSpeech de android
 */

public class TextToSpeechWrapper {
    private TextToSpeech mTts;
    private UtteranceProgressListener mCompletedListener;

    private static final String KEY_PARAM_UTTERANCE_ID = "END";

    public TextToSpeechWrapper(Activity activity){
        TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS){
                    Locale loc = new Locale("spa", "ESP");
                    if(mTts.isLanguageAvailable(loc) != 0){
                        mTts.setLanguage(loc);
                    }
                    //mTts.setLanguage(Locale.US);
                    mTts.setOnUtteranceProgressListener(mCompletedListener);
                }
            }
        };

        mTts = new TextToSpeech(activity, onInitListener);
    }

    public void setOnUtteranceProgressListener(UtteranceProgressListener listener){
        mCompletedListener = listener;
        if(mTts != null){
            mTts.setOnUtteranceProgressListener(listener);
        }
    }

    public void onDestroy(){
        if(mTts != null){
            mTts.shutdown();
        }
        mTts = null;
    }

    @SuppressWarnings("deprecation")
    public void speak(String text){
        if(mTts != null){
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                HashMap<String, String> params = new HashMap<>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, KEY_PARAM_UTTERANCE_ID);
                mTts.speak(text, TextToSpeech.QUEUE_ADD, params);
            }else{
                Bundle params = new Bundle();
                params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, KEY_PARAM_UTTERANCE_ID);
                mTts.speak(text, TextToSpeech.QUEUE_ADD, params, KEY_PARAM_UTTERANCE_ID);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void pause(){
        if(mTts != null){
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                mTts.playSilence(600, TextToSpeech.QUEUE_ADD, null);
            } else{
                mTts.playSilentUtterance(600, TextToSpeech.QUEUE_ADD, null);
            }
        }
    }

    public void stop(){
        if(mTts != null){
            setOnUtteranceProgressListener(null);
            mTts.stop();
        }
    }

    public void setVelocity(float velocity){
        if(mTts != null){
            mTts.setSpeechRate(velocity);
        }
    }
}
