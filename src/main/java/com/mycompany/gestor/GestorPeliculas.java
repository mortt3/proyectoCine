/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestor;

import com.mycompany.excepciones.MyException;
import com.mycompany.modelo.*;
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
public class GestorPeliculas {

    private final GestorFicheros<Pelicula> gestor;

    public GestorPeliculas() throws MyException {
        gestor = new GestorFicheros<>("peliculas.dat");
    }

    /**
     * Añade una película , validando director y actores estén registrados.
     *
     * @param p Película
     * @param directoresDisponibles directores válidos
     * @param actoresDisponibles actores válidos
     * @throws MyException
     */
    public void aniadirPelicula(Pelicula p,
            List<Director> directoresDisponibles,
            List<Actor> actoresDisponibles) throws MyException {

        // Validar que el director está registrado
        if (!directoresDisponibles.contains(p.getDirector())) {
            throw new MyException("El director no está registrado: " + p.getDirector());
        }

        // Validar que todos los actores están registrados
        for (Actor a : p.getActores()) {
            if (!actoresDisponibles.contains(a)) {
                throw new MyException("El actor no está registrado: " + a);
            }
        }

        gestor.aniadir(p);
    }

    public void borrarPelicula(Pelicula p) throws MyException {
        gestor.borrar(p);
    }

    public void modificarPelicula(Pelicula nueva,
            List<Director> directoresDisponibles,
            List<Actor> actoresDisponibles) throws MyException {
        List<Pelicula> peliculas = getPeliculas(); // Leer películas actuales
        Pelicula original = null;

        // Buscar la película original por ID
        for (Pelicula p : peliculas) {
            if (p.getIdPeli().equalsIgnoreCase(nueva.getIdPeli())) {
                original = p;
                break;
            }
        }

        if (original == null) {
            throw new MyException("No se encontró la película con ID: " + nueva.getIdPeli());
        }

        // Eliminar la película original
        borrarPelicula(original);

        // Añadir la nueva (validando director y actores)
        aniadirPelicula(nueva, directoresDisponibles, actoresDisponibles);
    }

    // litado de películas almacenadas.
    public List<Pelicula> getPeliculas() throws MyException {
        return gestor.leerLista();
    }

    /**
     * Importa películas desde un archivo de texto. el doc debe de tener el
     * formato tipo idPelicula;titulo;genero;idDirector;idActor1,idActor2,...
     *
     * @param ruta ruta del archivo
     * @param gestorDirectores gestor que contiene los directores válidos
     * @param gestorActores Gestor que contiene los actores válidos
     * @throws MyException si ocurre un error al importar
     */
    public void importarPeliculas(
            String ruta,
            GestorDirectores gestorDirectores,
            GestorActores gestorActores) throws MyException {

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            List<Pelicula> peliculas = new ArrayList<>();
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length < 5) {
                    continue; // Línea incompleta, se ignora
                }
                String idPeli = partes[0].trim();
                String titulo = partes[1].trim();
                String genero = partes[2].trim();
                String[] datosDirector = partes[3].split(",");
                String[] datosActores = partes[4].split("\\|");
                String idDirector = datosDirector[0].trim();
                String nombreDir = datosDirector.length > 1 ? datosDirector[1].trim() : "Desconocido";
                String apellidoDir = datosDirector.length > 2 ? datosDirector[2].trim() : "";

                Director director = gestorDirectores.getDirectores().stream()
                        .filter(d -> d.getIdDirector().equalsIgnoreCase(idDirector))
                        .findFirst()
                        .orElse(null);

                if (director == null) {
                    // Si el director no existe, lo creamos y añadimos
                    director = new Director(idDirector, nombreDir, apellidoDir);
                    gestorDirectores.aniadirDirector(director);
                }
                List<Actor> actores = new ArrayList<>();
                for (String datosActor : datosActores) {
                    String[] info = datosActor.split(",");
                    if (info.length < 3) {
                        continue;
                    }
                    String idActor = info[0].trim();
                    String nombreActor = info[1].trim();
                    int edad = Integer.parseInt(info[2].trim());

                    Actor actor = gestorActores.getActores().stream()
                            .filter(a -> a.getIdActor().equalsIgnoreCase(idActor))
                            .findFirst()
                            .orElse(null);

                    if (actor == null) {
                        // Si el actor no existe, se crea y se añade
                        actor = new Actor(idActor, nombreActor, edad);
                        gestorActores.aniadirActor(actor);
                    }
                    actores.add(actor);
                }
                Pelicula pelicula = new Pelicula(idPeli, titulo, genero, director, actores);
                peliculas.add(pelicula);
            }

            // Guardar todas las películas importadas
            gestor.guardarLista(peliculas);

        } catch (IOException e) {
            throw new MyException("Error al leer el archivo de películas: " + ruta);
        } catch (NumberFormatException e) {
            throw new MyException("Error en el formato de edad de algún actor: " + e.getMessage());
        } catch (Exception e) {
            throw new MyException("Error inesperado al importar películas: " + e.getMessage());
        }
    }

    public void exportarPeliculas(String ruta) throws MyException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Pelicula p : getPeliculas()) {
                StringBuilder actoresIds = new StringBuilder();
                for (Actor a : p.getActores()) {
                    if (actoresIds.length() > 0) {
                        actoresIds.append(",");
                    }
                    actoresIds.append(a.getIdActor());
                }

                bw.write(p.getIdPeli() + ";"
                        + p.getTitulo() + ";"
                        + p.getGenero() + ";"
                        + p.getDirector().getIdDirector() + ";"
                        + actoresIds);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new MyException("Error al exportar películas a: " + ruta);
        }
    }

    /**
     * Exportar películas a un archivo de texto en formato binario.
     *
     * @param ruta ruta del archivo
     * @throws MyException
     */
    public void importarPeliculasBinario(String ruta) throws MyException {
        gestor.importarBinario(ruta);
    }

    /**
     * Importar películas desde un archivo de texto en formato binario.
     *
     * @param ruta ruta del archivo
     * @throws MyException
     */
    public void exportarPeliculasBinario(String ruta) throws MyException {
        gestor.exportarBinario(ruta);
    }
}
