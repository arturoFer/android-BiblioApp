package org.afgl.biblioapp.estanteria;

import android.content.res.AssetManager;

import org.afgl.biblioapp.estanteria.events.EstanteriaEvent;
import org.afgl.biblioapp.estanteria.ui.EstanteriaView;
import org.afgl.biblioapp.libs.base.Eventbus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by arturo on 08/06/2017.
 * Presentador
 */

public class EstanteriaPresenterImpl implements EstanteriaPresenter{

    private Eventbus eventbus;
    private EstanteriaView view;
    private EstanteriaInteractor interactor;

    public EstanteriaPresenterImpl(Eventbus eventbus, EstanteriaView view, EstanteriaInteractor interactor) {
        this.eventbus = eventbus;
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onResume() {
        eventbus.register(this);
    }

    @Override
    public void onPause() {
        eventbus.unregister(this);
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void getListBooks(AssetManager assetManager) {
        interactor.getLibros(assetManager);
    }

    @Override
    @Subscribe
    public void onEventMainThread(EstanteriaEvent event) {
        String error = event.getError();
        switch (event.getType()){
            case EstanteriaEvent.READ_EVENT:
                if(error == null){
                    view.setBooks(event.getLibros());
                } else{
                    view.showError(error);
                }
                break;
        }
    }
}
