package org.afgl.biblioapp.capitulos.di;

import org.afgl.biblioapp.capitulos.ui.adapters.CapitulosAdapter;
import org.afgl.biblioapp.capitulos.ui.adapters.OnCapituloClickListener;
import org.afgl.biblioapp.entities.Capitulo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by arturo on 10/06/2017.
 * Modulo para la inyeccion de dependencias de la actividad capitulos
 */

@Module
public class CapitulosModule {

    private OnCapituloClickListener listener;

    public CapitulosModule(OnCapituloClickListener listener){
        this.listener = listener;
    }

    @Provides
    @Singleton
    OnCapituloClickListener providesOnCapituloClickListener(){
        return this.listener;
    }

    @Provides
    @Singleton
    CapitulosAdapter providesCapitulosAdapter(List<Capitulo> dataset, OnCapituloClickListener listener){
        return new CapitulosAdapter(dataset, listener);
    }

    @Provides
    @Singleton
    List<Capitulo> providesEmptyList(){
        return new ArrayList<>();
    }
}
