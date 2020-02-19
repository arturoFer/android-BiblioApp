package org.afgl.biblioapp.domain;

import android.content.res.AssetManager;
import android.net.Uri;
import android.webkit.WebResourceResponse;

import org.afgl.biblioapp.entities.Capitulo;
import org.afgl.biblioapp.entities.Libro;
import org.afgl.biblioapp.libro.ui.LibroActivity;
import org.afgl.biblioapp.utilities.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by arturo on 08/06/2017.
 * Encapsulamiento clase libros Epub
 */

public class BookWrapper {

    private Book book;
    private String filename;

    private static final String LOG_TAG = "BookWrapper: ";

    public BookWrapper(String fileName, AssetManager assetManager){
        try{
            this.filename = fileName;
            InputStream epubInputStream = assetManager.open(fileName);
            book = (new EpubReader()).readEpub(epubInputStream);
            epubInputStream.close();
        } catch(IOException e){
            LogUtils.e(LOG_TAG, e.getMessage());
        }
    }

    public String getFilename(){
        return this.filename;
    }

    public Uri firstChapter(){
        List<SpineReference> spines = getBookSpine();
        return 0 < spines.size()
                ? resourceName2Url(spines.get(0).getResource().getHref())
                :null;
    }

    /*
     * @param resource el nombre del recurso a devolver
     * @return respuesta web con el recurso pedido
     */
    public WebResourceResponse fetch(String resource, boolean isNightMode){
        Resource response = book.getResources().getByHref(resource);
        WebResourceResponse webResponse = null;
        if(response == null){
            LogUtils.e(LOG_TAG, resource + " recurso no encontrado en epub");
        } else{
            InputStream inputStream;
            try{
                boolean cssAdded = false;
                inputStream = response.getInputStream();
                webResponse = new WebResourceResponse("", "UTF-8", null);
                if(resource.contains(".css") && isNightMode && !LibroActivity.getCssAdded()){
                    String css = "body{ color: #9E9E9E; background-color: #303030; } .chapterHeader{ background-color: #303030; border-color: #9E9E9E; } .chapterHeader .translation{ background-color: #303030; } .chapterHeader .count{ background-color: #303030; } a{ color: #9E9E9E; }";
                    List<InputStream> streams = Arrays.asList(inputStream, new ByteArrayInputStream(css.getBytes()));
                    InputStream story = new SequenceInputStream(Collections.enumeration(streams));
                    webResponse.setData(story);
                    cssAdded = true;
                    LibroActivity.setCssAdded();
                }
                if(!cssAdded) {
                    webResponse.setData(inputStream);
                }
                webResponse.setMimeType(response.getMediaType().toString());
                inputStream.close();
            } catch (IOException e){
                LogUtils.e(LOG_TAG, e.getMessage());
            }
        }
        return webResponse;
    }

    /*
     * @return URI del siguiente recurso en la secuencia o nulo si no lo hay
     */
    public Uri nextResource(Uri resourceUri){
        List<SpineReference> mySpine = getBookSpine();
        String resourceName = url2ResourceName(resourceUri);
        for(int i = 0; i < mySpine.size()-1; ++i){
            if(mySpine.get(i).getResource().getHref().equals(resourceName)){
                return resourceName2Url(mySpine.get(i+1).getResource().getHref());
            }
        }
        return null;
    }

    /*
     * @return URI del recurso anterior en la secuencia o nulo si no lo hay
     */
    public Uri previousResource(Uri resourceUri){
        List<SpineReference> mySpine = getBookSpine();
        String resourceName = url2ResourceName(resourceUri);
        for(int i = 1; i < mySpine.size(); ++i){
            if(mySpine.get(i).getResource().getHref().equals(resourceName)){
                return resourceName2Url(mySpine.get(i-1).getResource().getHref());
            }
        }
        return null;
    }

