/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestor;

import com.mycompany.excepciones.MyException;
import com.mycompany.modelo.Director;
import java.util.List;

/**
 *
 * @author jorge
 */
public class GestorDirectores {

    private final GestorFicheros<Director> gestor;

    public GestorDirectores() throws MyException {
        gestor = new GestorFicheros<>("directores.dat");
    }

    public void aniadirDirector(Director d) throws MyException {
        gestor.aniadir(d);
    }

    public void borrarDirector(Director d) throws MyException {
        gestor.borrar(d);
    }

    public void modificarDirector(Director antiguo, Director nuevo) throws MyException {
        gestor.modificar(antiguo, nuevo);
    }

    public List<Director> getDirectores() throws MyException {
        return gestor.leerLista();
    }
}
