/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author SonyVaio
 */

public class Totalgarantizado implements Serializable {
   
    protected TotalgarantizadoPK totalgarantizadoPK;
   
    private BigDecimal totaldeuda;
    
    private BigDecimal totalgarantizado;
    
    private String usuarioactual;
    
    private Cliente cliente;

    public Totalgarantizado() {
    }

    public Totalgarantizado(TotalgarantizadoPK totalgarantizadoPK) {
        this.totalgarantizadoPK = totalgarantizadoPK;
    }

    public Totalgarantizado(String codigocomercializadora, String codigocliente) {
        this.totalgarantizadoPK = new TotalgarantizadoPK(codigocomercializadora, codigocliente);
    }

    public TotalgarantizadoPK getTotalgarantizadoPK() {
        return totalgarantizadoPK;
    }

    public void setTotalgarantizadoPK(TotalgarantizadoPK totalgarantizadoPK) {
        this.totalgarantizadoPK = totalgarantizadoPK;
    }

    public BigDecimal getTotaldeuda() {
        return totaldeuda;
    }

    public void setTotaldeuda(BigDecimal totaldeuda) {
        this.totaldeuda = totaldeuda;
    }

    public BigDecimal getTotalgarantizado() {
        return totalgarantizado;
    }

    public void setTotalgarantizado(BigDecimal totalgarantizado) {
        this.totalgarantizado = totalgarantizado;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (totalgarantizadoPK != null ? totalgarantizadoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Totalgarantizado)) {
            return false;
        }
        Totalgarantizado other = (Totalgarantizado) object;
        if ((this.totalgarantizadoPK == null && other.totalgarantizadoPK != null) || (this.totalgarantizadoPK != null && !this.totalgarantizadoPK.equals(other.totalgarantizadoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Totalgarantizado[ totalgarantizadoPK=" + totalgarantizadoPK + " ]";
    }
    
}
