package org.afgl.biblioapp.about;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.afgl.biblioapp.R;
import org.afgl.biblioapp.libs.GlideImageLoader;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ImageView logo = findViewById(R.id.logo_image);
        GlideImageLoader loader = new GlideImageLoader(Glide.with(this));
        loader.load(logo, R.drawable.logo);
    }
}
