package org.afgl.biblioapp.libro.di;

import org.afgl.biblioapp.libro.LibroPresenter;
import org.afgl.biblioapp.libs.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by arturo on 09/06/2017.
 * componente libro inyeccion de dependencias
 */

@Singleton
@Component ( modules = {LibroModule.class, LibsModule.class})
public interface LibroComponent {
    LibroPresenter getPresenter();
}
