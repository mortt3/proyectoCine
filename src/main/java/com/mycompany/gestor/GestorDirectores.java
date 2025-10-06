/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestor;

import com.mycompany.excepciones.MyException;
import com.mycompany.modelo.Director;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

    /**
     * Añade un nuevo director al archivo.
     *
     * @param d Director pa añadir
     * @throws MyException 
     */
    public void aniadirDirector(Director d) throws MyException {
        gestor.aniadir(d);
    }

    /**
     * Elimina un director del archivo.
     *
     * @param d director pa eliminar
     * 
     * @throws MyException
     */
    public void borrarDirector(Director d) throws MyException {
        gestor.borrar(d);
    }

    /**
     * Modifica un director existente, reemplazándolo por uno nuevo.
     *
     * @param antiguo Director que se desea reemplazar
     * @param nuevo Director con los datos actualizados
     * 
     * @throws MyException
     */
    public void modificarDirector(Director antiguo, Director nuevo) throws MyException {
        gestor.modificar(antiguo, nuevo);
    }

    /**
     * Devuelve la lista completa de directores almacenados.
     *
     * @return Lista de directores
     * @throws MyException
     */
    public List<Director> getDirectores() throws MyException {
        return gestor.leerLista();
    }

    /**
     * Importa directores desde un archivo de texto.
     * Cada línea del archivo debe tener el formato: id;nombre;apellido
     * @param ruta Ruta del archivo pa importar
     * @throws MyException
     */
    public void importarDirectores(String ruta) throws MyException {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            List<Director> directores = new ArrayList<>();
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    String id = partes[0].trim();
                    String nombre = partes[1].trim();
                    String apellido = partes[2].trim();
                    directores.add(new Director(id, nombre, apellido));
                }
            }
            gestor.guardarLista(directores);
        } catch (IOException e) {
            throw new MyException("Error al importar directores desde: " + ruta);
        }
    }

    /**
     * Exporta los directores actuales a un archivo de texto.
     * Cada director se guarda en una línea con el formato: id;nombre;apellido
     *
     * @param ruta Ruta del archivo de texto destino
     * @throws MyException
     */
    public void exportarDirectores(String ruta) throws MyException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Director d : getDirectores()) {
                bw.write(d.getIdDirector() + ";" + d.getNombre() + ";" + d.getApellido());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new MyException("Error al exportar directores a: " + ruta);
        }
    }
}

