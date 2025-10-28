/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modelo;

import com.mycompany.excepciones.MyException;
import java.io.Serializable;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge
 */
@XmlRootElement(name = "pelicula")
@XmlAccessorType(XmlAccessType.FIELD)
public class Pelicula implements Serializable {

    private String idPeli, titulo, genero;
    private Director director;
    private List<Actor> actores;

    public Pelicula(String idPeli, String titulo, String genero, Director director, List<Actor> actores) throws MyException {
        this.idPeli = idPeli;
        this.titulo = validarTitulo(titulo);
        this.genero = genero;
        this.director = director;
        this.actores = actores;
    }

    public Pelicula() {
    }

    private String validarTitulo(String titulo) throws MyException {
        if (!titulo.matches("[a-zA-Z0-9ñáéíóúÁÉÍÓÚ:/\\\"-_ç() ]{1,100}")) {
            throw new MyException("el titulo introducido es incorrecto");
        }
        return titulo;
    }

    public String getIdPeli() {
        return idPeli;
    }

    public void setIdPeli(String idPeli) {
        this.idPeli = idPeli;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) throws MyException {
        this.titulo = validarTitulo(titulo);
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public List<Actor> getActores() {
        return actores;
    }

    public void setActores(List<Actor> actores) {
        this.actores = actores;
    }

    @Override
    public String toString() {
        return idPeli + " - " + titulo + " (" + genero + ") | Director: "
                + director.getNombre() + " " + director.getApellido()
                + " | Actores: " + actores;
    }

    // compara dos películas por su ID.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pelicula other = (Pelicula) obj;
        return idPeli != null && idPeli.equalsIgnoreCase(other.idPeli);
    }

    @Override
    public int hashCode() {
        return idPeli != null ? idPeli.toLowerCase().hashCode() : 0;
    }

}
