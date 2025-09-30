/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.GUI;

import com.mycompany.excepciones.MyException;
import com.mycompany.gestor.GestorDirectores;
import com.mycompany.gestor.GestorPeliculas;
import com.mycompany.modelo.Actor;
import com.mycompany.modelo.Director;
import com.mycompany.modelo.Pelicula;
import com.mycompany.util.IdGenerator;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

/**
 *
 * @author jorge
 */
public class VentanaRegistroPelicula extends javax.swing.JFrame {

    private VentanaPrincipal vP;
    private GestorPeliculas gestorPeli;
    private IdGenerator idGen;
    private List<Actor> actoresSelccionados = new ArrayList<>();
    private List<Director> directoresDisponibles = new ArrayList<>();

    /**
     * Creates new form VentanaRegistroPelicula
     */
    public VentanaRegistroPelicula(VentanaPrincipal vP) {
        try {
            this.vP = vP;
            initComponents();
            cargarDirectores();
            limpiar();
            idGen = new IdGenerator();
            gestorPeli = new GestorPeliculas();
            this.setVisible(true);
        } catch (MyException ex) {
            JOptionPane.showMessageDialog(this, "Error al inicializar gestor de directores");
        }

    }

    private void limpiar() {
        txtTitulo.setText("Titulo de la peli");
        txtTitulo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtTitulo.getText().equals("Titulo de la peli")) {
                    txtTitulo.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtTitulo.getText().isEmpty()) {
                    txtTitulo.setText("Titulo de la peli");
                }
            }
        });
    }

    private void cargarDirectores() {
        try {
            GestorDirectores gestor = new GestorDirectores();
            directoresDisponibles = gestor.getDirectores();
            comboDirector.removeAllItems();
            for (Director d : directoresDisponibles) {
                comboDirector.addItem(d);
            }
        } catch (MyException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar los directores.");
        }
    }

    public void setActoresSeleccionados(List<Actor> actores) {
        actoresSelccionados = actores;
        if (actores.isEmpty()) {
            listaActortes.setText("Seleccionados: Ninguno");
            return;
        }

        String texto = "";
        for (Actor a : actores) {
            if (!texto.isEmpty()) {
                texto += ", "; // añade coma entre nombres
            }
            texto += a.getNombre();
        }

        listaActortes.setText("Seleccionados: " + texto);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtTitulo = new javax.swing.JTextField();
        comboGenero = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        comboDirector = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        listaActortes = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Titulo:");

        txtTitulo.setText("Titulo de la peli");

        comboGenero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Acción", "Comedia", "Drama", "Terror", "ciencia Ficcion" }));

        jLabel3.setText("Genero:");

        jLabel4.setText("Director:");

        comboDirector.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        comboDirector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboDirectorActionPerformed(evt);
            }
        });

        jButton1.setText("Agregar actores");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir sin guardar");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listaActortes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalir)
                        .addGap(23, 23, 23))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(25, 25, 25)
                                .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(18, 18, 18)
                                    .addComponent(comboGenero, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addGap(18, 18, 18)
                                    .addComponent(comboDirector, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jButton1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboDirector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(listaActortes, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnSalir))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        comboDirector.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        VentanaSeleccionActores vSA = new VentanaSeleccionActores(this);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        int confirmar = JOptionPane.showConfirmDialog(this, "desas salir sin guardar cambios?", "", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION)
            salir();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        try {
            String id = idGen.generarId("pelicula");
            String titulo = txtTitulo.getText();
            String genero = (String) comboGenero.getSelectedItem();
            Director director = (Director) comboDirector.getSelectedItem();

            if (actoresSelccionados.isEmpty()) {
                JOptionPane.showConfirmDialog(this, "Debes selecionar al menos a 1 actor", "", JOptionPane.CLOSED_OPTION);
            } else if (titulo.equals("Titulo de la peli")) {
                JOptionPane.showConfirmDialog(this, "Introduce un titulo a la pelicula");
            } else {
                Pelicula p = new Pelicula(id, titulo, genero, director, actoresSelccionados);
                gestorPeli.aniadirPelicula(p, directoresDisponibles, actoresSelccionados);
                JOptionPane.showMessageDialog(this, "pelicula registrada con éxito: " + id);
                int confirmar = JOptionPane.showConfirmDialog(this, "¿Deseas agregamr mas peliculas?", "", JOptionPane.YES_NO_OPTION);
                if (confirmar == JOptionPane.NO_OPTION) {
                    salir();
                } else {
                    limpiar();
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "anio debe de ser un numero, por ejemplo 2002");
        } catch (MyException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void comboDirectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboDirectorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboDirectorActionPerformed

    private void salir() {
        // TODO add your handling code here:
        vP.setVisible(true);
        this.dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<Object> comboDirector;
    private javax.swing.JComboBox<String> comboGenero;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel listaActortes;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables
}
