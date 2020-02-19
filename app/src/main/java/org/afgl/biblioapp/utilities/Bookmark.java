package org.afgl.biblioapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by arturo on 11/06/2017.
 * Definicion de la clase que gestiona los bookmarks que utilizamos al cambiar la orientacion
 * de pantalla
 */

public class Bookmark {

    private static final String PREFS_NAME = "PREFS_FILE";
    private static final String PREFS_EPUB_NAME = "FileName";
    private static final String PREFS_RESOURCE_URI = "ResourceUri";
    private static final String PREFS_SCROLLY = "ScrollY";

    private String fileName;
    private Uri mUri;
    private float scrollY;

    public Bookmark(String fileName, Uri resourceUri, float scrollY) {
        this.fileName = fileName;
        this.mUri = resourceUri;
        this.scrollY = scrollY;
    }

    //Llamado cuando se cambia la orientacion de la pantalla
    public Bookmark(Bundle savedInstanceState){
        fileName = savedInstanceState.getString(PREFS_EPUB_NAME);
        deserializeUri(savedInstanceState.getString(PREFS_RESOURCE_URI));
        scrollY = savedInstanceState.getFloat(PREFS_SCROLLY);
    }

    // Llamado cuando creamos una marca
    public Bookmark(Context context, String bookName){
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String bookmark = preferences.getString(bookName, "");
        deserializeBoookmark(bookmark);
    }

    private void deserializeBoookmark(String bookmark){
        String parts[] = bookmark.split(";");
        fileName = parts[0];
        deserializeUri(parts[1]);
        scrollY = Float.parseFloat(parts[2]);
    }

    private void deserializeUri(String uri){
        if((uri != null) && (!uri.isEmpty())){
            mUri = Uri.parse(uri);
        }
    }

    //devuelve true si bookmark esta vacio
    public boolean isEmpty(){
        return ((fileName == null) || (fileName.length() <= 0) || (mUri == null));
    }

    //Guarda el bookmark en un bundle cuando cambiamos la orientacion de pantalla
    public void save(Bundle outstate){
        if(!isEmpty()){
            outstate.putString(PREFS_EPUB_NAME, fileName);
            outstate.putString(PREFS_RESOURCE_URI, mUri.toString());
            outstate.putFloat(PREFS_SCROLLY, scrollY);
        }
    }

    public void saveToSharedPreferences(Context context){
        if(!isEmpty()){
            SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String bookmark = serializeBookmark();
            editor.putString(fileName, bookmark);
            editor.apply();
        }
    }

    private String serializeBookmark(){
        return fileName + ";" + mUri.toString() + ";" + Float.toString(scrollY);
    }

    public String getFileName(){
        return fileName;
    }

    public Uri getmUri(){
        return mUri;
    }

    public float getScrollY(){
        return scrollY;
    }

    static public void clearBookmarkFromPreferences(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(preferences.contains(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    static public boolean checkKey(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.contains(key);
    }
}