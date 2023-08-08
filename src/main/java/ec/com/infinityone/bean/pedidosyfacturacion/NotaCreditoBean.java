/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.pedidosyfacturacion;

import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Terminal;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Andres
 */
@Named
@ViewScoped
public class NotaCreditoBean extends FacturacionBean implements Serializable {

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
            seleccionarComercializadora();
        }
        if (habilitarTerminal) {
            terminal = new Terminal();
        } else {
            seleccionarTerminal(2);
        }
        if (habilitarCli) {
            cliente = new Cliente();
        }

        mostarFactura = true;
        mostarPantallaInicial = false;
    }
    
}
