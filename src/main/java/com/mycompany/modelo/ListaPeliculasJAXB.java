/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modelo;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Para importar y exporar el JAXB ya que necesita 1 objeto y mi clase peliculas pedia tambien e ldirector y actor
 * @author Alumno
 */
@XmlRootElement(name = "peliculas")
@XmlAccessorType(XmlAccessType.FIELD)

public class ListaPeliculasJAXB implements Serializable {
    @XmlElement(name = "pelicula") // cada elemento dentro será <pelicula>...</pelicula>
    private List<Pelicula> peliculas = new ArrayList<>();

    public ListaPeliculasJAXB() {
    }

    public ListaPeliculasJAXB(List<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(List<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }
}
    
