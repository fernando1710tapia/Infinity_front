/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;

/**
 *
 * @author HP
 */
public class NotaPedidoSOAP implements Serializable {
    
    private String cadena;
    private String codigoabastecedora;
    private String codigocomercializadora;
    private String numero;

    public String getCadena() {
        return cadena;
    }

    /**
     * @param cadena the cadena to set
     */
    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    /**
     * @return the codigoabastecedora
     */
    public String getCodigoabastecedora() {
        return codigoabastecedora;
    }

    /**
     * @param codigoabastecedora the codigoabastecedora to set
     */
    public void setCodigoabastecedora(String codigoabastecedora) {
        this.codigoabastecedora = codigoabastecedora;
    }

    /**
     * @return the codigocomercializadora
     */
    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    /**
     * @param codigocomercializadora the codigocomercializadora to set
     */
    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
}
