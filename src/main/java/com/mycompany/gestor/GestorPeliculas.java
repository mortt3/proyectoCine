/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestor;
import com.mycompany.excepciones.MyException;
import com.mycompany.modelo.*;

import java.util.List;


/**
 *
 * @author jorge
 */
public class GestorPeliculas {

    private final GestorFicheros<Pelicula> gestor;

    public GestorPeliculas() throws MyException {
        gestor = new GestorFicheros<>("peliculas.dat");
    }

    public void aniadirPelicula(Pelicula p, 
                                List<Director> directoresDisponibles, 
                                List<Actor> actoresDisponibles) throws MyException {

        // Validar que el director está registrado
        if (!directoresDisponibles.contains(p.getDirector())) {
            throw new MyException("El director no está registrado: " + p.getDirector());
        }

        // Validar que todos los actores están registrados
        for (Actor a : p.getActores()) {
            if (!actoresDisponibles.contains(a)) {
                throw new MyException("El actor no está registrado: " + a);
            }
        }

        gestor.aniadir(p);
    }

    public void borrarPelicula(Pelicula p) throws MyException {
        gestor.borrar(p);
    }

    public void modificarPelicula(Pelicula antigua, Pelicula nueva) throws MyException {
        gestor.modificar(antigua, nueva);
    }

    public List<Pelicula> getPeliculas() throws MyException {
        return gestor.leerLista();
    }
}

