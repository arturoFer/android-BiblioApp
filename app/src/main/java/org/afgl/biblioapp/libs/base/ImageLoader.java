package org.afgl.biblioapp.libs.base;

import android.widget.ImageView;

/**
 * Created by arturo on 06/06/2017.
 * Interfaz para la envoltura de Glide
 */

public interface ImageLoader {
    void load(ImageView imageView, byte[] array);
    void load(ImageView imageView, int res);
}
