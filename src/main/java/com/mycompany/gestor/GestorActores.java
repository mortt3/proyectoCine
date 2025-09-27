/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestor;
import com.mycompany.excepciones.MyException;
import com.mycompany.modelo.Actor;
import java.util.List;


/**
 *
 * @author jorge
 */
public class GestorActores {

    private final GestorFicheros<Actor> gestor;

    public GestorActores() throws MyException {
        gestor = new GestorFicheros<>("actores.dat");
    }

    public void aniadirActor(Actor a) throws MyException {
        gestor.aniadir(a);
    }

    public void borrarActor(Actor a) throws MyException {
        gestor.borrar(a);
    }

    public void modificarActor(Actor antiguo, Actor nuevo) throws MyException {
        gestor.modificar(antiguo, nuevo);
    }

    public List<Actor> getActores() throws MyException {
        return gestor.leerLista();
    }
}

