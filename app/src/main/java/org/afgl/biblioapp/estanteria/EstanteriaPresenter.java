package org.afgl.biblioapp.estanteria;

import android.content.res.AssetManager;

import org.afgl.biblioapp.estanteria.events.EstanteriaEvent;

/**
 * Created by arturo on 08/06/2017.
 * Presentador de la Actividad Estantería
 */

public interface EstanteriaPresenter {
    void onResume();
    void onPause();
    void onDestroy();

    void getListBooks(AssetManager assetManager);
    @SuppressWarnings("unused")
    void onEventMainThread(EstanteriaEvent event);
}
