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
public class Actor implements Serializable {
    private String idActor;
    private String nombre;
    private int edad;

    public Actor(String idActor, String nombre, int edad) {
        this.idActor = idActor;
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getIdActor() {
        return idActor;
    }

    public void setIdActor(String idActor) {
        this.idActor = idActor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
    
    @Override
    public String toString() {
        return idActor + " - " + nombre + " " + edad;
    }
}
