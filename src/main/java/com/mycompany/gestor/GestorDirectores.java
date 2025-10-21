/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestor;

import com.mycompany.excepciones.MyException;
import com.mycompany.modelo.Director;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//para importar DOM y SAX
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

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
     * Devuelve la lista completa de directores almacenados.
     *
     * @return Lista de directores
     * @throws MyException
     */
    public List<Director> getDirectores() throws MyException {
        return gestor.leerLista();
    }

    /**
     * Importa directores desde un archivo de texto. Cada línea del archivo debe
     * tener el formato: id;nombre;apellido
     *
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
     * Exporta los directores actuales a un archivo de texto. Cada director se
     * guarda en una línea con el formato: id;nombre;apellido
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

    /**
     * Importar directores desde binario.
     *
     * @param ruta del archivo a importar
     * @throws MyException
     */
    public void importarDirectoresBinario(String ruta) throws MyException {
        FileInputStream fis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(ruta);
            dis = new DataInputStream(fis);
            List<Director> director = new ArrayList<>();

            while (true) {
                try {
                    String id = dis.readUTF();
                    String nombre = dis.readUTF();
                    String apellido = dis.readUTF();

                    // Crear y añadir a la lista
                    director.add(new Director(id, nombre, apellido));
                } catch (EOFException e) {
                    break;
                } catch (IOException e) {
                    throw new MyException("Error al leer datos del archivo binario.");
                }
            }
            gestor.guardarLista(director);
        } catch (IOException e) {
            throw new MyException("Error al importar actores desde: " + ruta);
        } catch (NumberFormatException e) {
            throw new MyException("Formato de edad inválido en el archivo de actores.");
        }
    }

    /**
     * Importar directores desde XML usando DOM.
     * @param ruta del archivo XML
     * @throws MyException 
     */
    public void importarDirectoresDOM(String ruta) throws MyException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(ruta));

            document.getDocumentElement().normalize();

            NodeList listaDirectores = document.getElementsByTagName("director");
            List<Director> director = new ArrayList<>();
            for (int i = 0; i < listaDirectores.getLength(); i++) {
                Node nodo = listaDirectores.item(i);

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    try {
                        Element elemento = (Element) nodo;

                        String id = elemento.getElementsByTagName("idDirector").item(0).getTextContent();
                        String nombre = elemento.getElementsByTagName("nombre").item(0).getTextContent();
                        String apellido = elemento.getElementsByTagName("apellido").item(0).getTextContent();

                        director.add(new Director(id, nombre, apellido));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            gestor.guardarLista(director);
            System.out.println("Importación completada.");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Importar directores desde XML usando SAX.
     *
     * @param ruta del archivo XML
     * @throws MyException
     */

     //otra forma de importar con SAX usando el handler
    public void importarDirectoresSAX(String ruta) throws MyException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GestorDirectoresHandler handler = new GestorDirectoresHandler(gestor);
            saxParser.parse(new File(ruta), handler);
            System.out.println("Importación completada.");
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new MyException("Error al importar directores con SAX: " + ex.getMessage());
        }
    }

}
