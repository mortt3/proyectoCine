/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modelo;

import java.io.Serializable;

/**
 *
 * @author jorge
 */
public class Director implements Serializable {
    private String idDirector;
    private String nombre;
    private String apellido;

    public Director(String idDirector, String nombre, String apellido) {
        this.idDirector = idDirector;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getIdDirector() {
        return idDirector;
    }

    public void setIdDirector(String idDirector) {
        this.idDirector = idDirector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    @Override
    public String toString() {
        return idDirector + " - " + nombre + " " + apellido;
    }
    
}
