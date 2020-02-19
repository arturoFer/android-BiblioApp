package org.afgl.biblioapp.utilities;

import android.util.Log;

import org.afgl.biblioapp.BuildConfig;

/**
 * Created by arturo on 28/06/2017.
 * Para que en la version release no se escriban logs
 */

public class LogUtils {
    public static void e(String tag, String message){
        if(BuildConfig.DEBUG){
            Log.e(tag, message);
        }
    }
}
