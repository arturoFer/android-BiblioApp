package org.afgl.biblioapp.libro.ui;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.afgl.biblioapp.BiblioApp;
import org.afgl.biblioapp.R;
import org.afgl.biblioapp.about.AboutActivity;
import org.afgl.biblioapp.capitulos.ui.CapitulosActivity;
import org.afgl.biblioapp.diccionario.DiccionarioActivity;
import org.afgl.biblioapp.libs.GlideImageLoader;
import org.afgl.biblioapp.utilities.Bookmark;
import org.afgl.biblioapp.entities.Capitulo;
import org.afgl.biblioapp.libro.LibroPresenter;
import org.afgl.biblioapp.libro.di.LibroComponent;
import org.afgl.biblioapp.preferencias.PreferenciasActivity;

import java.util.ArrayList;
import java.util.List;

public class LibroActivity extends AppCompatActivity implements LibroView, MyGestureListener {

    public final static String PATH_EXTRA = "PATH_EXTRA";
    private final static int LIST_CHAPTER_ACTIVITY_ID = 0;

    private NestedWebView webView;
    private LibroPresenter presenter;

    private String currentPath;
    private Uri mCurrentResourceUri;

    private float scrollY = 0.0f;
    private Uri firstChapter;
    private boolean wasOnCreated;
    private boolean isSpeaking = false;

    private DrawerLayout drawerLayout;
    private int optionDrawerSelected = -1000;

    private float velocity;
    private boolean highLight;
    private int textSize = 100;
    private int nightMode;
    private boolean isNightMode;

    private static boolean cssAdded  = false;

    private boolean isOpenCab = false;
    private boolean isDiccionarioPressed = false;

