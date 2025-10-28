/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestor;

import com.mycompany.excepciones.MyException;
import com.mycompany.modelo.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
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
import java.io.File;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

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

    //ver si esxiste la pelicula
    private boolean existePelicula(String titulo) throws MyException {
        for (Pelicula p : getPeliculas()) {  // usamos tu getPeliculas()
            if (p.getTitulo().equalsIgnoreCase(titulo)) {
                return true;
            }
        }
        return false;
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
                            System.out.println("No se pudo crear película " + idPeli + e.getMessage());
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Error en línea: " + linea + " " + e.getMessage());
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

                    // Crear nueva película con los objetos actualizados
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

    public void importarPeliculasDOM(String ruta, GestorDirectores gestorDirectores, GestorActores gestorActores) throws MyException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(ruta));
            document.getDocumentElement().normalize();
            NodeList listaPeliculas = document.getElementsByTagName("pelicula");
            List<Pelicula> peliculas = new ArrayList<>();
            for (int i = 0; i < listaPeliculas.getLength(); i++) {
                Node nodo = listaPeliculas.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    try {
                        Element elemento = (Element) nodo;
                        String idPeli = elemento.getElementsByTagName("idPeli").item(0).getTextContent();
                        String titulo = elemento.getElementsByTagName("titulo").item(0).getTextContent();
                        String genero = elemento.getElementsByTagName("genero").item(0).getTextContent();

                        // Director
                        Element elemDirector = (Element) elemento.getElementsByTagName("director").item(0);
                        String idDirector = elemDirector.getElementsByTagName("idDirector").item(0).getTextContent();
                        Director director = null;
                        for (Director d : gestorDirectores.getDirectores()) {
                            if (d.getIdDirector().equalsIgnoreCase(idDirector)) {
                                director = d;
                                break;
                            }
                        }
                        if (director == null) {
                            String nombreDir = elemDirector.getElementsByTagName("nombre").item(0).getTextContent();
                            String apellidoDir = elemDirector.getElementsByTagName("apellido").item(0).getTextContent();
                            director = new Director(idDirector, nombreDir, apellidoDir);
                            gestorDirectores.aniadirDirector(director);
                        }

                        // Actores
                        List<Actor> actores = new ArrayList<>();
                        NodeList listaActores = elemento.getElementsByTagName("actor");
                        for (int j = 0; j < listaActores.getLength(); j++) {
                            Element elemActor = (Element) listaActores.item(j);
                            String idActor = elemActor.getElementsByTagName("idActor").item(0).getTextContent();
                            Actor actor = null;
                            for (Actor a : gestorActores.getActores()) {
                                if (a.getIdActor().equalsIgnoreCase(idActor)) {
                                    actor = a;
                                    break;
                                }
                            }
                            if (actor == null) {
                                String nombreActor = elemActor.getElementsByTagName("nombre").item(0).getTextContent();
                                int edad = Integer.parseInt(elemActor.getElementsByTagName("edad").item(0).getTextContent());
                                actor = new Actor(idActor, nombreActor, edad);
                                gestorActores.aniadirActor(actor);
                            }
                            actores.add(actor);
                        }

                        Pelicula pelicula = new Pelicula(idPeli, titulo, genero, director, actores);
                        peliculas.add(pelicula);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                gestor.guardarLista(peliculas);
            }
        } catch (Exception e) {
            throw new MyException("Error al importar películas con DOM");
        }
    }

    public void importarPeliculasSAX(String ruta, GestorDirectores gestorDirectores, GestorActores gestorActores) throws MyException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            List<Pelicula> peliculas = new ArrayList<>();

            DefaultHandler handler = new DefaultHandler() {

                private StringBuilder contenido = new StringBuilder();

                private String idPeli, titulo, genero;
                private Director directorActual;
                private String idDirector, nombreDirector, apellidoDirector;
                private List<Actor> actoresActuales = new ArrayList<>();
                private String idActor, nombreActor;
                private int edadActor;

                public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) {
                    // Limpiar el contenido al iniciar un nuevo elemento
                    contenido.setLength(0);
                    //si es pelicula se reinician los actores
                    if (qName.equalsIgnoreCase("pelicula")) {
                        actoresActuales = new ArrayList<>();
                        directorActual = null;
                        idPeli = null;
                        titulo = null;
                        genero = null;
                    }
                }

                public void characters(char[] ch, int start, int length) {
                    contenido.append(ch, start, length);
                }

                public void endElement(String uri, String localName, String qName) {
                    try {
                        switch (qName) {
                            case "idPeli":
                                idPeli = contenido.toString().trim();
                                break;
                            case "titulo":
                                titulo = contenido.toString().trim();
                                break;
                            case "genero":
                                genero = contenido.toString().trim();
                                break;
                            case "idDirector":
                                idDirector = contenido.toString().trim();

                                break;
                            case "nombre":
                                if (directorActual == null) {
                                    nombreDirector = contenido.toString().trim();
                                } else {
                                    nombreActor = contenido.toString().trim();
                                }

                                break;
                            case "apellido":
                                apellidoDirector = contenido.toString().trim();
                                break;
                            case "director":
                                try {
                                    directorActual = new Director(idDirector, nombreDirector, apellidoDirector);
                                    gestorDirectores.aniadirDirector(directorActual);
                                } catch (MyException ex) {
                                    System.out.println("No se pudo crear director: " + ex.getMessage());
                                }
                                break;
                            case "idActor":
                                idActor = contenido.toString().trim();
                                break;
                            case "edad":
                                try {
                                    edadActor = Integer.parseInt(contenido.toString().trim());
                                    if (edadActor == 0) {
                                        throw new MyException("La edad no puede ser negativa");
                                    }
                                } catch (NumberFormatException ex) {
                                    throw new MyException("Edad inválida para actor");
                                }

                                break;
                            case "actor":
                                try {
                                    Actor actor = new Actor(idActor, nombreActor, edadActor);
                                    gestorActores.aniadirActor(actor);
                                    actoresActuales.add(actor);
                                } catch (MyException ex) {
                                    System.out.println("No se pudo crear actor: " + ex.getMessage());
                                }
                            case "pelicula":
                                // Crear la película al cerrar el elemento pelicula
                                try {
                                    Pelicula pelicula = new Pelicula(idPeli, titulo, genero, directorActual, actoresActuales);
                                    peliculas.add(pelicula);
                                } catch (MyException ex) {
                                    throw new MyException("No se pudo crear película: " + ex.getMessage());
                                }
                                break;
                        }
                    } catch (MyException ex) {
                        Logger.getLogger(GestorPeliculas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            saxParser.parse(new File(ruta), handler);
            // Guardar todas las películas leídas y evitar duplicados
            List<Pelicula> peliculasSinDuplicados = new ArrayList<>();
            for (Pelicula p : peliculas) {
                if (!gestor.leerLista().contains(p)) {
                    gestor.aniadir(p);
                    peliculasSinDuplicados.add(p);
                }
            }
            gestor.guardarLista(peliculasSinDuplicados);
            System.out.println(" Peliculas importadas correctamente con SAX  ");

        } catch (Exception e) {
            throw new MyException("Error al importar películas con SAX: " + e.getMessage());
        }
    }

    /**
     * Exporta las películas a un archivo de texto en el formato
     * idPelicula;titulo;genero;idDirector;idActor1,idActor2,...
     *
     * @param ruta ruta del archivo de destino
     * @throws MyException si ocurre un error durante la exportación
     */
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

    /**
     * Exporta las películas a un archivo XML usando DOM.
     *
     * @param ruta ruta del archivo XML de destino
     * @throws MyException si ocurre un error durante la exportación
     */
    public void exportarPeliculasDOM(String ruta) throws MyException {
        try {
            // Crear documento XML usando DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            // Elemento raíz "peliculas"
            Element root = document.createElement("peliculas");
            document.appendChild(root);
            // Añadir cada película como un elemento hijo
            for (Pelicula p : getPeliculas()) {    //pelicula 
                Element peliculaElem = document.createElement("pelicula");
                // idPeli
                Element idPeliElem = document.createElement("idPeli");
                idPeliElem.appendChild(document.createTextNode(p.getIdPeli()));
                peliculaElem.appendChild(idPeliElem);
                // titulo
                Element tituloElem = document.createElement("titulo");
                tituloElem.appendChild(document.createTextNode(p.getTitulo()));
                peliculaElem.appendChild(tituloElem);
                // genero
                Element generoElem = document.createElement("genero");
                generoElem.appendChild(document.createTextNode(p.getGenero()));
                peliculaElem.appendChild(generoElem);

                // Director
                Element directorElem = document.createElement("director");
                if (p.getDirector() != null) {
                    // idDirector
                    Element idDirectorElem = document.createElement("idDirector");
                    idDirectorElem.appendChild(document.createTextNode(p.getDirector().getIdDirector()));
                    directorElem.appendChild(idDirectorElem);

                    Element nombreDirElem = document.createElement("nombre");
                    nombreDirElem.appendChild(document.createTextNode(p.getDirector().getNombre()));
                    directorElem.appendChild(nombreDirElem);

                    Element apellidoDirElem = document.createElement("apellido");
                    apellidoDirElem.appendChild(document.createTextNode(p.getDirector().getApellido()));
                    directorElem.appendChild(apellidoDirElem);
                }
                peliculaElem.appendChild(directorElem);

                // recorre los actores
                for (Actor a : p.getActores()) {
                    Element actorElem = document.createElement("actor");

                    // idActor
                    Element idActorElem = document.createElement("idActor");
                    idActorElem.appendChild(document.createTextNode(a.getIdActor()));
                    actorElem.appendChild(idActorElem);

                    // nombre actor
                    Element nombreActorElem = document.createElement("nombre");
                    nombreActorElem.appendChild(document.createTextNode(a.getNombre()));
                    actorElem.appendChild(nombreActorElem);
                    // edad actor
                    Element edadActorElem = document.createElement("edad");
                    edadActorElem.appendChild(document.createTextNode(String.valueOf(a.getEdad())));
                    actorElem.appendChild(edadActorElem);

                    peliculaElem.appendChild(actorElem);
                }

                // Añadir la película al elemento raíz
                root.appendChild(peliculaElem);
            }

            // Guardar el documento XML en archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(ruta));
            transformer.transform(source, result);

        } catch (Exception e) {
            throw new MyException("Error al exportar películas con DOM: " + e.getMessage());
        }

    }

    public void exportarPeliculasJAXB(String ruta) throws MyException {
        try {
            // Crear el contexto con la clase raíz
            JAXBContext context = JAXBContext.newInstance(ListaPeliculasJAXB.class);

            // Crear marshaller (para escribir XML)
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Obtener películas actuales del fichero
            List<Pelicula> peliculas = getPeliculas();

            // Envolverlas en el contenedor
            ListaPeliculasJAXB listaPeliculas = new ListaPeliculasJAXB(peliculas);

            // Guardar en archivo XML
            marshaller.marshal(listaPeliculas, new File(ruta));

        } catch (Exception e) {
            throw new MyException("Error al exportar películas con JAXB: " + e.getMessage());
        }
    }
    
    public void importarPeliculasJAXB(String ruta,
            GestorDirectores gestorDirectores,
            GestorActores gestorActores) throws MyException {
        try {
            //Crear el JAXB y el unmarshaller
            JAXBContext context = JAXBContext.newInstance(ListaPeliculasJAXB.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            //   Leer el archivo XML
            ListaPeliculasJAXB lista = (ListaPeliculasJAXB) unmarshaller.unmarshal(new File(ruta));

            //Obtener las listas actuales
            List<Director> directoresActuales = gestorDirectores.getDirectores();
            List<Actor> actoresActuales = gestorActores.getActores();

            //Recorrer las películas del XML
            for (Pelicula p : lista.getPeliculas()) {

                //DIRECTOR
                Director directorImportado = p.getDirector();
                if (directorImportado != null) {
                    Director directorEncontrado = null;

                    //Buscar si ya existe un director igual
                    for (Director d : directoresActuales) {
                        if (d.getNombre().equalsIgnoreCase(directorImportado.getNombre())
                                && d.getApellido().equalsIgnoreCase(directorImportado.getApellido())) {
                            directorEncontrado = d;
                            break;
                        }
                    }

                    //Si no existe se añade
                    if (directorEncontrado == null) {
                        gestorDirectores.aniadirDirector(directorImportado);
                        directoresActuales.add(directorImportado);
                        p.setDirector(directorImportado);
                    } else {
                        p.setDirector(directorEncontrado);
                    }
                }

                //ACTORES
                List<Actor> actoresFinales = new ArrayList<>();

                for (Actor actorImportado : p.getActores()) {
                    Actor actorEncontrado = null;

                    //buscar si ya existe el actor
                    for (Actor a : actoresActuales) {
                        if (a.getNombre().equalsIgnoreCase(actorImportado.getNombre())) {
                            actorEncontrado = a;
                            break;
                        }
                    }

                    //si no existe, se añade
                    if (actorEncontrado == null) {
                        gestorActores.aniadirActor(actorImportado);
                        actoresActuales.add(actorImportado);
                        actoresFinales.add(actorImportado);
                    } else {
                        actoresFinales.add(actorEncontrado);
                    }
                }

                //Asignar los actores finales
                p.setActores(actoresFinales);

                //AÑADIR PELÍCULA
                if (!existePelicula(p.getTitulo())) {
                    aniadirPelicula(p, directoresActuales, actoresActuales);
                } 

            }

        } catch (Exception e) {
            throw new MyException("error al importar películas con JAXB: " + e.getMessage());
        }
    }

}