    /*
     * return la tabla de contenidos del libro
     */
    public void getTableOfContents(List<Capitulo> capitulos){
        List<TOCReference> tocReferences = book.getTableOfContents().getTocReferences();
        logTableOfContents(tocReferences, 0, capitulos);
    }
    private void logTableOfContents(List<TOCReference> tocReferences, int depth, List<Capitulo> capitulos){
        for(TOCReference tocReference : tocReferences){
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < depth; i++){
                stringBuilder.append("\t");
            }
            stringBuilder.append(tocReference.getTitle());
            String tittle = stringBuilder.toString();
            String location = tocReference.getCompleteHref();

            Capitulo capitulo = new Capitulo(tittle, location);
            capitulos.add(capitulo);
            logTableOfContents(tocReference.getChildren(), depth+1, capitulos);
        }
    }

    private List<SpineReference> getBookSpine(){
        return book.getSpine().getSpineReferences();
    }

    public InputStream getResourceByUri(Uri resourceUri){
        String resourceName = url2ResourceName(resourceUri);
        Resource resource = book.getResources().getByHref(resourceName);
        InputStream inputStream = null;
        if(resource == null){
            LogUtils.e(LOG_TAG, resourceName + " Recurso no encontrado");
        } else{
            try{
                inputStream = resource.getInputStream();
            }catch (IOException e){
                LogUtils.e(LOG_TAG, e.getMessage());
            }
        }
        return inputStream;
    }

    public static Libro getLibroFromPath(AssetManager assetManager, String path){
        Libro libro = new Libro();
        try {
            InputStream epubInputStream = assetManager.open("books/" + path);
            Book book = (new EpubReader()).readEpub(epubInputStream);
            libro.setTitulo(getTitulo(book));
            libro.setAutor(getAutores(book));
            byte[] portada = getPortada(book);
            if(portada != null) {
                libro.setPortada(book.getCoverImage().getData());
            }
            epubInputStream.close();

        } catch (IOException e){
            LogUtils.e(LOG_TAG, e.getMessage());
        }
        return libro;
    }

    private static String getTitulo(Book book){
        return book.getTitle();
    }

    private static String getAutores(Book book){
        List<Author> autores = book.getMetadata().getAuthors();
        String stringAutores = "";
        for(Author autor : autores){
            stringAutores = autor.getFirstname() + " " + autor.getLastname() + "\n";
        }
        if(stringAutores.length() > 0){
            stringAutores = stringAutores.substring(0, stringAutores.length()-1);
        }
        return stringAutores;
    }

    private static byte[] getPortada(Book book){
        byte[] portada = null;
        try {
            Resource coverImage = book.getCoverImage();
            if(coverImage != null){
                portada = coverImage.getData();
            } else{
                Resource otherCoverImage = book.getResources().getById("book-cover");
                if(otherCoverImage != null){
                    book.setCoverImage(otherCoverImage);
                    portada = book.getCoverImage().getData();
                }
            }
        } catch (IOException e){
            LogUtils.e(LOG_TAG, e.getMessage());
        }
        return portada;
    }

    /*
     * @param url usada por WebView
     * @return resourceName ruta usada por el archivo zip
     */
    public static String url2ResourceName(Uri url){
        // Solo nos interesa la parte path de la url
        String resourceName = url.getPath();

        // recortamos el primer caracter del principio '/'
        if (resourceName.charAt(0) == '/') {
            resourceName = resourceName.substring(1);
        }
        return resourceName;
    }

    /*
     * @param resourceName usado por el archivo zip
     * @return URL usada por el WebView
     */
    public static Uri resourceName2Url(String resourceName){
        // construye la ruta como un archivo local
        // empaqueta resourceName en la seccion path de un archivo URI
        // necesitamos dejar los caracteres '/' en la ruta, para que el WebView sea consciente
        // de la ruta al recurso, para que pueda resolver correctamente
        // la ruta de cualquier URL relativa contenida en el recurso
        return new Uri.Builder().scheme("file")
                .authority("")
                .appendEncodedPath(Uri.encode(resourceName, "/"))
                .build();
    }

}
