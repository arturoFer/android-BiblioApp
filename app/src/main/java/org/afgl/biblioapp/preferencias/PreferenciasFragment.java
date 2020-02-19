package org.afgl.biblioapp.preferencias;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;

import org.afgl.biblioapp.R;

/**
 * Created by arturo on 15/06/2017.
 * Fragmento que carga nuestras preferencias desde nuestra plantilla de preferencias
 */

public class PreferenciasFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String nightModeKey = getResources().getString(R.string.nightmode_key);
        if(key.equals(nightModeKey)) {
            boolean modeCad = sharedPreferences.getBoolean(key, false);
            int mode;
            if(!modeCad){
                mode = AppCompatDelegate.MODE_NIGHT_NO;
            } else{
                mode = AppCompatDelegate.MODE_NIGHT_YES;
            }
            AppCompatDelegate.setDefaultNightMode(mode);
            getActivity().recreate();
        }
    }
}
