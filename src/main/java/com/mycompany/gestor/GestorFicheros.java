/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestor;

import com.mycompany.excepciones.MyException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author jorge
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorFicheros<T> {

    private final File fichero;
    private static final File CARPETAINICIAL = new File("src", "main");
    private static final File CARPETA = new File(CARPETAINICIAL, "Ficheros");

    public GestorFicheros(String nombreFichero) throws MyException {
        if (!CARPETA.exists()) {
            crearCarpeta();
        }
        this.fichero = new File(CARPETA, nombreFichero);

        try {
            if (!fichero.exists()) {
                fichero.createNewFile();
                guardarLista(new ArrayList<>());
            }
        } catch (IOException e) {
            throw new MyException("Error creando fichero: " + fichero.getAbsolutePath());
        }
    }

    public static void crearCarpeta() {
        CARPETA.mkdirs();
    }

    public List<T> leerLista() throws MyException {
        if (!fichero.exists() || fichero.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                return (List<T>) obj;
            } else {
                return new ArrayList<>();
            }
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new MyException("Error leyendo fichero: " + fichero.getAbsolutePath());
        }
    }

    public void guardarLista(List<T> lista) throws MyException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            throw new MyException("Error guardando en fichero: " + fichero.getAbsolutePath());
        }
    }

    public void aniadir(T obj) throws MyException {
        List<T> lista = leerLista();
        lista.add(obj);
        guardarLista(lista);
    }

    public void borrar(T obj) throws MyException {
        List<T> lista = leerLista();
        if (lista.remove(obj)) {
            guardarLista(lista);
        } else {
            throw new MyException("No se encontró el objeto para borrar: " + obj);
        }
    }

    public void modificar(T antiguo, T nuevo) throws MyException {
        List<T> lista = leerLista();
        int index = lista.indexOf(antiguo);
        if (index != -1) {
            lista.set(index, nuevo);
            guardarLista(lista);
        } else {
            throw new MyException("No se encontró el objeto a modificar: " + antiguo);
        }
    }
}
