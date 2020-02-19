package org.afgl.biblioapp.estanteria;

import android.content.res.AssetManager;

/**
 * Created by arturo on 08/06/2017.
 * Interactuador implementacion
 */

public class EstanteriaInteractorImpl implements EstanteriaInteractor{

    private EstanteriaRepository repository;

    public EstanteriaInteractorImpl(EstanteriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getLibros(AssetManager assetManager) {
        repository.getLibros(assetManager);
    }
}
