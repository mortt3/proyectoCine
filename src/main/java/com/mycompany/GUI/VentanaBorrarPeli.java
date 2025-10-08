/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.GUI;

import com.mycompany.excepciones.MyException;
import com.mycompany.gestor.GestorPeliculas;
import com.mycompany.modelo.Pelicula;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jorge
 */
public class VentanaBorrarPeli extends javax.swing.JFrame {

    private VentanaPrincipal vP;
    private GestorPeliculas gestorPeli;
    private List<Pelicula> peliculas;

    /**
     * Creates new form VentanaRegistroPelicula
     */
    public VentanaBorrarPeli(VentanaPrincipal vP) {
        try {
            this.vP = vP;
            initComponents();
            gestorPeli = new GestorPeliculas();
            this.setVisible(true);
            cargarPeliculas(); //inicializamos la tabla
            limpiar();
        } catch (MyException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }

    }

    private void cargarPeliculas() {
        try {
            peliculas = gestorPeli.getPeliculas();
            String[] columnas = {"ID", "Título", "Género", "Director"};
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

            for (Pelicula p : peliculas) {
                Object[] fila = {
                    p.getIdPeli(),
                    p.getTitulo(),
                    p.getGenero(),
                    p.getDirector().getNombre() + " " + p.getDirector().getApellido() // nombre y apellido del director
                };
                modelo.addRow(fila);
            }
            tabla.setModel(modelo);
        } catch (MyException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar películas: " + ex.getMessage());
        }
    }

    private void limpiar() {
        txtId.setText("id de la peli");
        txtId.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtId.getText().equals("id de la peli")) {
                    txtId.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtId.getText().isEmpty()) {
                    txtId.setText("id de la peli");
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
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

        txtId.setText("id");

        jLabel1.setText("Ingrese el id de la pelicula que desea borrar:");

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(228, 228, 228))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnSalir))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int confirmar = JOptionPane.showConfirmDialog(this, "¿desas salir?", "", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION)
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
            String id = txtId.getText().trim();

            Pelicula aBorrar = null;
            for (Pelicula p : peliculas) {
                if (p.getIdPeli().equals(id)) {
                    aBorrar = p;
                }
            }

            if (aBorrar == null ) {
                JOptionPane.showMessageDialog(this, "No existe una película con ese ID.");
            } else {

                int confirmar = JOptionPane.showConfirmDialog(this,
                        "¿Seguro que deseas borrar la película: " + aBorrar.getTitulo() + "?",
                        "",
                        JOptionPane.YES_NO_OPTION);

                if (confirmar == JOptionPane.YES_OPTION) {
                    gestorPeli.borrarPelicula(aBorrar);
                    JOptionPane.showMessageDialog(this, "Película borrada con éxito.");
                    cargarPeliculas(); // refrescar la tabla
                    limpiar(); // quitamso el texto escirto
                }
            }

        } catch (MyException ex) {
            JOptionPane.showMessageDialog(this, "Error al borrar: " + ex.getMessage());
        }

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void salir() {
        // TODO add your handling code here:
        vP.setVisible(true);
        this.dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtId;
    // End of variables declaration//GEN-END:variables
}
