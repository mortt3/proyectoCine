/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modelo;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author jorge
 */
public class Pelicula implements Serializable {
    private String idPeli;
    private String titulo;
    private String genero;
    private Director director;
    private List<Actor> actores;

    public Pelicula(String idPeli, String titulo, String genero, Director director, List<Actor> actores) {
        this.idPeli = idPeli;
        this.titulo = titulo;
        this.genero = genero;
        this.director = director;
        this.actores = actores;
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

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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
    
}
