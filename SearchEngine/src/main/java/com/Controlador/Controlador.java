package com.Controlador;

import com.Vista.VFramePrincipal;
import com.Vista.VPanelPrincipal;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
 *
 * @author Prieto
 */
public class Controlador implements ActionListener{
    
    private VFramePrincipal vPrincipal;
    private VPanelPrincipal vpPrincipal;

    public Controlador() {
        vPrincipal = new VFramePrincipal();
        vpPrincipal = new VPanelPrincipal();
        
        addListeners();
        addFrameSettings();
        
        
    }
    
    private void addFrameSettings() {
        vPrincipal.setLocationRelativeTo(null);//situa la ventana en el centro de la pantalla
        vPrincipal.setVisible(true);//muestra la ventana
        //vprincipal.setPreferredSize(new Dimension(970,400));
        vPrincipal.getContentPane().setLayout(new CardLayout());//Layout CardLayout para poder tener 
                                                               //mas de un panel en la misma posicion
        vPrincipal.add(vpPrincipal);// añade el jpanel a la vista del frame
        //vPrincipal.add(vpMenu);
        //vprincipal.add(vpsocios);
        //vprincipal.add(vpPrincipal);
        //vprincipal.pack();
        this.muestraPanel(vpPrincipal);//para que muestre un jpanel vacio al abrirlo                                                        
    }
    
    private void muestraPanel(JPanel panel) {
        vpPrincipal.setVisible(false);
        //vpMenu.setVisible(false);
        //vpmonitor.setVisible(false);
        //vpsocios.setVisible(false);
        panel.setVisible(true);//muestra el panel
        panel.updateUI();//actualiza el look and feel del jpanel
    }

    
    private void addListeners() {
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "Salir de la aplicacion" -> {
                vPrincipal.dispose();
                System.exit(0);
            }
        }
    }

}
