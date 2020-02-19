package org.afgl.biblioapp.estanteria.ui;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.afgl.biblioapp.BiblioApp;
import org.afgl.biblioapp.R;
import org.afgl.biblioapp.about.AboutActivity;
import org.afgl.biblioapp.entities.Libro;
import org.afgl.biblioapp.estanteria.EstanteriaPresenter;
import org.afgl.biblioapp.estanteria.di.EstanteriaComponent;
import org.afgl.biblioapp.estanteria.ui.adapter.EstanteriaAdapter;
import org.afgl.biblioapp.estanteria.ui.adapter.OnItemClickListener;
import org.afgl.biblioapp.libro.ui.LibroActivity;
import org.afgl.biblioapp.libs.base.ImageLoader;

import java.util.List;

public class EstanteriaActivity extends AppCompatActivity implements EstanteriaView, OnItemClickListener {

    private EstanteriaAdapter adapter;
    private RecyclerView recyclerView;
    private EstanteriaPresenter presenter;

    private int nightMode;

    private boolean created = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estanteria);

        recyclerView = findViewById(R.id.recyclerview);

        nightMode = getNightMode();

        setupToolbar();
        setupInjection();
        setupRecyclerView();
        loadImageCollapsingToolBar();

    }

    private void setupToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout ctlLayout = findViewById(R.id.collapsingToolbar);
        ctlLayout.setTitle(getResources().getString(R.string.menu_estanteria));
    }

    private void setupInjection() {
        BiblioApp app = (BiblioApp) getApplication();
        EstanteriaComponent component = app.getEstanteriaComponent(this, this, this);
        presenter = component.getPresenter();
        adapter = component.getAdapter();
    }


    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.number_columns)));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void loadImageCollapsingToolBar(){
        ImageView imageView = findViewById(R.id.estanteria_image);
        ImageLoader loader = adapter.getImageLoader();
        loader.load(imageView, R.drawable.estanteria);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_estanteria_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_about:
                launchAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchAbout(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.onResume();

        if(!created) {
            AssetManager assetManager = getAssets();
            presenter.getListBooks(assetManager);
            created = true;
        }

        int currentNightMode = getNightMode();
        if(currentNightMode != nightMode){
            nightMode = currentNightMode;
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        recreate();
                    }
                };
                handler.post(runnable);
            } else{
                recreate();
            }
        }
    }

    @Override
    protected void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setBooks(List<Libro> libros) {
        adapter.setBooks(libros);
    }

    @Override
    public void showBook(Libro libro) {
        Intent intent = new Intent(this, LibroActivity.class);
        intent.putExtra(LibroActivity.PATH_EXTRA, libro.getLocation());
        startActivity(intent);
    }

    @Override
    public void showError(String error) {
        String messageError = String.format(getString(R.string.estanteria_error_read), error);
        Snackbar.make(findViewById(android.R.id.content), messageError, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void OnItemClick(Libro libro) {
        showBook(libro);
    }

    private int getNightMode(){
        return AppCompatDelegate.getDefaultNightMode();
    }
}
