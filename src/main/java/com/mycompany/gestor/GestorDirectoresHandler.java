package com.mycompany.gestor;

import com.mycompany.excepciones.MyException;
import com.mycompany.modelo.Director;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GestorDirectoresHandler extends DefaultHandler {

    private final GestorFicheros<Director> gestor;
    private List<Director> lista = new ArrayList<>();
    private Director actual;
    private StringBuilder contenido;

    public GestorDirectoresHandler(GestorFicheros<Director> gestor) {
        this.gestor = gestor;
    }

    @Override
    public void startDocument() throws SAXException {
        lista.clear();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        contenido = new StringBuilder();
        if (qName.equalsIgnoreCase("director")) {
            actual = new Director();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (contenido != null) {
            contenido.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // Obtener el texto acumulado hasta ahor
        String texto;
        if (contenido == null) {
            texto = "";
        } else {
            texto = contenido.toString().trim();
        }
        if (actual != null) {
            switch (qName.toLowerCase()) {
                case "iddirector":
                    actual.setIdDirector(texto);
                    break;
                case "nombre": {
                    try {
                        actual.setNombre(texto);
                    } catch (MyException ex) {
                        Logger.getLogger(GestorDirectoresHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

                case "apellido": {
                    try {
                        actual.setApellido(texto);
                    } catch (MyException ex) {
                        Logger.getLogger(GestorDirectoresHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

                case "director":
                    lista.add(actual);
                    actual = null;
                    break;
            }
        }
        contenido = null;
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            gestor.guardarLista(lista);
        } catch (MyException e) {
            throw new SAXException("Error al guardar lista de directores: " + e.getMessage(), e);
        }
    }
}
