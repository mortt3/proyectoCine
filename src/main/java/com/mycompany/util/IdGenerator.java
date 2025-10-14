/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.util;

import com.mycompany.excepciones.MyException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jorge
 */
public class IdGenerator {

    private static final File CARPETAINICIAL = new File("src", "main");
    private static final File CARPETA = new File(CARPETAINICIAL, "Ficheros");
    private static final File FILE = new File(CARPETA, "ids.dat");

    private Map<String, Integer> ids;

    public IdGenerator() {
        ids = cargarIds();
    }

    private Map<String, Integer> cargarIds() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            return (Map<String, Integer>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private void saveIds() throws MyException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(ids);
        } catch (IOException e) {
            throw new MyException("Error al guardar los IDs");
        }
    }

    public String generarId(String tipo) throws MyException {
        int nextId = ids.getOrDefault(tipo, 0) + 1;
        ids.put(tipo, nextId);
        saveIds();

        String prefijo = switch (tipo) {
            case "actor" ->
                "ac_";
            case "pelicula" ->
                "pi_";
            case "director" ->
                "di_";
            default ->
                "";
        };
        return String.format("%s%05d", prefijo, nextId);
    }
}
