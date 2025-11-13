/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.mycompany.excepciones.MyException;
import com.mycompany.modelo.Actor;
import java.io.File;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.DefaultHandler;

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
     * Importa actores desde binario
     *
     * @param ruta del archivo
     * @throws MyException
     */
    public void importarActoresBinario(String ruta) throws MyException {
        FileInputStream fis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(ruta);
            dis = new DataInputStream(fis);
            List<Actor> actores = new ArrayList<>();

            while (true) {
                try {
                    String id = dis.readUTF();
                    String nombre = dis.readUTF();
                    int edad = dis.readInt();

                    // Crear y añadir a la lista
                    actores.add(new Actor(id, nombre, edad));
                } catch (EOFException e) {
                    break;
                } catch (IOException e) {
                    throw new MyException("Error al leer datos del archivo binario.");
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
     * Importar actores desde XML usando DOM.
     *
     * @param ruta del archivo XML
     * @throws MyException
     */
    public void importarActoresDOM(String ruta) throws MyException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder(); // Crear el constructor de documentos
            Document document = builder.parse(new File(ruta)); // Parsear el archivo XML
            document.getDocumentElement().normalize(); // Normalizar el documento

            NodeList listaActores = document.getElementsByTagName("actor"); // Obtener la lista de nodos "actor"
            List<Actor> actores = new ArrayList<>(); // Lista para almacenar los actores importados

            for (int i = 0; i < listaActores.getLength(); i++) {
                Node nodo = listaActores.item(i); // Obtener cada nodo "actor"

                if (nodo.getNodeType() == Node.ELEMENT_NODE) { // Verificar que es un elemento
                    try {
                        Element elemento = (Element) nodo; // Convertir el nodo a elemento

                        String id = elemento.getElementsByTagName("idActor").item(0).getTextContent();
                        String nombre = elemento.getElementsByTagName("nombre").item(0).getTextContent();
                        int edad = Integer.parseInt(elemento.getElementsByTagName("edad").item(0).getTextContent());

                        actores.add(new Actor(id, nombre, edad));
                    } catch (MyException e) {
                        // Ignora actores inválidos pero sigue con el resto
                        System.out.println("actor incorrecto");
                    }
                }
            }

            gestor.guardarLista(actores);
            System.out.println("Actores importados correctamente desde: " + ruta);

        } catch (Exception e) {
            throw new MyException("Error al importar actores con DOM ");
        }
    }

    /**
     * Importar actores desde XML usando SAX.
     *
     * @param ruta del archivo XML
     * @throws MyException
     */

    public void importarActoresSAX(String ruta) throws MyException {
        try {
            // Crear el parser SAX
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            List<Actor> actores = new ArrayList<>();

            // Crear el manejador de eventos
            DefaultHandler handler = new DefaultHandler() {
                private StringBuilder contenido = new StringBuilder();
                private String id;
                private String nombre;
                private int edad;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    contenido.setLength(0); // limpia el texto anterior
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    contenido.append(ch, start, length);
                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    switch (qName) {
                        case "idActor":
                            id = contenido.toString().trim();
                            break;
                        case "nombre":
                            nombre = contenido.toString().trim();
                            break;
                        case "edad":
                            edad = Integer.parseInt(contenido.toString().trim());
                            break;
                        case "actor":
                            try {
                                actores.add(new Actor(id, nombre, edad));
                            } catch (MyException e) {
                                System.out.println("Actor incorrecto");
                            }
                            break;
                    }
                }
            };

            // Parsear el archivo XML
            saxParser.parse(new File(ruta), handler);

            // Guardar en el fichero binario
            gestor.guardarLista(actores);
            System.out.println(" Actores importados correctamente con SAX  ");

        } catch (Exception e) {
            throw new MyException("Error al importar actores con SAX  ");
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

    public Actor buscarActorPorId(String id) throws MyException {
        for (Actor a : getActores()) {
            if (a.getIdActor().equalsIgnoreCase(id)) {
                return a;
            }
        }
        return null;
    }

}
