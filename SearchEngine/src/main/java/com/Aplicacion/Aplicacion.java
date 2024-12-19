package com.Aplicacion;

import com.Controlador.Controlador;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.multi.MultiLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author Prieto
 */
public class Aplicacion {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        try {
            UIManager.setLookAndFeel( new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Aplicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        Controlador c = Controlador.getInstance(null);
        c.init();
    }
}
