/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modelo;

import com.mycompany.excepciones.MyException;
import java.io.Serializable;

/**
 *
 * @author jorge
 */
public class Director implements Serializable {

    private String idDirector, nombre, apellido;

    public Director(String idDirector, String nombre, String apellido) throws MyException {
        this.idDirector = idDirector;
        this.nombre = validarNombre(nombre);
        this.apellido = validarApellido(apellido);
    }
    private String validarNombre (String nombre) throws MyException{
         if(!nombre.matches("[a-zA-Z0-9ñáéíóúÁÉÍÓÚñÑ: \\\"-_ç() ]{1,30}")) {throw new MyException("el nombre no es valido");}
         return nombre;
    }
    
    private String validarApellido (String apellido) throws MyException{
         if(!apellido.matches("[a-zA-Z0-9ñáéíóúÁÉÍÓÚñÑ: \\\"-_ç() ]{1,30}")) {throw new MyException("el apellido no es valido");}
         return apellido;
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

    public void setNombre(String nombre) throws MyException {
        this.nombre = validarNombre(nombre);
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) throws MyException {
        this.apellido = validarApellido(apellido);
    }

    @Override
    public String toString() {
        return idDirector + " - " + nombre + " " + apellido;
    }
    
     // compara dos directores.

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Director other = (Director) obj;
        return idDirector != null && idDirector.equals(other.idDirector);
    }
     //Genera el hash code basado en el id del director
    @Override
    public int hashCode() {
        return idDirector != null ? idDirector.hashCode() : 0;
    }

}
