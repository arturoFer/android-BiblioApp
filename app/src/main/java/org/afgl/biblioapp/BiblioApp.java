package org.afgl.biblioapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import org.afgl.biblioapp.capitulos.di.CapitulosComponent;
import org.afgl.biblioapp.capitulos.di.CapitulosModule;
import org.afgl.biblioapp.capitulos.di.DaggerCapitulosComponent;
import org.afgl.biblioapp.capitulos.ui.adapters.OnCapituloClickListener;
import org.afgl.biblioapp.estanteria.di.DaggerEstanteriaComponent;
import org.afgl.biblioapp.estanteria.di.EstanteriaComponent;
import org.afgl.biblioapp.estanteria.di.EstanteriaModule;
import org.afgl.biblioapp.estanteria.ui.EstanteriaActivity;
import org.afgl.biblioapp.estanteria.ui.EstanteriaView;
import org.afgl.biblioapp.estanteria.ui.adapter.OnItemClickListener;
import org.afgl.biblioapp.libro.di.DaggerLibroComponent;
import org.afgl.biblioapp.libro.di.LibroComponent;
import org.afgl.biblioapp.libro.di.LibroModule;
import org.afgl.biblioapp.libro.ui.LibroView;
import org.afgl.biblioapp.libs.di.LibsModule;

/**
 * Created by arturo on 08/06/2017.
 * Clase apliacion para la inyeccion de dependencias
 */

public class BiblioApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initDayNightMode();
    }

    public EstanteriaComponent getEstanteriaComponent(EstanteriaActivity activity, EstanteriaView view, OnItemClickListener clickListener){
        return DaggerEstanteriaComponent
                .builder()
                .libsModule(new LibsModule(activity))
                .estanteriaModule(new EstanteriaModule(view, clickListener))
                .build();
    }

    public LibroComponent getLibroComponent(LibroView view){
        return DaggerLibroComponent
                .builder()
                .libsModule(new LibsModule())
                .libroModule(new LibroModule(view))
                .build();
    }

    public CapitulosComponent getCapitulosComponent(OnCapituloClickListener listener){
        return DaggerCapitulosComponent
                .builder()
                .capitulosModule(new CapitulosModule(listener))
                .build();
    }

    private void initDayNightMode(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean nightMode = sharedPreferences.getBoolean(getResources().getString(R.string.nightmode_key), false);
        int mode;
        if(!nightMode){
            mode = AppCompatDelegate.MODE_NIGHT_NO;
        } else{
            mode = AppCompatDelegate.MODE_NIGHT_YES;
        }
        AppCompatDelegate.setDefaultNightMode(mode);
    }
}
