/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modelo;

import com.mycompany.excepciones.MyException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jorge
 */
@XmlRootElement(name = "actor")
@XmlAccessorType(XmlAccessType.FIELD)
public class Actor implements Serializable {

    private String idActor, nombre;
    private int edad;

    public Actor(String idActor, String nombre, int edad) throws MyException {
        this.idActor = idActor;
        this.nombre = validarNombre(nombre);
        this.edad = validarEdad(edad);
    }

    public Actor() {

    }

    private String validarNombre(String nombre) throws MyException {
        if (!nombre.matches("[a-zA-Z0-9ñáéíóúÁÉÍÓÚñÑ: \\\"-_ç() ]{1,50}")) {
            throw new MyException("el nombre no es valido");
        }
        return nombre;
    }

    public int validarEdad(int edad) throws MyException {
        if (edad < 5 || edad >= 90) {
            throw new MyException("El actor debe de tener ente 0 y 90");
        }
        return edad;
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

    public void setNombre(String nombre) throws MyException {
        this.nombre = validarNombre(nombre);
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) throws MyException {
        this.edad = validarEdad(edad);
    }

    @Override
    public String toString() {
        return idActor + " - " + nombre + " " + edad;
    }
    // compara dos actores por su ID.

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Actor other = (Actor) obj;
        return idActor != null && idActor.equals(other.idActor);
    }
    //Genera el hash code basado en el id del actor

    @Override
    public int hashCode() {
        return idActor != null ? idActor.hashCode() : 0;
    }

}
