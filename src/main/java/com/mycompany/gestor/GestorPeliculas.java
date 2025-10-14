/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestor;

import com.mycompany.excepciones.MyException;
import com.mycompany.modelo.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    public void importarPeliculas(String ruta,
            GestorDirectores gestorDirectores,
            GestorActores gestorActores) throws MyException {
        List<Pelicula> peliculas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                try {
                    String[] partes = linea.split(";");
                    if (partes.length < 5) {
                        continue; // Línea incompleta
                    }
                    String idPeli = partes[0].trim();
                    String titulo = partes[1].trim();
                    String genero = partes[2].trim();

                    String[] datosDirector = partes[3].split(",");
                    String idDirector = datosDirector[0].trim();
                    String nombreDir = datosDirector.length > 1 ? datosDirector[1].trim() : "Desconocido";
                    String apellidoDir = datosDirector.length > 2 ? datosDirector[2].trim() : "Desconocido";

                    // Buscar director en gestor
                    Director director = null;
                    for (Director d : gestorDirectores.getDirectores()) {
                        if (d.getIdDirector().equalsIgnoreCase(idDirector)) {
                            director = d;
                            break;
                        }
                    }

                    // Crear director si no existe
                    if (director == null) {
                        try {
                            director = new Director(idDirector, nombreDir, apellidoDir);
                            gestorDirectores.aniadirDirector(director);
                        } catch (MyException e) {
                            System.out.println("No se pudo crear director para película " + idPeli + ": " + e.getMessage());
                            continue; // Saltar esta película
                        }
                    }

                    // Actores
                    List<Actor> actores = new ArrayList<>();
                    String[] datosActores = partes[4].split("\\|");
                    for (String infoActor : datosActores) {
                        try {
                            String[] info = infoActor.split(",");
                            if (info.length < 3) {
                                continue;
                            }

                            String idActor = info[0].trim();
                            String nombreActor = info[1].trim();
                            int edad = Integer.parseInt(info[2].trim());

                            // Buscar actor en gestor
                            Actor actor = null;
                            for (Actor a : gestorActores.getActores()) {
                                if (a.getIdActor().equalsIgnoreCase(idActor)) {
                                    actor = a;
                                    break;
                                }
                            }

                            // Crear actor si no existe
                            if (actor == null) {
                                try {
                                    actor = new Actor(idActor, nombreActor, edad);
                                    gestorActores.aniadirActor(actor);
                                } catch (MyException e) {
                                    System.out.println("No se pudo crear actor " + idActor + " para película " + idPeli + ": " + e.getMessage());
                                    continue; // Saltar este actor
                                }
                            }

                            actores.add(actor);
                        } catch (NumberFormatException e) {
                            System.out.println("Edad inválida para actor en película " + idPeli);
                        }
                    }

                    // Crear película solo si hay director y al menos un actor válido
                    if (director != null && !actores.isEmpty()) {
                        try {
                            Pelicula pelicula = new Pelicula(idPeli, titulo, genero, director, actores);
                            peliculas.add(pelicula);
                        } catch (MyException e) {
                            System.out.println("No se pudo crear película " + idPeli + ": " + e.getMessage());
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Error en línea: " + linea + " -> " + e.getMessage());
                }
            }

            // Guardar todas las películas válidas
            gestor.guardarLista(peliculas);

        } catch (IOException e) {
            throw new MyException("Error al leer el archivo de películas: " + ruta);
        }
    }

    /**
     * Importa películas desde un archivo binario.
     *
     * @param ruta ruta del archivo
     * @param gestorDirectores gestor de los directores válidos
     * @param gestorActores Gestor de los actores válidos
     * @throws MyException si ocurre un error al importar
     */
    public void importarPeliculasBinario(
            String ruta,
            GestorDirectores gestorDirectores,
            GestorActores gestorActores) throws MyException {

        List<Pelicula> peliculas = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {

            while (true) {
                try {
                    Pelicula p = (Pelicula) ois.readObject();

                    //comprobar que exista el director
                    Director director = null;
                    for (Director d : gestorDirectores.getDirectores()) {
                        if (d.getIdDirector().equalsIgnoreCase(p.getDirector().getIdDirector())) {
                            director = d;
                            break;
                        }
                    }
                    //se crea si no existe
                    if (director == null) {
                        director = p.getDirector();
                        gestorDirectores.aniadirDirector(director);
                    }
                    //comprobar que exista el actor
                    List<Actor> actoresActualizados = new ArrayList<>();
                    for (Actor a : p.getActores()) {
                        Actor actorEncontrado = null;
                        for (Actor existente : gestorActores.getActores()) {
                            if (existente.getIdActor().equalsIgnoreCase(a.getIdActor())) {
                                actorEncontrado = existente;
                                break;
                            }
                        }
                        //se crea si no existe
                        if (actorEncontrado == null) {
                            gestorActores.aniadirActor(a);
                            actoresActualizados.add(a);
                        } else {
                            actoresActualizados.add(actorEncontrado);
                        }
                    }

                    // === Crear nueva película con los objetos actualizados ===
                    Pelicula nuevaPeli = new Pelicula(
                            p.getIdPeli(),
                            p.getTitulo(),
                            p.getGenero(),
                            director,
                            actoresActualizados
                    );

                    peliculas.add(nuevaPeli);

                } catch (EOFException e) {
                    break; // Fin del archivo
                }
            }

            // Guardar todas las películas leídas
            gestor.guardarLista(peliculas);

        } catch (IOException e) {
            throw new MyException("Error al importar películas desde: " + ruta);
        } catch (ClassNotFoundException e) {
            throw new MyException("Error al leer los objetos del archivo binario.");
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

    public void exportarPeliculasBinario(String ruta) throws MyException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            for (Pelicula p : getPeliculas()) {
                oos.writeObject(p);
            }
        } catch (IOException e) {
            throw new MyException("Error al exportar películas a: " + ruta);
        }
    }

}
