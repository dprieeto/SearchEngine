package com.Vista;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Prieto
 */
/**
 *
 * @author Prieto
 */
public class VistaMensaje {

    /*
    -1, JOptionPane.PLAIN_MESSAGE -> mensaje sin icono
    0, JOptionPane.ERROR_MESSAGE -> mensaje de error
    1, JOptionPane.INFORMATION_MESSAGE -> mensaje de informacion
    2, JOptionPane.WARNING_MESSAGE -> mensaje de advertencia o warning
    3, JOptionPane.QUESTION_MESSAGE -> mensaje de pregunta
     */

    public void Mensaje(String titulo, String texto, int n) {
        JOptionPane.showMessageDialog(null, texto, titulo, n);
    }

    public void Mensaje(Component C, String titulo, String texto) {
        switch (titulo) {
            case "info" -> {
                JOptionPane.showMessageDialog(C, texto, titulo, JOptionPane.INFORMATION_MESSAGE);
                break;
            }
            case "error" -> {
                JOptionPane.showMessageDialog(C, texto, titulo, JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
    }

    /**
     * Muestra un mensaje de error o informacion. Metodo estatico Asi se puede
     * utilizar sin necesidad de crear un objeto
     *
     * @param C componente sobre el que se lanza el mensaje, puede ser null
     * @param titulo info- mensaje de informacion, error-mensaje de error
     * @param texto texto que sequiere mostrar del mensaje
     */
    public static void StaticMensaje(Component C, String titulo, String texto) {
        switch (titulo) {
            case "info" -> {
                JOptionPane.showMessageDialog(C, texto, titulo, JOptionPane.INFORMATION_MESSAGE);
                break;
            }
            case "error" -> {
                JOptionPane.showMessageDialog(C, texto, titulo, JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
    }

    /**
     * Muestra un mensaje grafico de confirmacion, en nuestro caso devuelve: 0
     * para si o aceptar, 1 para no y 2 para cancelar
     *
     * @param C
     * @param mensaje
     * @return opcion
     */
    public int MensajeConfirmacion(Component C, String mensaje) {
        int opcion = JOptionPane.showConfirmDialog(C, mensaje, "Aviso", JOptionPane.YES_NO_CANCEL_OPTION, 2);
        //OK_CANCEL_OPTION. Botones Aceptar y Cancelar.
        //CLOSED_OPTION. Botón Aceptar.
        //YES_NO_OPTION. Botones Sí y No.
        //YES_NO_CANCEL_OPTION. Botones Sí, No y Cancelar.
        return opcion;
    }

}
