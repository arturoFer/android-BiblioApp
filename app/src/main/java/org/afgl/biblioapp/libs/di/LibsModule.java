package org.afgl.biblioapp.libs.di;

import android.app.Activity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.afgl.biblioapp.libs.GlideImageLoader;
import org.afgl.biblioapp.libs.MyEventbus;
import org.afgl.biblioapp.libs.base.Eventbus;
import org.afgl.biblioapp.libs.base.ImageLoader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by arturo on 06/06/2017.
 * Modulo inyeccion dependencias de la librerias Glide y Eventbus
 */

@Module
public class LibsModule {

    private Activity activity;

    public LibsModule(){}

    public LibsModule(Activity activity){
        this.activity = activity;
    }

    @Provides
    @Singleton
    Eventbus providesEventBus(org.greenrobot.eventbus.EventBus eventBus){
        return new MyEventbus(eventBus);
    }

    @Provides
    @Singleton
    org.greenrobot.eventbus.EventBus providesLibraryEventBus(){
        return org.greenrobot.eventbus.EventBus.getDefault();
    }

    @Provides
    @Singleton
    ImageLoader providesImageLoader(RequestManager requestManager){
        return new GlideImageLoader(requestManager);
    }

    @Provides
    @Singleton
    RequestManager providesRequestManager(){
        return Glide.with(activity);
    }

    @Provides
    @Singleton
    Activity providesActivity(){
        return this.activity;
    }
}
