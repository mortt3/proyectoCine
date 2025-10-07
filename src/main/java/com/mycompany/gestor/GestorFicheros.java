/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestor;

import com.mycompany.excepciones.MyException;
import com.mycompany.modelo.Director;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author jorge
 */
public class GestorFicheros<T> {
    private final File fichero;
    private static final File CARPETAINICIAL = new File("src", "main");
    private static final File CARPETA = new File(CARPETAINICIAL, "Ficheros");


    public GestorFicheros(String nombreFichero) throws MyException {
        // Asegura que la carpeta exista (aunque el método crearCarpeta también lo hace explícitamente)
        if (!CARPETA.exists()) {
            // Se puede usar crearCarpeta() aquí si se desea.
        }

        this.fichero = new File(CARPETA, nombreFichero);

        try {
            // Si el fichero no existe, se crea y se inicializa con una lista vacía
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

    /**
     * Lee la lista de objetos desde el fichero.
     * @return Lista de objetos de tipo T leída desde el archivo
     * @throws MyException si ocurre un error al leer el fichero
     */
    public List<T> leerLista() throws MyException {
        // Si el fichero no existe o está vacío, se devuelve una lista vacía
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

    /**
     * Guarda una lista completa de objetos en el fichero.
     * @param lista Lista de objetos a guardar
     * @throws MyException
     */
    public void guardarLista(List<T> lista) throws MyException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            throw new MyException("Error guardando en fichero: " + fichero.getAbsolutePath());
        }
    }

    /**
     * Añade un objeto a la lista almacenada y actualiza el fichero.
     * @param obj Objeto
     * @throws MyException 
     */
    public void aniadir(T obj) throws MyException {
        List<T> lista = leerLista();
        lista.add(obj);
        guardarLista(lista);
    }

    /**
     * Elimina un objeto de la lista almacenada, si existe.
     * @param obj Objeto a eliminar
     * @throws MyException 
     */
    public void borrar(T obj) throws MyException {
        List<T> lista = leerLista();
        if (lista.remove(obj)) {
            guardarLista(lista);
        } else {
            throw new MyException("No se encontró el objeto para borrar: " + obj);
        }
    }

    /**
     * Modifica un objeto existente en la lista, reemplazándolo por uno nuevo.
     * @param antiguo Objeto a reemplazar
     * @param nuevo Objeto nuevo que sustituye al antiguo
     * @throws MyException 
     */
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
    
     /**
     * Importa actores binario
     *
     * @param ruta ruta del archivo de origen
     * @throws MyException
     */
    public void importarBinario(String ruta) throws MyException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            Object obj = ois.readObject();
                if (obj instanceof List<?>) {
                    guardarLista((List<T>) obj);
                } else {
                throw new MyException("El archivo no contiene una lista de directores válida.");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new MyException("Error al importar directores en binario desde: " + ruta);
        }
    }

    /**
     * Exporta la lista de actores en binario
     *
     * @param ruta ruta del archivo de destino 
     * @throws MyException
     */
    public void exportarBinario(String ruta) throws MyException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(leerLista());
        } catch (IOException e) {
            throw new MyException("Error al exportar directores en binario: " + ruta);
        }
    }
}

