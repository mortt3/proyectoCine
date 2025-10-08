/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestor;

import com.mycompany.excepciones.MyException;
import com.mycompany.modelo.Actor;
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

    /**
     * la lista de actores guardados
     *
     * @return lista de actores
     * @throws MyException
     */
    public List<Actor> getActores() throws MyException {
        return gestor.leerLista();
    }

    /**
     * Importa actores desde un archivo de texto (formato: id;nombre;edad)
     *
     * @param ruta ruta del archivo
     * @throws MyException
     */
    public void importarActores(String ruta) throws MyException {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            List<Actor> actores = new ArrayList<>();
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    String id = partes[0].trim();
                    String nombre = partes[1].trim();
                    int edad = Integer.parseInt(partes[2].trim());
                    actores.add(new Actor(id, nombre, edad));
                }
            }

            gestor.guardarLista(actores);
        } catch (IOException e) {
            throw new MyException("Error al importar actores desde: " + ruta);
        } catch (NumberFormatException e) {
            throw new MyException("Formato de edad inválido en el archivo de actores.");
        }
    }

    /**
     * Exporta la lista de actores (formato: id;nombre;edad)
     *
     * @param ruta ruta
     * @throws MyException
     */
    public void exportarActores(String ruta) throws MyException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Actor a : getActores()) {
                bw.write(a.getIdActor() + ";" + a.getNombre() + ";" + a.getEdad());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new MyException("Error al exportar actores a: " + ruta);
        }
    }

    /**
     * Importar actores desde un archivo de texto en formato binario.
     *
     * @param ruta ruta del archivo
     * @throws MyException
     */
    public void importarAcotresBinario(String ruta) throws MyException {
        gestor.importarBinario(ruta);
    }

    /**
     * Exportar actores desde un archivo de texto en formato binario.
     *
     * @param ruta ruta del archivo
     * @throws MyException
     */
    public void exportarAcotresBinario(String ruta) throws MyException {
        gestor.exportarBinario(ruta);
    }

}
