package org.afgl.biblioapp.capitulos.di;

import org.afgl.biblioapp.capitulos.ui.adapters.CapitulosAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by arturo on 10/06/2017.
 * Coomponenente de la inyeccion de dependencias
 */
@Singleton
@Component( modules = { CapitulosModule.class })
public interface CapitulosComponent {
    CapitulosAdapter getAdapter();
}
