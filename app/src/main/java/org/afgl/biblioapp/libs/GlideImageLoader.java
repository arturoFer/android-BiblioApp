package org.afgl.biblioapp.libs;

import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.afgl.biblioapp.libs.base.ImageLoader;

/**
 * Created by arturo on 06/06/2017.
 * Envoltura de Glide
 */

public class GlideImageLoader implements ImageLoader{
    private RequestManager requestManager;

    public GlideImageLoader(RequestManager requestManager){
        this.requestManager = requestManager;
    }

    @Override
    public void load(ImageView imageView, byte[] imageByteArray) {
        requestManager
                .load(imageByteArray)
                .asBitmap()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    @Override
    public void load(ImageView imageView, int res) {
        requestManager
                .load(res)
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

}
