package org.afgl.biblioapp.estanteria.di;

import org.afgl.biblioapp.entities.Libro;
import org.afgl.biblioapp.estanteria.EstanteriaInteractor;
import org.afgl.biblioapp.estanteria.EstanteriaInteractorImpl;
import org.afgl.biblioapp.estanteria.EstanteriaPresenter;
import org.afgl.biblioapp.estanteria.EstanteriaPresenterImpl;
import org.afgl.biblioapp.estanteria.EstanteriaRepository;
import org.afgl.biblioapp.estanteria.EstanteriaRepositoryImpl;
import org.afgl.biblioapp.estanteria.ui.EstanteriaView;
import org.afgl.biblioapp.estanteria.ui.adapter.EstanteriaAdapter;
import org.afgl.biblioapp.estanteria.ui.adapter.OnItemClickListener;
import org.afgl.biblioapp.libs.base.Eventbus;
import org.afgl.biblioapp.libs.base.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by arturo on 08/06/2017.
 * Modulo Inyecion de dependencias de Estanteria
 */
@Module
public class EstanteriaModule {

    private EstanteriaView view;
    private OnItemClickListener clickListener;

    public EstanteriaModule(EstanteriaView view, OnItemClickListener clickListener){
        this.view = view;
        this.clickListener = clickListener;
    }

    @Provides
    @Singleton
    EstanteriaView providesEstanteriaView(){
        return this.view;
    }

    @Provides
    @Singleton
    OnItemClickListener providesClickLitener(){
        return this.clickListener;
    }

    @Provides
    @Singleton
    EstanteriaPresenter providesEstanteriaPresenter(Eventbus eventbus, EstanteriaView view, EstanteriaInteractor interactor){
        return new EstanteriaPresenterImpl(eventbus, view, interactor);
    }

    @Provides
    @Singleton
    EstanteriaInteractor providesEstanteriaInteractor(EstanteriaRepository repository){
        return  new EstanteriaInteractorImpl(repository);
    }

    @Provides
    @Singleton
    EstanteriaRepository providesEstanteriaRepository(Eventbus eventbus){
        return new EstanteriaRepositoryImpl(eventbus);
    }

    @Provides
    @Singleton
    EstanteriaAdapter providesEstanteriaAdapter(List<Libro> dataset, OnItemClickListener clickListener, ImageLoader imageloader){
        return new EstanteriaAdapter(dataset, clickListener, imageloader);
    }

    @Provides
    @Singleton
    List<Libro> providesEmptyList(){
        return new ArrayList<>();
    }
}
