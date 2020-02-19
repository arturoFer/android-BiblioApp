package org.afgl.biblioapp.entities;

/**
 * Created by arturo on 08/06/2017.
 * Clase que almacena el titulo, autor, portada y path a un determinado libro
 */

public class Libro {

    private String titulo;
    private String autor;
    private byte[] portada;
    private String location;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public byte[] getPortada() {
        return portada;
    }

    public void setPortada(byte[] portada) {
        this.portada = portada;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
