/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author HP
 */
public class VentaDespachoTotal implements Serializable{
    
    private String terminal;

    private int contador;

    private BigInteger cantidadTotal;


    public VentaDespachoTotal() {
    }

    public VentaDespachoTotal(String terminal) {
        this.terminal = terminal;
    }

    public VentaDespachoTotal(String terminal, int contador, BigInteger cantidadTotal) {
        this.terminal = terminal;
        this.contador = contador;
        this.cantidadTotal = cantidadTotal; 
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (terminal != null ? terminal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VentaDespachoTotal)) {
            return false;
        }
        VentaDespachoTotal other = (VentaDespachoTotal) object;
        if ((this.terminal == null && other.terminal != null) || (this.terminal != null && !this.terminal.equals(other.terminal))) {
            return false;
        }
        return true;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public BigInteger getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(BigInteger cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }
    
    
}
