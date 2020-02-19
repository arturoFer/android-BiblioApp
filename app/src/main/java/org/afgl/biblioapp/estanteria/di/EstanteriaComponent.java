package org.afgl.biblioapp.estanteria.di;

import org.afgl.biblioapp.estanteria.EstanteriaPresenter;
import org.afgl.biblioapp.estanteria.ui.adapter.EstanteriaAdapter;
import org.afgl.biblioapp.libs.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by arturo on 08/06/2017.
 * Inyeccion de dependencias
 */
@Singleton
@Component( modules = {EstanteriaModule.class, LibsModule.class})
public interface EstanteriaComponent {
    EstanteriaAdapter getAdapter();
    EstanteriaPresenter getPresenter();
}
