package com.Controlador;

import com.Modelo.Documento;
import com.Modelo.SearchEngine;
import com.Vista.VFramePrincipal;
import com.Vista.VPanelPrincipal;
import com.Vista.VistaMensaje;
import com.Modelo.SolrServer;
import java.awt.CardLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author Prieto
 */
public class Controlador implements ActionListener {
    
    private VFramePrincipal vPrincipal;
    
    private VPanelPrincipal vpPrincipal;
    
    private SearchEngine search;
    
    private static Controlador controlador;

    private Controlador(String coreName) {
        vPrincipal = new VFramePrincipal();
        vpPrincipal = new VPanelPrincipal();
        search = new SearchEngine(coreName);
        controlador = null;
        addListeners();
        addFrameSettings();
        
        
    }
    
    /**
     * Inicializa el unico objeto de esta clase.
     * @return 
     */
    public static Controlador getInstance(String coreName) {
        if( controlador == null)
            controlador = new Controlador(coreName);
        return controlador;
    }
    
    /**
     * Inicia la aplicacion. Si el servidor de solr no esta encendido se cierra
     * la aplicacion.
     */
    public void init() {
        if(SolrServer.isSolrServerRunning())
            this.muestraPanel(vpPrincipal);
        else {
            VistaMensaje.StaticMensaje(vPrincipal, "error", "Actualmente el servidor esta caido." 
                + "\nIntentelo mas tarde");
            vPrincipal.dispose();
        }
            
    }
    
    private void addFrameSettings() {
        //setWindowSize();
        vPrincipal.setLocationRelativeTo(null);//situa la ventana en el centro de la pantalla
        vPrincipal.setVisible(true);//muestra la ventana
        //vprincipal.setPreferredSize(new Dimension(970,400));
        vPrincipal.getContentPane().setLayout(new GridLayout());
        //vPrincipal.getContentPane().setLayout(new CardLayout());//Layout CardLayout para poder tener 
                                                               //mas de un panel en la misma posicion
        vPrincipal.add(vpPrincipal);// añade el jpanel a la vista del frame
        //vPrincipal.add(vpMenu);
        //vprincipal.add(vpsocios);
        //vprincipal.add(vpPrincipal);
        //vprincipal.pack();
        //this.muestraPanel(vpPrincipal);//para que muestre un jpanel vacio al abrirlo 
        
        vpPrincipal.jTextFieldBuscador.setText("");
        
    }
    
    private void muestraPanel(JPanel panel) {
        vpPrincipal.setVisible(false);
        //vpMenu.setVisible(false);
        //vpmonitor.setVisible(false);
        //vpsocios.setVisible(false);
        panel.setVisible(true);//muestra el panel
        panel.updateUI();//actualiza el look and feel del jpanel
    }
    
    

    /**
     * Añade los listeners del action performed
     */
    private void addListeners() {
        
        //vPrincipal.jMenuItemSalir.addActionListener(this);
        
        vpPrincipal.jButtonBuscar.addActionListener(this);
 
    }
    
    /*
    private void setWindowSize() {
        // Obtener el entorno gráfico
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle screenBounds = ge.getMaximumWindowBounds();
        vPrincipal.setSize(screenBounds.width, screenBounds.height);
    }
    */
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "Buscar" -> {
                
                System.out.println("buscando a wally");
                String consulta = vpPrincipal.jTextFieldBuscador.getText();
                //System.out.println(consulta);
                if(consulta != null && !consulta.isBlank())
                    searchDocuments(consulta);
            }
        }
    }
    
    private void searchDocuments(String consulta) {
        List<Documento> docs = search.buscar(consulta);
        if(!docs.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Documentos encontrados: " + docs.size()).append("\n\n");
            for(int i=0; i<docs.size(); i++) {
                String indice = docs.get(i).getIndice();
                String texto = docs.get(i).getTexto();
                
                sb.append("####### Numero de documento: " + indice).append("\n");
                sb.append(texto).append("\n\n");
            }
            int posicionScroll = vpPrincipal.jTextPaneResultados.getCaretPosition();
            vpPrincipal.jTextPaneResultados.setText(sb.toString());
            vpPrincipal.jTextPaneResultados.setCaretPosition(posicionScroll);// asi no te lleva al final del scroll
        }
        else 
            VistaMensaje.StaticMensaje(vPrincipal, "info", 
                    "No se han encontrado resultados para esa busqueda.");
    }

}
