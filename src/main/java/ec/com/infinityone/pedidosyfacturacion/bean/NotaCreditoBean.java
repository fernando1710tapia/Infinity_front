/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.pedidosyfacturacion.bean;

import ec.com.infinityone.actorcomercial.bean.ComercializadoraBean;
import ec.com.infinityone.bean.TerminalBean;
import ec.com.infinityone.modeloWeb.Cliente;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Andres
 */
@Named
@ViewScoped
public class NotaCreditoBean extends FacturacionBean implements Serializable{
     
    /**
     * Constructor por defecto
     */
    public NotaCreditoBean() {
    }
    
     public void refacturar() {
        reestablecer();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        } else {
            seleccionarComercializdora();
        }
        if (habilitarTerminal) {
            terminal = new TerminalBean();
        } else {
            seleccionarTerminal();
        }
        if (habilitarCli) {
            cliente = new Cliente();
        }

        mostarFactura = true;
        mostarPantallaInicial = false;
    }

}