    private boolean isGoBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro);

        wasOnCreated = true;

        Intent intent = getIntent();
        currentPath = intent.getStringExtra(PATH_EXTRA);

        webView = findViewById(R.id.webview);

        setupNavigation();
        setupToolbar();
        setupGestureDetection();
        setupInjection();
        setupWebView();

        nightMode = getNightMode();
        isNightMode = isNightMode(nightMode);

        if(savedInstanceState != null){
            // La orientacion de pantalla ha cambiado, se ha recargado la pagina
            gotoBookmark(new Bookmark(savedInstanceState)) ;
        }

        presenter.initTts(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();

        if(!wasOnCreated){
            wasOnCreated = true;
            return;
        }

        AssetManager assetManager = getAssets();
        presenter.setBook(currentPath, assetManager);

        if(mCurrentResourceUri == null){
            changeChapter(firstChapter);
        } else {
            changeChapter(mCurrentResourceUri);
        }
    }

    @Override
    protected void onPause() {
        presenter.stopSpeak();
        presenter.onPause();
        super.onPause();
        wasOnCreated = false;
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPostResume() {
        if(isSpeaking){
            isSpeaking = false;
            invalidateOptionsMenu();
        }
        getSettings();
        changeVisibilityIconsMenu();
        super.onPostResume();
    }

    private void getSettings(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String velocityCad = sharedPreferences.getString(getResources().getString(R.string.velocity_key),"1");
        switch (velocityCad){
            case "0":
                velocity = 1.1f;
                break;
            case "1":
                velocity = 1.0f;
                break;
            case "2":
                velocity = 0.8f;
                break;
        }

        highLight = sharedPreferences.getBoolean(getResources().getString(R.string.highlight_key),false);
        
        String textSizeCad = sharedPreferences.getString(getResources().getString(R.string.textsize_key), "1");
        int currentTextSize = 100;
        switch (textSizeCad){
            case "0":
                currentTextSize = 75;
                break;
            case "1":
                currentTextSize = 100;
                break;
            case "2":
                currentTextSize = 150;
                break;
        }

        if(textSize != currentTextSize){
            textSize = currentTextSize;
            WebSettings settings = webView.getSettings();
            settings.setTextZoom(textSize);
        }

        int currentNightMode = getNightMode();
        if(currentNightMode != nightMode){
            nightMode = currentNightMode;
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        recreate();
                    }
                });
            } else{
                recreate();
            }
        }
    }

    private void setupNavigation(){
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        NavigationView navView = findViewById(R.id.navview);

        View header = navView.getHeaderView(0);
        ImageView imageView = header.findViewById(R.id.libro_image);
        GlideImageLoader loader = new GlideImageLoader(Glide.with(this));
        loader.load(imageView, R.drawable.libro);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                optionDrawerSelected = item.getItemId();
                drawerLayout.closeDrawers();
                return true;
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                switch (optionDrawerSelected){
                    case R.id.menu_next:
                        onNext();
                        break;
                    case R.id.menu_prev:
                        onPrevious();
                        break;
                    case R.id.menu_books:
                        launchEstanteria();
                        break;
                    case R.id.menu_chapters:
                        getIndex();
                        break;
                    case R.id.menu_add_bookmark:
                        addBookmark();
                        break;
                    case R.id.menu_goto_bookmark:
                        gotoHardBookmark();
                        break;
                    case R.id.menu_clear_bookmark:
                        clearBookmark();
                        break;
                    case R.id.menu_settings:
                        launchSettings();
                        break;
                }
                optionDrawerSelected = -1000;
            }
        });
    }

    private void setupToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbarLibro);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupGestureDetection(){
        final GestureDetector gestureDetector = new GestureDetector(this, new MyGestureDetector(this));
        View.OnTouchListener gestureOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        };
        webView.setOnTouchListener(gestureOnTouchListener);
    }

    private void setupInjection(){
        BiblioApp app = (BiblioApp) getApplication();
        LibroComponent component = app.getLibroComponent(this);
        presenter = component.getPresenter();
    }

    private void setupWebView(){
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            webView.setWebViewClient(createWebViewClient24());
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.setWebViewClient(createWebViewClient21());
        } else{
            webView.setWebViewClient(createWebViewClient16());
        }

        webView.setWebChromeClient(new WebChromeClient());
    }

    @TargetApi(Build.VERSION_CODES.N)
    private WebViewClient createWebViewClient24(){
        return new WebViewClient(){
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Uri resourceUri = request.getUrl();
                return onRequest(resourceUri);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                onPageLoaded(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                url = Uri.decode(url);
                if(url.contains("file:///")){
                    if(url.contains("#")){
                        mCurrentResourceUri = getContentWhithoutTag(url);
                    } else{
                        mCurrentResourceUri = Uri.parse(url);
                    }
                    return false;
                } else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }
        };
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private WebViewClient createWebViewClient21(){
        return new WebViewClient(){
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Uri resourceUri = request.getUrl();
                return onRequest(resourceUri);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                onPageLoaded(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                url = Uri.decode(url);
                if(url.contains("file:///")){
                    if(url.contains("#")){
                        mCurrentResourceUri = getContentWhithoutTag(url);
                    } else{
                        mCurrentResourceUri = Uri.parse(url);
                    }
                    return false;
                } else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }
        };
    }

    @SuppressWarnings("deprecation")
    private WebViewClient createWebViewClient16(){
        return new WebViewClient(){
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Uri resourceUri = Uri.parse(url);
                return onRequest(resourceUri);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                onPageLoaded(url);
            }

           @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                url = Uri.decode(url);
                if(url.contains("file:///")){
                    if(url.contains("#")){
                        mCurrentResourceUri = getContentWhithoutTag(url);
                    } else{
                        mCurrentResourceUri = Uri.parse(url);
                    }
                    return false;
                } else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }
        };
    }

    private WebResourceResponse onRequest(Uri resourceUri){
        return presenter.fetch(resourceUri, isNightMode);
    }

    private void loadUri(Uri uri) {
        mCurrentResourceUri = uri;
        // previene el cache pues WebSettings.LOAD_NO_CACHE no siempre funciona
        webView.clearCache(false);
        // Si la url contiene el caracter %23 la decodificamos para convertirlo en #
        String url = Uri.decode(mCurrentResourceUri.toString());
        // carga animacion y al terminar esta la nueva url;
        if(!url.contains("#")) {
            loadFadeOutAnimation(url);
        } else{
            webView.loadUrl(url);
            mCurrentResourceUri = getContentWhithoutTag(url);
        }
    }

    private Uri getContentWhithoutTag(String url){
        int indexOf = url.indexOf('#');
        String temp = url;
        if(0 < indexOf){
            temp = url.substring(0, indexOf);
        }
        return Uri.parse(temp);
    }

    private void loadFadeOutAnimation(final String url){
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                View fadeView = findViewById(R.id.fadeView);
                webView.setVisibility(View.GONE);
                fadeView.setVisibility(View.VISIBLE);
                webView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl(url);
                    }
                }, 100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        webView.startAnimation(fadeOut);
    }

    @Override
    public void firstChapter(Uri uri) {
        firstChapter = uri;
    }

    @Override
    public void showError(String error) {
        String messageError = String.format(getString(R.string.libro_error_read), error);
        Snackbar.make(findViewById(android.R.id.content), messageError, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void changeChapter(Uri resourceUri) {
        loadUri(resourceUri);
    }

    @Override
    public void launchChaptersList(List<Capitulo> capitulos) {
        Intent intent = new Intent(this, CapitulosActivity.class);

        intent.putExtra(PATH_EXTRA, currentPath);

        intent.putExtra(CapitulosActivity.CHAPTERS_EXTRA, (ArrayList<Capitulo>)capitulos);
        startActivityForResult(intent, LIST_CHAPTER_ACTIVITY_ID );
    }

    @Override
    public void endSpeak() {
        stopSpeak();
        showError(getResources().getString(R.string.is_speaking_end));
    }

    @Override
    public void highLightSpeak(final String speak) {
       webView.post(new Runnable() {
           @Override
           public void run() {
               webView.clearMatches();
               webView.findAllAsync(speak);
           }
       });
    }

    @Override
    public void onNext() {
        if(!isSpeaking) {
            presenter.nextChapter(mCurrentResourceUri);
        } else{
            showError(getResources().getString(R.string.is_speaking_error));
        }
    }

    @Override
    public void onPrevious() {
        if(!isSpeaking) {
            presenter.previousChapter(mCurrentResourceUri);
        } else{
            showError(getResources().getString(R.string.is_speaking_error));
        }
    }

    @Override
    public boolean pageUpDown(float y) {
        AppBarLayout appBar = findViewById(R.id.appbar);
        int top = appBar.getTop();
        int appbarHeight = appBar.getHeight();
        int offset = appbarHeight + top;

        if(offset != 0){
            appBar.setExpanded(false, true);
            return true;
        }

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2 ) {
            return nativeScroll(y);
        }else{
            return myScroll(y);
        }
    }

    private boolean nativeScroll(float y){
        int height = webView.getMeasuredHeight();
        if (y <= height / 5) {
            if (!webView.pageUp(false)) {
                onPrevious();
            }
            return true;
        } else if (y >= 4 * height / 5) {
            if (!webView.pageDown(false)) {
                onNext();
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean myScroll(float y){
        int height = webView.getMeasuredHeight();
        int inicio = webView.getScrollY();
        int fin;
        if(y <= height / 5){
            if(inicio == 0){
                onPrevious();
            } else{
                fin = (int)(inicio - 0.95*height);
                if(fin <= 0){
                    fin = 0;
                }
                animateScroll(inicio, fin);
            }
            return true;
        }else if(y >= 4 * height / 5){
            int contentHeight = (int) Math.floor(webView.getContentHeight()*getResources().getDisplayMetrics().density);
            int diff = contentHeight - (inicio + height);
            if(diff == 0){
                onNext();
            } else{
                fin = (int)(inicio + 0.95*height);
                if(fin >= contentHeight - height){
                    fin = contentHeight - height;
                }
                animateScroll(inicio, fin);
            }
            return true;
        }else{
            return false;
        }
    }

    private void animateScroll(int inicio, int fin){
        ObjectAnimator anim = ObjectAnimator.ofInt(webView, "scrollY",inicio, fin);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(400);
        anim.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_libro_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_speak:
                startSpeak(mCurrentResourceUri);
                return true;
            case R.id.menu_stop_speak:
                stopSpeak();
                return true;
            case R.id.menu_settings:
                launchSettings();
                return true;
            case R.id.menu_about:
                launchAbout();
                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem speak = menu.findItem(R.id.menu_speak);
        MenuItem stopSpeak = menu.findItem(R.id.menu_stop_speak);
        if(isSpeaking){
            speak.setVisible(false);
            stopSpeak.setVisible(true);
        } else{
            speak.setVisible(true);
            stopSpeak.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void launchAbout(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void launchEstanteria(){
        finish();
    }

    private void getIndex(){
        presenter.getIndex();
    }

    private void launchSettings(){
        Intent intent = new Intent(this, PreferenciasActivity.class);
        startActivity(intent);
    }

    private void startSpeak(Uri resourceUri){
        isSpeaking = true;
        invalidateOptionsMenu();
        presenter.startSpeak(resourceUri, velocity, highLight);
    }

    private void stopSpeak(){
        isSpeaking = false;
        invalidateOptionsMenu();
        presenter.stopSpeak();
        if(highLight) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.clearMatches();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LIST_CHAPTER_ACTIVITY_ID){
            if(resultCode == RESULT_OK) {
                currentPath = data.getStringExtra(PATH_EXTRA);
                mCurrentResourceUri = data.getParcelableExtra(CapitulosActivity.CHAPTER_EXTRA);
                wasOnCreated = true;
            }
        }
    }

    private void onPageLoaded(String url){
        if(isGoBack){
            mCurrentResourceUri = Uri.parse(webView.getUrl());
            isGoBack = false;
            return;
        }
        cssAdded = false;
        if(!url.contains("#")){
            View fadeView = findViewById(R.id.fadeView);
            fadeView.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            loadFadeInAnimation();
        }
    }

    private void calculaAlturaTotal(){
        int alturaTotal = webView.getContentHeight();
        webView.scrollTo(0, (int) (scrollY * alturaTotal));
        //animateScroll(0, (int)(scrollY * alturaTotal));
        scrollY = 0.0f;
    }

    private void loadFadeInAnimation(){
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                calculaAlturaTotal();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        webView.startAnimation(fadeIn);
    }

    private void gotoBookmark(Bookmark bookmark){
        if(!bookmark.isEmpty()){
            scrollY = bookmark.getScrollY();
            currentPath = bookmark.getFileName();
            mCurrentResourceUri = bookmark.getmUri();
        }
    }

    private Bookmark getBookmark(){
        if(mCurrentResourceUri != null){
            float contentHeight = (float) webView.getContentHeight();
            contentHeight = (contentHeight == 0) ? 0.0f : webView.getScrollY()/contentHeight;
            return new Bookmark(currentPath, mCurrentResourceUri, contentHeight);
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bookmark bookmark = getBookmark();
        if(bookmark != null) {
            bookmark.save(outState);
        }
    }

    private void addBookmark(){
        Bookmark bookmark = getBookmark();
        if(bookmark != null){
            bookmark.saveToSharedPreferences(this);
            NavigationView navview = findViewById(R.id.navview);
            navview.getMenu().findItem(R.id.menu_goto_bookmark).setVisible(true);
            navview.getMenu().findItem(R.id.menu_clear_bookmark).setVisible(true);
            showError(getResources().getString(R.string.success_bookmark));
        }
    }

    private void gotoHardBookmark(){
        Bookmark bookmark = new Bookmark(this, currentPath);

        if(!bookmark.isEmpty()){
            if(!currentPath.equals(bookmark.getFileName())){
                return;
            }
            mCurrentResourceUri = bookmark.getmUri();
            scrollY = bookmark.getScrollY();
            changeChapter(mCurrentResourceUri);
        }
    }

    private void clearBookmark(){
        Bookmark.clearBookmarkFromPreferences(this, currentPath);
        NavigationView navview = findViewById(R.id.navview);
        navview.getMenu().findItem(R.id.menu_goto_bookmark).setVisible(false);
        navview.getMenu().findItem(R.id.menu_clear_bookmark).setVisible(false);
        showError(getResources().getString(R.string.success_delete_bookmark));
    }

    private void changeVisibilityIconsMenu(){
        NavigationView navview = findViewById(R.id.navview);
        if(Bookmark.checkKey(this, currentPath)){
            navview.getMenu().findItem(R.id.menu_goto_bookmark).setVisible(true);
            navview.getMenu().findItem(R.id.menu_clear_bookmark).setVisible(true);
        } else{
            navview.getMenu().findItem(R.id.menu_goto_bookmark).setVisible(false);
            navview.getMenu().findItem(R.id.menu_clear_bookmark).setVisible(false);
        }
    }

    private int getNightMode(){
        return AppCompatDelegate.getDefaultNightMode();
    }

    private boolean isNightMode(int nightMode){
        return nightMode == AppCompatDelegate.MODE_NIGHT_YES;
    }

    static public boolean getCssAdded(){
        return  cssAdded;
    }

    static public void setCssAdded(){
        cssAdded = true;
    }

    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (!isOpenCab) {
                addDictionaryMenuItem(mode);
                isOpenCab = true;
            }
        }
        super.onSupportActionModeStarted(mode);
    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            if(isOpenCab) {
                if(isDiccionarioPressed) {
                    webView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String text = getClipboardText();
                            if(text != null) {
                                launchDictionary(text);
                            }
                        }
                    }, 200);
                    isDiccionarioPressed = false;
                }
                isOpenCab = false;
            }
        }
    }

    private MenuItem getCopyMenuItem(Menu menu){
        MenuItem copyItem = null;
        for(int i = 0; i < menu.size(); i++){
            String resourceName = getResources().getResourceName(menu.getItem(i).getItemId());
            if (resourceName.contains("copiar") || resourceName.contains("copy")) {
                copyItem = menu.getItem(i);
                break;
            }
        }
        return copyItem;
    }

    private void addDictionaryMenuItem(final ActionMode mode){
        final Menu menu = mode.getMenu();
        final MenuItem copyItem = getCopyMenuItem(menu);
        if(copyItem != null) {
            MenuItem diccionarioItem = menu.add(R.string.label_diccionario);
            diccionarioItem.setIcon(R.drawable.ic_school_white_24dp);
            //MenuItemCompat.setShowAsAction(diccionarioItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
            diccionarioItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            diccionarioItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    isDiccionarioPressed = true;
                    menu.performIdentifierAction(copyItem.getItemId(), 0);
                    mode.finish();
                    return true;
                }
            });
        }
    }

    private String getClipboardText(){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if(clipboard != null) {
            return clipboard.getPrimaryClip().getItemAt(0).coerceToText(this).toString();
        } else{
            showError(getResources().getString(R.string.clipboard_error));
            return null;
        }
    }

    private void launchDictionary(String text){
        Intent intent = new Intent(this, DiccionarioActivity.class);
        intent.putExtra(DiccionarioActivity.WORD_EXTRA, text);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String url = webView.getUrl();
        boolean isFootnotes = url.contains("footnotes.xml");
        boolean isBackButton = (keyCode == KeyEvent.KEYCODE_BACK);
        if(isFootnotes && isBackButton && webView.canGoBack()){
            isGoBack = true;
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}