/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.bean.comun;

import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.serivicio.actorcomercial.ClienteServicio;
import ec.com.infinityone.bean.TerminalBean;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author HP
 */
@Named
@ViewScoped
public class ComunBean extends ReusableBean implements Serializable{

    private static final Logger LOG = Logger.getLogger(ComunBean.class.getName());
    /*
    Variable para acceder a los servicios de Cliente
     */
    @Inject
    protected ClienteServicio clienteServicio;
     /*
    Variable para habilitar comercializadora
     */
    protected boolean habilitarComer;
    /*
    Variable para habilitar cliente
     */
    protected boolean habilitarCli;
    /*
    Variable para almacenar los datos comercializadora
     */
    protected List<ComercializadoraBean> listaComercializadora;
    /*
    Variable para almacenar los datos clientes
     */
    protected List<Cliente> listaClientes;
    /*
    Variable Comercializadora
     */
    protected ComercializadoraBean comercializadora;
    /*
    Variable Cliente
     */
    protected Cliente cliente;
    
    
    public void habilitarComerCliPorUsuario() {
        if (x != null) {
            if (x.getNiveloperacion().equals("cero")) {
                habilitarComer = true;
                habilitarCli = true;
            }
            else if (x.getNiveloperacion().equals("adco")) {
                habilitarComer = false;
                habilitarCli = true;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(x.getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
            }
            else if (x.getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                habilitarCli = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(x.getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                    }
                }
                listaClientes = clienteServicio.obtenerClientesPorComercializadora(comercializadora.getCodigo());
                for (int i = 0; i < listaClientes.size(); i++) {
                    if (listaClientes.get(i).getCodigo().equals(x.getCodigocliente())) {
                        this.cliente = listaClientes.get(i);
                    }
                }
            }
        }
    }

    public boolean isHabilitarComer() {
        return habilitarComer;
    }

    public void setHabilitarComer(boolean habilitarComer) {
        this.habilitarComer = habilitarComer;
    }

    public boolean isHabilitarCli() {
        return habilitarCli;
    }

    public void setHabilitarCli(boolean habilitarCli) {
        this.habilitarCli = habilitarCli;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    
    
}
