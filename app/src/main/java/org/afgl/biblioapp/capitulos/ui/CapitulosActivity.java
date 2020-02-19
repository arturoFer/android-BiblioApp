package org.afgl.biblioapp.capitulos.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.afgl.biblioapp.BiblioApp;
import org.afgl.biblioapp.R;
import org.afgl.biblioapp.capitulos.di.CapitulosComponent;
import org.afgl.biblioapp.capitulos.ui.adapters.CapitulosAdapter;
import org.afgl.biblioapp.capitulos.ui.adapters.OnCapituloClickListener;
import org.afgl.biblioapp.entities.Capitulo;
import org.afgl.biblioapp.libro.ui.LibroActivity;

import java.util.List;

public class CapitulosActivity extends AppCompatActivity implements OnCapituloClickListener {

    public static final String CHAPTERS_EXTRA = "CHAPTERS_EXTRA";
    public static final String CHAPTER_EXTRA = "CHAPTER_EXTRA";

    private RecyclerView recyclerView;
    private CapitulosAdapter adapter;

    List<Capitulo> capitulos;

    String currentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capitulos);

        Intent intent = getIntent();
        capitulos = intent.getParcelableArrayListExtra(CHAPTERS_EXTRA);
        currentPath = intent.getStringExtra(LibroActivity.PATH_EXTRA);


        recyclerView = findViewById(R.id.recyclerviewCap);
        setupInjection();
        setupRecyclerView();

        adapter.setCapitulos(capitulos);

    }

    private void setupInjection(){
        BiblioApp app = (BiblioApp) getApplication();
        CapitulosComponent component = app.getCapitulosComponent(this);
        adapter = component.getAdapter();
    }

    private void setupRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Capitulo capitulo) {
        //Uri location = capitulo.getContentWhithoutTag();
        Uri location = capitulo.getUri();

        Intent intent = new Intent();

        intent.putExtra(LibroActivity.PATH_EXTRA, currentPath);
        intent.putExtra(CHAPTER_EXTRA, location);

        setResult(RESULT_OK, intent);
        finish();
    }
}
