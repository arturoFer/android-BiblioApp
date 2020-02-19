package org.afgl.biblioapp.libro.di;

import org.afgl.biblioapp.libro.LibroInteractor;
import org.afgl.biblioapp.libro.LibroInteractorImpl;
import org.afgl.biblioapp.libro.LibroPresenter;
import org.afgl.biblioapp.libro.LibroPresenterImpl;
import org.afgl.biblioapp.libro.LibroRepository;
import org.afgl.biblioapp.libro.LibroRepositoryImpl;
import org.afgl.biblioapp.libro.ui.LibroView;
import org.afgl.biblioapp.libs.base.Eventbus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by arturo on 09/06/2017.
 * Modulo libro para la inyeccion de dependencias
 */
@Module
public class LibroModule {

    private LibroView view;

    public LibroModule(LibroView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    LibroView providesLibroView(){
        return this.view;
    }


    @Provides
    @Singleton
    LibroPresenter providesLibroPresenter(Eventbus eventbus, LibroView view, LibroInteractor interactor){
        return new LibroPresenterImpl(eventbus, view, interactor);
    }

    @Provides
    @Singleton
    LibroInteractor providesLibroInteractor(LibroRepository repository){
        return new LibroInteractorImpl(repository);
    }

    @Provides
    @Singleton
    LibroRepository providesLibroRepository(Eventbus eventbus){
        return new LibroRepositoryImpl(eventbus);
    }
}
